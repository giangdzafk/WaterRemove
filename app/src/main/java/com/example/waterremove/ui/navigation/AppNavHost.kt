package com.example.waterremove.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waterremove.ui.feature.home.HomeScreen
import com.example.waterremove.ui.feature.language.LanguageScreen
import com.example.waterremove.ui.feature.onboarding.OnboardingIntroScreen
import com.example.waterremove.ui.feature.onboarding.OnboardingSelectScreen
import com.example.waterremove.ui.feature.splash.SplashScreen
import com.example.waterremove.ui.feature.welcome.WelcomeScreen

object Routes {
    const val SPLASH = "splash"
    const val LANGUAGE = "language"
    const val ONBOARDING_1 = "onboarding_1"
    const val ONBOARDING_2 = "onboarding_2"
    const val ONBOARDING_3 = "onboarding_3"
    const val WELCOME = "welcome"
    const val HOME = "home"
}

/**
 * Lần đầu: Splash > Language > Onb1 > Onb2 > (native_full_1) > Onb3 > (native_full_2) > Welcome > Home
 * Các lần sau: Splash > Home
 * (inter_splash / native_full sẽ chèn vào khi gắn AdMob)
 */
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onFinished = { firstOpen ->
                navController.navigate(if (firstOpen) Routes.LANGUAGE else Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true } // back không quay lại splash
                }
            })
        }
        composable(Routes.LANGUAGE) {
            LanguageScreen(onNext = { navController.navigate(Routes.ONBOARDING_1) })
        }
        composable(Routes.ONBOARDING_1) {
            OnboardingSelectScreen(onNext = { navController.navigate(Routes.ONBOARDING_2) })
        }
        composable(Routes.ONBOARDING_2) {
            OnboardingIntroScreen(page = 2, onNext = { navController.navigate(Routes.ONBOARDING_3) })
        }
        composable(Routes.ONBOARDING_3) {
            OnboardingIntroScreen(page = 3, onNext = { navController.navigate(Routes.WELCOME) })
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(onGetStarted = {
                navController.navigate(Routes.HOME) {
                    popUpTo(0) { inclusive = true } // xoá toàn bộ luồng onboarding khỏi back stack
                }
            })
        }
        composable(Routes.HOME) {
            HomeScreen()
        }
    }
}
