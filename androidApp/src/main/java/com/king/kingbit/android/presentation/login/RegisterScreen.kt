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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.king.kingbit.login.presentation.RegisterStep
import com.king.kingbit.login.domain.model.checkPassword
import com.king.kingbit.login.presentation.RegisterAction
import com.king.kingbit.login.presentation.RegisterSideEffect
import com.king.kingbit.login.presentation.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController, registerViewModel: RegisterViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var isRegistered by remember { mutableStateOf(false) }
    val step by registerViewModel.step.collectAsStateWithLifecycle()
    val email by registerViewModel.email.collectAsStateWithLifecycle()
    val emailError by registerViewModel.emailError.collectAsStateWithLifecycle()
    val password by registerViewModel.password.collectAsStateWithLifecycle()
    val passwordConfirm by registerViewModel.passwordConfirm.collectAsStateWithLifecycle()
    val passwordError by registerViewModel.passwordError.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        registerViewModel.eventRegister.collect { event ->
            when (event) {
                RegisterSideEffect.Idle -> {
                    showDialog = false
                }

                is RegisterSideEffect.RegisterStatus -> {
                    showDialog = true
                    isRegistered = event.isRegisterSuccess
                }
            }
        }
    }

    RegisterScreen(
        step = step,
        showDialog = showDialog,
        isRegistered = isRegistered,
        email = email,
        password = password,
        passwordConfirm = passwordConfirm,
        emailError = emailError,
        passwordError = passwordError,
        onAction = registerViewModel::onAction,
        onBack = { navController.popBackStack() },
    )
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    step: RegisterStep,
    showDialog: Boolean,
    isRegistered: Boolean,
    email: String,
    password: String,
    passwordConfirm: String,
    emailError: String,
    passwordError: String,
    onAction: (RegisterAction) -> Unit,
    onBack: () -> Unit,
) {
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
                    onAction(RegisterAction.ResetRegister)
                    if (isRegistered) onBack()
                },
                drawableIcon = if (isRegistered) R.drawable.image_sucess else R.drawable.logo_kingbit,
                onAllow = {})
        }

        Column(
            modifier = rootModifier, verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            RegisterHeaderSection(
                modifier = Modifier.fillMaxWidth(), registerStep = step
            )

            AnimatedContent(
                targetState = step, label = "register-step"
            ) { currentStep ->
                when (currentStep) {
                    RegisterStep.EMAIL -> {
                        RegisterEmailSection(
                            emailText = email,
                            onEmailTextChange = {
                                onAction(RegisterAction.EmailChanged(it))
                            },
                            errorMessage = emailError,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            onNext = {
                                onAction(RegisterAction.CheckEmailExited(email))
                            },
                            onBack = onBack,
                        )
                    }

                    RegisterStep.PASSWORD -> {
                        RegisterPasswordSection(
                            passwordText = password,
                            onPasswordTextChange = { onAction(RegisterAction.PasswordChanged(it)) },
                            passwordConfirmText = passwordConfirm,
                            onPasswordConfirmTextChange = {
                                onAction(
                                    RegisterAction.PasswordConfirmChanged(
                                        it
                                    )
                                )
                            },
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            onSubmit = {
                                onAction(RegisterAction.RegisterKingBit(email, password))
                            },
                            onBack = {
                                onAction(RegisterAction.EmailChanged(email))
                                onBack()
                            },
                            passwordError = passwordError,
                            onCheckPasswordMatch = { onAction(RegisterAction.CheckPasswordMatch) })
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
) {
    val isEnable by remember(emailText) {
        derivedStateOf {
            emailText.isNotEmpty() && validateEmail(emailText)
        }
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        KingBitTextField(
            text = emailText,
            onValueChange = {
                onEmailTextChange(it)
            },
            onDone = {
                focusManager.clearFocus()
            },
            label = "Email",
            hint = "Enter your email",
            isInputSecret = false,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
            drawable = R.drawable.ic_email,
            focusRequester = focusRequester,
            focusManager = focusManager,
            isError = errorMessage.isNotEmpty(),
            errorMessage = errorMessage,
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (isEnable) {
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
                enabled = isEnable,
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = if (isEnable) Color.White else Color.Gray
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
    onBack: () -> Unit,
    passwordError: String,
    onCheckPasswordMatch: () -> Unit
) {
    val requirements = remember(passwordText) { checkPassword(passwordText) }
    val isEnableConfirm by remember(requirements) {
        derivedStateOf {
            requirements.hasDigit && requirements.hasLetter && requirements.isAtLeast8Chars
        }
    }

    val isEnableSubmit by remember(passwordConfirmText, passwordText) {
        derivedStateOf {
            passwordConfirmText.isNotEmpty() && passwordText == passwordConfirmText
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier) {
            KingBitTextField(
                text = passwordText,
                onValueChange = {
                    onPasswordTextChange(it)
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
                isError = false,
                errorMessage = "",
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
                },
                onDone = {
                    focusManager.clearFocus()
                    onCheckPasswordMatch()
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
                isError = passwordError.isNotEmpty(),
                errorMessage = passwordError,
                enable = isEnableConfirm
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                enabled = isEnableSubmit,
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
    val icon = if (isValid) Icons.Default.CheckCircle else Icons.Default.Close
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