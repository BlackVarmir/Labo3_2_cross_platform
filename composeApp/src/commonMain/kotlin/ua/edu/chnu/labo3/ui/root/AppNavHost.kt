package ua.edu.chnu.labo3.ui.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.edu.chnu.labo3.ui.about.AboutPage
import ua.edu.chnu.labo3.ui.deletetext.DeleteTextPage
import ua.edu.chnu.labo3.ui.gettext.GetTextPage
import ua.edu.chnu.labo3.ui.home.HomePage
import ua.edu.chnu.labo3.ui.network.NetworkPage
import ua.edu.chnu.labo3.ui.posttext.PostTextPage
import ua.edu.chnu.labo3.ui.puttext.PutTextPage
import ua.edu.chnu.labo3.ui.reminders.RemindersPage

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomePage(
                onRemindersButtonClick = { navController.navigate(Screen.Reminders.route) },
                onNetworkButtonClick = { navController.navigate(Screen.Network.route) },
                onGetTextButtonClick = { navController.navigate(Screen.GetText.route) },
                onPostTextButtonClick = { navController.navigate(Screen.PostText.route) },
                onPutTextButtonClick = { navController.navigate(Screen.PutText.route) },
                onDeleteTextButtonClick = { navController.navigate(Screen.DeleteText.route) },
                onAboutButtonClick = { navController.navigate(Screen.AboutDevice.route) },
            )
        }

        composable(Screen.Reminders.route) {
            RemindersPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Network.route) {
            NetworkPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.GetText.route) {
            GetTextPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PostText.route) {
            PostTextPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.PutText.route) {
            PutTextPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.DeleteText.route) {
            DeleteTextPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AboutDevice.route) {
            AboutPage(
                onUpButtonClick = { navController.popBackStack() }
            )
        }
    }
}
