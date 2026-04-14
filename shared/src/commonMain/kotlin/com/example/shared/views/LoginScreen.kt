package com.example.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.viewmodel.AuthViewModel
import newsapp.shared.generated.resources.Res
import newsapp.shared.generated.resources.dont_have_acc
import newsapp.shared.generated.resources.email_address
import newsapp.shared.generated.resources.login_to_continue
import newsapp.shared.generated.resources.password
import newsapp.shared.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onLogin: () -> Unit,
    viewModel: AuthViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }

        Text(
            text = stringResource(Res.string.welcome_back),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 32.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(Res.string.login_to_continue),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp).align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(Res.string.email_address)) },
            placeholder = { Text("example@mail.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3B82F6),
                focusedLabelColor  = Color(0xFF3B82F6)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text(stringResource(Res.string.password)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3B82F6),
                focusedLabelColor  = Color(0xFF3B82F6)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.authError != null) {
            Text(
                viewModel.authError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(email, password, onLogin)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))

        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.align(Alignment.CenterVertically))
            }else{
                Text("Login", fontSize = 18.sp, color = Color(0xFFFFFFFF))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {onNavigateToSignup()}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                stringResource(Res.string.dont_have_acc),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

