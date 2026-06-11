package mx.utng.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    SmartHealthMonitorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 🔵 Logo: Corazón azul
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo SmartHealth",
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFF2196F3)  // 🔵 Azul (Material Blue 500)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = "SmartHealth Monitor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Campo Correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text("Correo electrónico") },
                    placeholder = { Text("usuario@ejemplo.com") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null && errorMessage!!.contains("correo", ignoreCase = true),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Contraseña") },
                    placeholder = { Text("Mínimo 6 caracteres") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errorMessage != null && errorMessage!!.contains("contraseña", ignoreCase = true),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )

                // Mensaje de error
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón ENTRAR
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null

                        when {
                            email.isBlank() -> {
                                errorMessage = "✗ Ingresa un correo electrónico"
                                isLoading = false
                            }
                            !email.contains("@") -> {
                                errorMessage = "✗ Correo inválido (debe contener @)"
                                isLoading = false
                            }
                            password.isBlank() -> {
                                errorMessage = "✗ Ingresa tu contraseña"
                                isLoading = false
                            }
                            password.length < 6 -> {
                                errorMessage = "✗ La contraseña debe tener al menos 6 caracteres"
                                isLoading = false
                            }
                            else -> {
                                isLoading = false
                                onLoginSuccess()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciando sesión...")
                    } else {
                        Text("ENTRAR", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enlace "¿Olvidaste tu contraseña?"
                TextButton(
                    onClick = { /* TODO: Implementar recuperación de contraseña */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


            }
        }
    }
}