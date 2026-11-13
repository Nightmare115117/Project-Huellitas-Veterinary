package com.Proyect_DevOps.Security.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Service
public class RateLimitService {

    private static final int CLEANUP_INTERVAL = 256;

    private final ConcurrentHashMap<String, ClientBucket> buckets = new ConcurrentHashMap<>();
    private final AtomicLong requestCount = new AtomicLong();
    private final int capacity;
    private final Duration refillPeriod;
    private final long idleExpirationNanos;

    public RateLimitService(
            @Value("${app.rate-limit.capacity:100}") int capacity,
            @Value("${app.rate-limit.refill-period-seconds:60}") long refillPeriodSeconds) {
        if (capacity < 1) {
            throw new IllegalArgumentException("La capacidad del rate limit debe ser mayor que cero");
        }
        if (refillPeriodSeconds < 1) {
            throw new IllegalArgumentException("El periodo de recarga debe ser mayor que cero");
        }

        this.capacity = capacity;
        this.refillPeriod = Duration.ofSeconds(refillPeriodSeconds);
        this.idleExpirationNanos = Math.multiplyExact(this.refillPeriod.toNanos(), 2);
    }

    public boolean tryConsume(String clientKey) {
        Objects.requireNonNull(clientKey, "clientKey no puede ser null");
        if (clientKey.isBlank()) {
            throw new IllegalArgumentException("clientKey no puede estar vacio");
        }

        long now = System.nanoTime();
        ClientBucket clientBucket = buckets.compute(clientKey, (key, current) -> {
            if (current == null || now - current.lastAccessNanos >= idleExpirationNanos) {
                current = new ClientBucket(createBucket(), now);
            } else {
                current.lastAccessNanos = now;
            }
            return current;
        });

        boolean allowed;
        synchronized (clientBucket) {
            allowed = clientBucket.bucket.tryConsume(1);
        }

        if (requestCount.incrementAndGet() % CLEANUP_INTERVAL == 0) {
            removeExpiredBuckets(System.nanoTime());
        }
        return allowed;
    }

    public long getRetryAfterSeconds() {
        long nanosPerToken = refillPeriod.toNanos() / capacity;
        if (refillPeriod.toNanos() % capacity != 0) {
            nanosPerToken++;
        }
        long secondsPerToken = nanosPerToken / 1_000_000_000L;
        if (nanosPerToken % 1_000_000_000L != 0) {
            secondsPerToken++;
        }
        return Math.max(1, secondsPerToken);
    }

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, refillPeriod));
        return Bucket.builder().addLimit(limit).build();
    }

    private void removeExpiredBuckets(long now) {
        for (String clientKey : buckets.keySet()) {
            buckets.computeIfPresent(clientKey, (key, clientBucket) -> {
                synchronized (clientBucket) {
                    return now - clientBucket.lastAccessNanos >= idleExpirationNanos
                            ? null
                            : clientBucket;
                }
            });
        }
    }

    private static final class ClientBucket {
        private final Bucket bucket;
        private volatile long lastAccessNanos;

        private ClientBucket(Bucket bucket, long lastAccessNanos) {
            this.bucket = bucket;
            this.lastAccessNanos = lastAccessNanos;
        }
    }
}