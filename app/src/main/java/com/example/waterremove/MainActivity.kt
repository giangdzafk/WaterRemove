package com.example.waterremove

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.waterremove.ui.navigation.AppNavHost
import com.example.waterremove.ui.theme.WaterRemoveTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dùng AppCompatActivity (không phải ComponentActivity) để
 * AppCompatDelegate.setApplicationLocales() đổi được ngôn ngữ trên Android < 13.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // splash hệ thống, rồi chuyển sang Theme.WaterRemove
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterRemoveTheme {
                AppNavHost()
            }
        }
    }
}
