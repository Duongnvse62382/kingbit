package com.king.kingbit.android.presentation.home.bottom_nav

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItemState(
    val title : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector
)
