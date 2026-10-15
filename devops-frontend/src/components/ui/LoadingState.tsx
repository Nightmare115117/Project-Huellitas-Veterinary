export default function LoadingState({ message }: { message: string }) {
  return (
    <div className="loading" role="status" aria-live="polite">
      <span className="spinner" aria-hidden />
      {message}
    </div>
  )
}
