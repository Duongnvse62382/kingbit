package com.king.kingbit.android.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.king.kingbit.android.R
import com.king.kingbit.util.Route
import com.king.kingbit.android.design.KingBitButton
import com.king.kingbit.android.design.KingBitLink
import com.king.kingbit.android.design.KingBitTextField
import com.king.kingbit.android.presentation.login.components.ConnectWithApp
import com.king.kingbit.android.presentation.login.components.ConnectWithDivider
import com.king.kingbit.android.presentation.login.components.CustomDialogUI
import com.king.kingbit.android.util.DeviceConfiguration
import com.king.kingbit.login.presentation.LoginAction
import com.king.kingbit.login.presentation.AuthenticationEvent
import com.king.kingbit.login.presentation.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = koinViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    var loginErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        loginViewModel.eventAuth.collect { event ->
            when (event) {
                is AuthenticationEvent.ShowError -> {
                    showDialog = true
                    loginErrorMessage = event.message
                }

                is AuthenticationEvent.Idle -> {
                    showDialog = false
                    loginErrorMessage = ""
                }

                is AuthenticationEvent.NavigateHome -> {
                    navController.popBackStack()
                    navController.navigate(Route.Home)
                }

                AuthenticationEvent.NavigationRegister -> {
                    navController.navigate(Route.Register)
                }
            }
        }
    }
    LoginScreen(
        showDialog = showDialog, loginErrorMessage = loginErrorMessage, onAction = loginViewModel::onAction
    )
}


@Composable
fun LoginScreen(showDialog: Boolean, loginErrorMessage : String, onAction: (LoginAction) -> Unit) {

    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }, contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        val rootModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .clip(
                RoundedCornerShape(
                    topStart = 15.dp, topEnd = 15.dp
                )
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = 16.dp, vertical = 24.dp
            )
            .consumeWindowInsets(WindowInsets.navigationBars)

        if (showDialog) {
            KingBitDialog(title = "Incorrect Credentials", message = loginErrorMessage, drawableIcon = R.drawable.logo_kingbit, onDismiss = {
                onAction(LoginAction.ResetLogin)
            }, onAllow = {

            })
        }

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        when (DeviceConfiguration.fromWindowSizeClass(windowSizeClass)) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                Column(
                    modifier = rootModifier, verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    LoginHeaderSection(
                        modifier = Modifier.fillMaxWidth()
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onPasswordTextChange = { passwordText = it },
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText, password = passwordText
                                )
                            )
                        },
                        onNavigationRegister = {
                            onAction(LoginAction.GoRegister)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        focusRequester = focusRequester,
                        focusManager = focusManager
                    )
                }
            }

            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                Row(
                    modifier = rootModifier
                        .windowInsetsPadding(WindowInsets.displayCutout)
                        .padding(
                            horizontal = 32.dp
                        ), horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    LoginHeaderSection(
                        modifier = Modifier.weight(1f)
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onPasswordTextChange = { passwordText = it },
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText, password = passwordText
                                )
                            )
                        },
                        onNavigationRegister = {
                            onAction(LoginAction.GoRegister)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        focusRequester = focusRequester,
                        focusManager = focusManager
                    )
                }
            }

            DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE, DeviceConfiguration.DESKTOP -> {
                Column(
                    modifier = rootModifier
                        .verticalScroll(rememberScrollState())
                        .padding(top = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginHeaderSection(
                        modifier = Modifier.widthIn(max = 540.dp),
                        alignment = Alignment.CenterHorizontally
                    )
                    LoginFormSection(
                        emailText = emailText,
                        onEmailTextChange = { emailText = it },
                        passwordText = passwordText,
                        onPasswordTextChange = { passwordText = it },
                        onLogin = {
                            onAction(
                                LoginAction.LoginKingBit(
                                    username = emailText, password = passwordText
                                )
                            )
                        },
                        onNavigationRegister = {
                            onAction(LoginAction.GoRegister)
                        },
                        modifier = Modifier.widthIn(max = 540.dp),
                        focusRequester = focusRequester,
                        focusManager = focusManager
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
        modifier = modifier, horizontalAlignment = alignment
    ) {
        Text(
            text = "Welcome Back!",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Let’s login for explore continues",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.size(10.dp))
        Box(modifier = modifier.fillMaxWidth()) {

            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_kingbit_dark else R.drawable.logo_kingbit),
                contentDescription = "Image Login"
            )
        }
    }

}

@Composable
fun LoginFormSection(
    modifier: Modifier = Modifier,
    emailText: String,
    onEmailTextChange: (String) -> Unit,
    passwordText: String,
    onPasswordTextChange: (String) -> Unit,
    onLogin: () -> Unit,
    onNavigationRegister: () -> Unit,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
) {
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    Column(
        modifier = modifier
    ) {
        KingBitTextField(
            text = emailText,
            onValueChange = {
                onEmailTextChange(it)
                emailError = ""
            },
            onDone = {},
            label = "Email",
            hint = "Enter your email",
            isInputSecret = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
            drawable = R.drawable.ic_email,
            focusRequester = focusRequester,
            focusManager = focusManager,
            isError = emailError.isNotEmpty(),
            errorMessage = emailError

        )

        Spacer(modifier = Modifier.height(16.dp))
        KingBitTextField(
            text = passwordText,
            onValueChange = {
                onPasswordTextChange(it)
                passwordError = ""
            },
            onDone = {
                focusManager.clearFocus()
                emailError = when {
                    emailText.isEmpty() -> "Please enter your email address."
                    !validateEmail(emailText) -> "Enter a valid email address."
                    else -> ""
                }
                passwordError = if (passwordText.isEmpty()) "Please enter your password." else ""
                if (emailText.isNotEmpty() && passwordText.isNotEmpty() && validateEmail(emailText)) {
                    onLogin()
                }
            },
            label = "Password",
            hint = "Enter your password",
            isInputSecret = true,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            drawable = R.drawable.ic_password,
            focusRequester = focusRequester,
            focusManager = focusManager,
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError
        )
        Spacer(modifier = Modifier.height(24.dp))
        KingBitButton(
            modifier = Modifier.fillMaxWidth(), text = "Log In", onClick = {
                focusManager.clearFocus()
                emailError = when {
                    emailText.isEmpty() -> "Please enter your email address."
                    !validateEmail(emailText) -> "Enter a valid email address."
                    else -> ""
                }
                passwordError = if (passwordText.isEmpty()) "Please enter your password." else ""

                if (emailText.isNotEmpty() && passwordText.isNotEmpty() && validateEmail(emailText)) {
                    onLogin()
                }

            }, enabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))

        ConnectWithDivider()

        Spacer(modifier = Modifier.height(16.dp))

        ConnectWithApp()
        Spacer(modifier = Modifier.height(16.dp))

        KingBitLink(
            text = "Don't have an account?", onClick = {
                onNavigationRegister()
            }, modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun KingBitDialog(
    title: String, message: String, drawableIcon: Int, onDismiss: () -> Unit, onAllow: () -> Unit
) {
    Dialog(onDismissRequest = {

    }) {
        CustomDialogUI(
            title = title,
            message = message,
            drawableIcon = drawableIcon,
            onDismiss = onDismiss,
            onAllow = onAllow
        )
    }
}

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}