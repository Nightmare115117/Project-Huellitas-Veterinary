package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.GlassBackground
import com.example.myapplication.ui.GlassCard
import com.example.myapplication.ui.glassFieldColors
import com.example.myapplication.ui.theme.Blue300
import com.example.myapplication.ui.theme.Blue600
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Red300
import com.example.myapplication.ui.theme.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private const val API_BASE_URL = "https://dev-server.tail223158.ts.net/huellitas/api"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La UI siempre es oscura: iconos claros en las barras aunque el sistema esté en modo claro
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var roleId by remember { mutableStateOf<Int?>(null) }
    var feedbackText by remember { mutableStateOf("") }
    var feedbackColor by remember { mutableStateOf(Success) }
    val scope = rememberCoroutineScope()

    if (roleId != null) {
        RolePanel(roleId = roleId!!, onLogout = { roleId = null })
        return
    }

    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp)) {
                Text(
                    text = "Huellitas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Blue300
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

            GlassCard(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    colors = glassFieldColors()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = glassFieldColors()
                )

                if (feedbackText.isNotEmpty()) {
                    Text(
                        text = feedbackText,
                        color = feedbackColor,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        // TalkBack anuncia el error sin tener que buscarlo
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }

                Button(
                    enabled = !isLoading,
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            feedbackText = "Completa correo y contraseña"
                            feedbackColor = Red300
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
                                feedbackText = "Credenciales incorrectas"
                                feedbackColor = Red300
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
                    colors = ButtonDefaults.textButtonColors(contentColor = Blue300),
                    onClick = { }
                ) {
                    Text("Crear cuenta")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                infoPill("Agenda", "3 citas")
                infoPill("Historial", "12 pets")
            }

            GlassCard(
                Modifier.fillMaxWidth(),
                tint = Blue600.copy(alpha = 0.22f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Hoy",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Consulta de Luna",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "15:30 • Revisión general • Dr. Mejía",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun RolePanel(roleId: Int, onLogout: () -> Unit) {
    val panel: Triple<String, String, List<String>> = when (roleId) {
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
    val (title, subtitle, actions) = panel

    GlassBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Huellitas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Blue300
            )
            Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            actions.forEach { action ->
                GlassCard(Modifier.fillMaxWidth(), onClick = { }) {
                    Text(action, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = onLogout,
                colors = ButtonDefaults.textButtonColors(contentColor = Red300),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun infoPill(title: String, subtitle: String) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(0.48f),
        shape = RoundedCornerShape(18.dp),
        padding = 16.dp,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PhoneScreenPreview() {
    MyApplicationTheme {
        PhoneScreen()
    }
}
