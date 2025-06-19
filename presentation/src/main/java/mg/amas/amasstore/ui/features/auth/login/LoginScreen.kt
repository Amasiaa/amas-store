package mg.amas.amasstore.ui.features.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mg.amas.amasstore.R
import mg.amas.amasstore.navigation.HomeScreen
import mg.amas.amasstore.navigation.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val loginEvent = viewModel.loginState.collectAsState()

    when (val event = loginEvent.value) {
        is LoginEvent.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
                Text(text = stringResource(id = R.string.loading_login), style = MaterialTheme.typography.bodyMedium)
            }
        }
        is LoginEvent.Error -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(event.message, color = Color.Red)
            }
        }
        is LoginEvent.Success -> {
            LaunchedEffect(event) {
                navController.navigate(HomeScreen) {
                    popUpTo(HomeScreen) {
                        inclusive = true
                    }
                }
            }
        }
        else -> {
            LoginContent(
                onSignUpClicked = { navController.navigate(RegisterScreen) },
                onSignInClicked = { email, password ->
                    viewModel.login(email, password)
                },
            )
        }
    }
}

@Composable
fun LoginContent(
    onSignInClicked: (String, String) -> Unit,
    onSignUpClicked: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W900),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                ),
            label = { Text(text = stringResource(id = R.string.name)) },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            colors =
                OutlinedTextFieldDefaults.colors().copy(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = Color.Gray,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = Color.Gray,
                ),
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                ),
            label = { Text(text = stringResource(id = R.string.password)) },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(mask = '*'),
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        painter =
                            painterResource(
                                id = if (showPassword) R.drawable.outline_visibility_24 else R.drawable.outline_visibility_off_24,
                            ),
                        contentDescription = null,
                    )
                }
            },
            colors =
                OutlinedTextFieldDefaults.colors().copy(
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedLeadingIconColor = Color.Gray,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = Color.Gray,
                ),
        )

        Button(
            onClick = { onSignInClicked(email, password) },
            modifier = Modifier.fillMaxWidth(0.75f),
            enabled = email.isNotEmpty() && password.isNotEmpty(),
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.size(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(id = R.string.not_have_account), style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = { onSignUpClicked() }) {
                Text(
                    text = stringResource(id = R.string.register),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W600),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview() {
    LoginContent(onSignInClicked = { email, password -> }, onSignUpClicked = {})
}
