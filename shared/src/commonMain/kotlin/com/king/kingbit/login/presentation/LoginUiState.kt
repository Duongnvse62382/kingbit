package com.king.kingbit.login.presentation

sealed class LoginUiState() {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
}

sealed class LoginEvent {
    data object Ide: LoginEvent()
    data class ShowError(val message: String): LoginEvent()
    data object NavigateHome: LoginEvent()
}