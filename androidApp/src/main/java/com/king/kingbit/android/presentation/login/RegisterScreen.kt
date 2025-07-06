package com.king.kingbit.android.presentation.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.king.kingbit.android.R
import com.king.kingbit.android.design.KingBitTextField
import com.king.kingbit.login.domain.model.RegisterStep
import com.king.kingbit.login.domain.model.checkPassword
import com.king.kingbit.login.presentation.LoginAction
import com.king.kingbit.login.presentation.LoginViewModel
import com.king.kingbit.login.presentation.RegisterEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navController: NavController, loginViewModel: LoginViewModel = koinViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var registerStep by remember { mutableStateOf(RegisterStep.EMAIL) }
    var errorMessage by remember { mutableStateOf("") }
    var isRegistered by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        loginViewModel.eventRegister.collect { event ->
            when (event) {
                RegisterEvent.Idle -> {
                    errorMessage = ""
                    showDialog = false
                }

                RegisterEvent.NextPasswordStep -> {
                    registerStep = RegisterStep.PASSWORD
                }

                is RegisterEvent.ShowError -> {
                    errorMessage = event.message
                }

                is RegisterEvent.RegisterStatus -> {
                    showDialog = true
                    isRegistered = event.isRegisterSuccess
                }
            }
        }
    }

    RegisterScreen(
        errorMessage = errorMessage,
        registerStep = registerStep,
        showDialog = showDialog,
        isRegistered = isRegistered,
        onAction = loginViewModel::onAction,
        onBack = {
            navController.popBackStack()
        },
        onRegisterStep = {
            registerStep = it
        })
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    errorMessage: String,
    registerStep: RegisterStep,
    showDialog: Boolean,
    isRegistered: Boolean,
    onAction: (LoginAction) -> Unit,
    onBack: () -> Unit,
    onRegisterStep: (RegisterStep) -> Unit
) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var passwordConfirmText by remember { mutableStateOf("") }


    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = modifier
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
            KingBitDialog(
                title = if (isRegistered) "Registration Successful" else "Registration Failed",
                message = if (isRegistered) "Your account has been created successfully. You can now log in and start using the app." else "Something went wrong during registration. Please try again later.",
                onDismiss = {
                    onAction(LoginAction.ResetLogin)
                    if (isRegistered) {
                        onBack()
                    }
                },
                drawableIcon = if(isRegistered) R.drawable.image_sucess else R.drawable.logo_kingbit,
                onAllow = {})
        }

        Column(
            modifier = rootModifier, verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            RegisterHeaderSection(
                modifier = Modifier.fillMaxWidth(), registerStep = registerStep
            )


            AnimatedContent(
                targetState = registerStep, label = "register-step"
            ) { step ->
                when (step) {
                    RegisterStep.EMAIL -> {
                        RegisterEmailSection(
                            emailText = emailText,
                            onEmailTextChange = {
                                emailText = it
                            },
                            errorMessage = errorMessage,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            onNext = {
                                onAction(LoginAction.CheckEmailExited(emailText))
                            },
                            onBack = onBack,
                            onAction = onAction
                        )
                    }

                    RegisterStep.PASSWORD -> {
                        RegisterPasswordSection(
                            passwordText = passwordText,
                            onPasswordTextChange = {
                                passwordText = it
                            },
                            passwordConfirmText = passwordConfirmText,
                            onPasswordConfirmTextChange = {
                                passwordConfirmText = it
                            },
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            onSubmit = {
                                onAction(LoginAction.RegisterKingBit(emailText, passwordText))
                            },
                            onBack = {
                                onRegisterStep(RegisterStep.EMAIL)
                            })
                    }
                }
            }
        }
    }
}


@Composable
fun RegisterHeaderSection(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    registerStep: RegisterStep
) {

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }

    LaunchedEffect(registerStep) {
        title = when (registerStep) {
            RegisterStep.EMAIL -> "What's your email address"
            RegisterStep.PASSWORD -> "Create your password"
        }

        subtitle = when (registerStep) {
            RegisterStep.EMAIL -> "We’ll use it to create your account"
            RegisterStep.PASSWORD -> "your password must be at 8 characters long, and contain at least on letter and one digit."
        }
    }

    Column(modifier = modifier, horizontalAlignment = alignment) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_kingbit_dark else R.drawable.logo_kingbit),
            contentDescription = "Image Login"
        )

        Spacer(modifier = Modifier.size(10.dp))

        HorizontalDivider(thickness = 2.dp)

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = title, style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = subtitle, style = MaterialTheme.typography.labelMedium
        )
    }

}


@Composable
fun RegisterEmailSection(
    modifier: Modifier = Modifier,
    emailText: String,
    onEmailTextChange: (String) -> Unit,
    errorMessage: String,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onAction: (LoginAction) -> Unit
) {
    var emailError by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        emailError = errorMessage
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        KingBitTextField(
            text = emailText,
            onValueChange = {
                onAction(LoginAction.ResetLogin)
                onEmailTextChange(it)
                emailError = ""
            },
            onDone = {
                focusManager.clearFocus()
                emailError = when {
                    emailText.isEmpty() -> "Please enter your email address."
                    !validateEmail(emailText) -> "Enter a valid email address."
                    else -> ""
                }
            },
            label = "Email",
            hint = "Enter your email",
            isInputSecret = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
            drawable = R.drawable.ic_email,
            focusRequester = focusRequester,
            focusManager = focusManager,
            isError = emailError.isNotEmpty(),
            errorMessage = emailError,
            imeAction = ImeAction.Done
        )


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(5.dp, 0.dp, 5.dp, 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                modifier = Modifier.height(45.dp), onClick = {
                    onBack()
                }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.background,
                ), border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Button(
                onClick = {
                    if (emailText.isNotEmpty() && validateEmail(emailText)) {
                        onAction(LoginAction.ResetLogin)
                        onNext()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
                ),
                enabled = emailText.isNotEmpty() && validateEmail(emailText),
                contentPadding = PaddingValues(12.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center), text = "Next", fontSize = 18.sp
                    )

                    Icon(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = if (emailText.isNotEmpty() && validateEmail(emailText)) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterPasswordSection(
    modifier: Modifier = Modifier,
    passwordText: String,
    onPasswordTextChange: (String) -> Unit,
    passwordConfirmText: String,
    onPasswordConfirmTextChange: (String) -> Unit,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    var passwordError by remember { mutableStateOf("") }
    var passwordErrorConfirm by remember { mutableStateOf("") }

    val requirements = remember(passwordText) { checkPassword(passwordText) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier) {
            KingBitTextField(
                text = passwordText,
                onValueChange = {
                    onPasswordTextChange(it)
                    passwordError = ""
                    passwordErrorConfirm = ""
                },
                onDone = {
                    focusManager.clearFocus()
                },
                label = "Password",
                hint = "Enter your password",
                isInputSecret = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password,
                drawable = R.drawable.ic_password,
                focusRequester = focusRequester,
                focusManager = focusManager,
                isError = passwordError.isNotEmpty(),
                errorMessage = passwordError,
                imeAction = ImeAction.Done,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ValidationItem("Has at least 8 characters?", requirements.isAtLeast8Chars)
            ValidationItem("Has one letter?", requirements.hasLetter)
            ValidationItem("Has one digit?", requirements.hasDigit)

            Spacer(modifier = Modifier.height(24.dp))

            KingBitTextField(
                text = passwordConfirmText,
                onValueChange = {
                    onPasswordConfirmTextChange(it)
                    passwordErrorConfirm = ""
                },
                onDone = {
                    focusManager.clearFocus()
                    if (passwordText != passwordConfirmText) {
                        passwordErrorConfirm = "Password not math"
                    }
                },
                label = "Confirm Password",
                hint = "Enter your password",
                isInputSecret = true,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
                drawable = R.drawable.ic_password,
                focusRequester = focusRequester,
                focusManager = focusManager,
                isError = passwordErrorConfirm.isNotEmpty(),
                errorMessage = passwordErrorConfirm,
                enable = requirements.hasDigit && requirements.hasLetter && requirements.isAtLeast8Chars
            )
        }



        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(5.dp, 0.dp, 5.dp, 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                modifier = Modifier.height(45.dp), onClick = {
                    onBack()
                }, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.background,
                ), border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    onSubmit()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White
                ),
                enabled = passwordConfirmText.isNotEmpty() && passwordText == passwordConfirmText,
                contentPadding = PaddingValues(12.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Submit",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ValidationItem(text: String, isValid: Boolean) {
    val icon = if (isValid) Icons.Default.CheckCircle else Icons.Default.Warning
    val iconColor = if (isValid) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (isValid) MaterialTheme.colorScheme.onBackground else Color.Gray
        )
    }
}