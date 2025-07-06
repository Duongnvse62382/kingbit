package com.king.kingbit.login.domain.model

data class PasswordRequirements(
    val isAtLeast8Chars: Boolean,
    val hasLetter: Boolean,
    val hasDigit: Boolean
)

fun checkPassword(password: String): PasswordRequirements {
    val isAtLeast8 = password.length >= 8
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    return PasswordRequirements(isAtLeast8, hasLetter, hasDigit)
}