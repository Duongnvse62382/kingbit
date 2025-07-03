package com.king.kingbit.android.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.king.kingbit.android.R

@Composable
fun ConnectWithApp(
    modifier: Modifier = Modifier,
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onAppleClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically


    ) {
        IconButton(onClick = onGoogleClick) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_fb),
                contentDescription = "Google",
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(onClick = onFacebookClick) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_google),
                contentDescription = "Facebook",
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(onClick = onAppleClick) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_apple),
                contentDescription = "Apple",
                modifier = Modifier.size(40.dp)
            )
        }
    }

}