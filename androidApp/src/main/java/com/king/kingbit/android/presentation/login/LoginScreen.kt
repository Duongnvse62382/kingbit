package com.king.kingbit.android.presentation.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.king.kingbit.android.R
import com.king.kingbit.android.design.KingBitButton
import com.king.kingbit.android.design.KingBitLink
import com.king.kingbit.android.design.KingBitTextField
import com.king.kingbit.android.util.DeviceConfiguration
import com.king.kingbit.login.presentation.LoginAction
import com.king.kingbit.login.presentation.LoginEvent
import com.king.kingbit.login.presentation.LoginState
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = koinViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when (event) {
                is LoginEvent.ShowError -> {
                    showDialog = true
                }

                is LoginEvent.Idle -> {
                    showDialog = false
                }

                is LoginEvent.NavigateHome -> {

                }

            }
        }
    }
    LoginScreen(
        loginState = loginState,
        showDialog = showDialog,
        onAction = loginViewModel::onAction
    )
}


@Composable
fun LoginScreen(loginState: LoginState, showDialog: Boolean, onAction: (LoginAction) -> Unit) {

    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        val rootModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .clip(
                RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 15.dp
                )
            )
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
            .consumeWindowInsets(WindowInsets.navigationBars)

        if (showDialog) {
            LoginErrorDialog(
                message = "LoginFail",
                onDismiss = {
                    onAction(LoginAction.ResetLogin)
                }
            )
        }

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        when (DeviceConfiguration.fromWindowSizeClass(windowSizeClass)) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                Column(
                    modifier = rootModifier,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    LoginHeaderSection(
                        modifier = Modifier.fillMaxWidth()
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onPasswordTextChange = { passwordText = it },
                        onEmailCheck = {
                            emailError = isValidEmail(emailText)
                        },
                        emailError = emailError,
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText,
                                    password = passwordText
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                Row(
                    modifier = rootModifier
                        .windowInsetsPadding(WindowInsets.displayCutout)
                        .padding(
                            horizontal = 32.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    LoginHeaderSection(
                        modifier = Modifier
                            .weight(1f)
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onPasswordTextChange = { passwordText = it },
                        onEmailCheck = {
                            emailError = isValidEmail(emailText)
                        },
                        emailError = emailError,
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText,
                                    password = passwordText
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }

            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.DESKTOP -> {
                Column(
                    modifier = rootModifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginHeaderSection(
                        modifier = Modifier
                            .widthIn(max = 540.dp),
                        alignment = Alignment.CenterHorizontally
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onEmailCheck = {
                            emailError = isValidEmail(emailText)
                        },
                        emailError = emailError,
                        onPasswordTextChange = { passwordText = it },
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText,
                                    password = passwordText
                                )
                            )
                        },
                        modifier = Modifier
                            .widthIn(max = 540.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginHeaderSection(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            text = "Welcome Back!",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Let’s login for explore continues",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.size(10.dp))
        Box(modifier = modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                painter = painterResource(id = R.drawable.ic_login_image),
                contentDescription = "Image Login"
            )
        }
    }

}

@Composable
fun LoginFormSection(
    emailText: String,
    onEmailTextChange: (String) -> Unit,
    onEmailCheck: () -> Unit,
    passwordText: String,
    onPasswordTextChange: (String) -> Unit,
    onLogin: () -> Unit,
    emailError : Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        KingBitTextField(
            text = emailText,
            onValueChange = onEmailTextChange,
            onDone = {},
            onCheckEmailValid = onEmailCheck,
            label = "Email or Phone Number",
            hint = "Enter your email",
            isInputSecret = false,
            isError = emailError,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardType = KeyboardType.Email,
            drawable = R.drawable.ic_email
        )

        Spacer(modifier = Modifier.height(16.dp))
        KingBitTextField(
            text = passwordText,
            onValueChange = onPasswordTextChange,
            onDone = onLogin,
            onCheckEmailValid = {},
            label = "Password",
            hint = "Enter your password",
            isInputSecret = true,
            modifier = Modifier
                .fillMaxWidth(),
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            drawable = R.drawable.ic_password
        )
        Spacer(modifier = Modifier.height(24.dp))
        KingBitButton(
            text = "Log In",
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        KingBitLink(
            text = "Don't have an account?",
            onClick = {},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun LoginErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 12.dp, // shadow
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Login failed",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = "Login Failed",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("OK")
            }
        },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            )
    )
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}