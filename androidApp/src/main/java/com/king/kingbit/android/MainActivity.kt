package com.king.kingbit.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.king.kingbit.android.app.App
import com.king.kingbit.android.theme.MyApplicationTheme


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                )
                {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = !isSystemInDarkTheme()
                    DisposableEffect(systemUiController, useDarkIcons) {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = useDarkIcons
                        )
                        onDispose {}
                    }

                    App()
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
