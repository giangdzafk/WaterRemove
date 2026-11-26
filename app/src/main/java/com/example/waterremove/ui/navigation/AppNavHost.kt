package com.example.waterremove.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.waterremove.ui.feature.cleaner.CleanerFeature
import com.example.waterremove.ui.feature.cleaner.CleanerScreen
import com.example.waterremove.ui.feature.cleaner.CleaningScreen
import com.example.waterremove.ui.feature.cleaner.ResultScreen
import com.example.waterremove.ui.feature.home.HomeScreen
import com.example.waterremove.ui.feature.language.LanguageScreen
import com.example.waterremove.ui.feature.onboarding.OnboardingIntroScreen
import com.example.waterremove.ui.feature.onboarding.OnboardingSelectScreen
import com.example.waterremove.ui.feature.settings.SettingsScreen
import com.example.waterremove.ui.feature.soundtest.SoundTestScreen
import com.example.waterremove.ui.feature.splash.SplashScreen
import com.example.waterremove.ui.feature.tips.TipDetailScreen
import com.example.waterremove.ui.feature.tutorial.TutorialScreen
import com.example.waterremove.ui.feature.welcome.WelcomeScreen

object Routes {
    const val SPLASH = "splash"
    const val LANGUAGE = "language"
    const val ONBOARDING_1 = "onboarding_1"
    const val ONBOARDING_2 = "onboarding_2"
    const val ONBOARDING_3 = "onboarding_3"
    const val WELCOME = "welcome"
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val LANGUAGE_SETTINGS = "language_settings"
    const val SOUND_TEST = "sound_test"

    const val CLEANER = "cleaner/{feature}"
    const val CLEANING = "cleaning/{feature}/{level}"
    const val RESULT = "result/{feature}/{level}/{percent}"
    const val TUTORIAL = "tutorial/{feature}"
    const val TIP = "tip/{index}"

    fun cleaner(f: CleanerFeature) = "cleaner/${f.key}"
    fun cleaning(f: CleanerFeature, level: Int) = "cleaning/${f.key}/$level"
    fun result(f: CleanerFeature, level: Int, percent: Int) = "result/${f.key}/$level/$percent"
    fun tutorial(key: String) = "tutorial/$key"
    fun tip(index: Int) = "tip/$index"
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val back: () -> Unit = { navController.popBackStack() }
    val goHome: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME) { inclusive = false }
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onFinished = { firstOpen ->
                navController.navigate(if (firstOpen) Routes.LANGUAGE else Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.LANGUAGE) {
            LanguageScreen(onDone = { navController.navigate(Routes.ONBOARDING_1) })
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
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        // Home
        composable(Routes.HOME) {
            HomeScreen(
                onWater = { navController.navigate(Routes.cleaner(CleanerFeature.WATER)) },
                onDust = { navController.navigate(Routes.cleaner(CleanerFeature.DUST)) },
                onSoundTest = { navController.navigate(Routes.SOUND_TEST) },
                onTip = { navController.navigate(Routes.tip(it)) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.TIP, arguments = listOf(navArgument("index") { type = NavType.IntType })) { entry ->
            TipDetailScreen(index = entry.arguments?.getInt("index") ?: 0, onBack = back)
        }

        // ---------- Water / Dust Remover ----------
        composable(Routes.CLEANER, arguments = listOf(navArgument("feature") { type = NavType.StringType })) {
            CleanerScreen(
                onBack = back,
                onStartAuto = { feature, level -> navController.navigate(Routes.cleaning(feature, level)) },
                onOpenGuide = { navController.navigate(Routes.tutorial(it.key)) },
            )
        }
        composable(
            Routes.CLEANING,
            arguments = listOf(
                navArgument("feature") { type = NavType.StringType },
                navArgument("level") { type = NavType.IntType },
            ),
        ) {
            CleaningScreen(onResult = { feature, level, percent ->
                navController.navigate(Routes.result(feature, level, percent)) {
                    popUpTo(Routes.CLEANING) { inclusive = true } // back từ Result không quay lại Cleaning
                }
            })
        }
        composable(
            Routes.RESULT,
            arguments = listOf(
                navArgument("feature") { type = NavType.StringType },
                navArgument("level") { type = NavType.IntType },
                navArgument("percent") { type = NavType.IntType },
            ),
        ) { entry ->
            val feature = CleanerFeature.fromKey(entry.arguments?.getString("feature"))
            val level = entry.arguments?.getInt("level") ?: 0
            ResultScreen(
                percent = entry.arguments?.getInt("percent") ?: 0,
                onHome = goHome,
                onCleanAgain = {
                    navController.navigate(Routes.cleaning(feature, level)) {
                        popUpTo(Routes.RESULT) { inclusive = true }
                    }
                },
                onTestSpeaker = {
                    navController.navigate(Routes.SOUND_TEST) {
                        popUpTo(Routes.RESULT) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.TUTORIAL, arguments = listOf(navArgument("feature") { type = NavType.StringType })) { entry ->
            TutorialScreen(featureKey = entry.arguments?.getString("feature") ?: "water", onBack = back)
        }

        // Sound test
        composable(Routes.SOUND_TEST) {
            SoundTestScreen(onBack = back, onOpenGuide = { navController.navigate(Routes.tutorial("sound")) })
        }

        // Setting
        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = back, onLanguage = { navController.navigate(Routes.LANGUAGE_SETTINGS) })
        }
        composable(Routes.LANGUAGE_SETTINGS) {
            LanguageScreen(onDone = back, onBack = back)
        }
    }
}
