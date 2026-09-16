package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private const val API_BASE_URL = "https://dev-server.tail223158.ts.net/huellitas/api"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PhoneScreen()
            }
        }
    }
}

private suspend fun loginUser(email: String, password: String): Int? = withContext(Dispatchers.IO) {
    val loginUrl = URL("$API_BASE_URL/usuario/login")
    val loginConnection = loginUrl.openConnection() as HttpURLConnection
    loginConnection.requestMethod = "POST"
    loginConnection.doOutput = true
    loginConnection.doInput = true
    loginConnection.connectTimeout = 15000
    loginConnection.readTimeout = 15000
    loginConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
    loginConnection.setRequestProperty("Accept", "application/json")

    val payload = """{"correo":"${email}","contrasena":"${password}"}"""

    try {
        loginConnection.outputStream.use { outputStream ->
            outputStream.write(payload.toByteArray(Charsets.UTF_8))
            outputStream.flush()
        }

        val responseCode = loginConnection.responseCode
        val responseBody = if (responseCode in 200..299) {
            loginConnection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } else {
            loginConnection.errorStream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
        }

        if (responseCode !in 200..299 || !responseBody.contains("\"login\":true", ignoreCase = true)) {
            return@withContext null
        }
    } catch (_: Exception) {
        return@withContext null
    } finally {
        loginConnection.disconnect()
    }

    val roleUrl = URL("$API_BASE_URL/usuario/Rol?correo=${java.net.URLEncoder.encode(email, "UTF-8")}")
    val roleConnection = roleUrl.openConnection() as HttpURLConnection
    roleConnection.requestMethod = "GET"
    roleConnection.connectTimeout = 15000
    roleConnection.readTimeout = 15000
    roleConnection.setRequestProperty("Accept", "application/json")

    try {
        if (roleConnection.responseCode !in 200..299) {
            return@withContext null
        }
        roleConnection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText().trim().toIntOrNull() }
    } catch (_: Exception) {
        null
    } finally {
        roleConnection.disconnect()
    }
}

@Composable
fun PhoneScreen() {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
            MaterialTheme.colorScheme.background
        )
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var roleId by remember { mutableStateOf<Int?>(null) }
    var feedbackText by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Color(0xFF2E7D32)) }
    val scope = rememberCoroutineScope()

    if (roleId != null) {
        RolePanel(roleId = roleId!!, onLogout = { roleId = null })
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Huellitas",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Veterinary",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Cuidamos a tus mascotas con atención cercana y rápida.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    if (feedbackText.isNotEmpty()) {
                        Text(
                            text = feedbackText,
                            color = feedbackColor,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        enabled = !isLoading,
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                feedbackText = "Completa correo y contraseña"
                                feedbackColor = Color(0xFFB00020)
                                return@Button
                            }

                            scope.launch {
                                isLoading = true
                                feedbackText = ""
                                val loggedRoleId = loginUser(email, password)
                                isLoading = false
                                if (loggedRoleId != null) {
                                    roleId = loggedRoleId
                                    feedbackText = ""
                                } else {
                                    "Credenciales incorrectas"
                                    feedbackText = "Credenciales incorrectas"
                                    feedbackColor = Color(0xFFB00020)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(if (isLoading) "Ingresando..." else "Entrar", fontSize = 16.sp)
                    }

                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = { }
                    ) {
                        Text("Crear cuenta")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                infoPill("Agenda", "3 citas")
                infoPill("Historial", "12 pets")
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hoy",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Consulta de Luna",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "15:30 • Revisón general • Dr. Mejía",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun RolePanel(roleId: Int, onLogout: () -> Unit) {
    val (title, subtitle, actions) = when (roleId) {
        1 -> Triple(
            "Panel administrativo",
            "Gestiona usuarios, mascotas, citas y la operación de Huellitas.",
            listOf("Usuarios", "Mascotas", "Citas", "Reportes")
        )
        3 -> Triple(
            "Panel del gestor",
            "Administra la sucursal y consulta sus reportes.",
            listOf("Usuarios", "Sucursales", "Reportes")
        )
        2 -> Triple(
            "Panel del dueño de la mascota",
            "Consulta la información de tus mascotas, citas y tratamientos.",
            listOf("Inicio", "Mis mascotas", "Mis citas", "Tratamientos")
        )
        else -> Triple(
            "Panel de Huellitas",
            "Tu usuario no tiene un rol configurado.",
            emptyList()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Huellitas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = subtitle, style = MaterialTheme.typography.bodyLarge)

        actions.forEach { action ->
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(action)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onLogout, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun infoPill(title: String, subtitle: String) {
    val pillColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)

    Card(
        modifier = Modifier.fillMaxWidth(0.48f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = pillColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PhoneScreenPreview() {
    MyApplicationTheme {
        PhoneScreen()
    }
}