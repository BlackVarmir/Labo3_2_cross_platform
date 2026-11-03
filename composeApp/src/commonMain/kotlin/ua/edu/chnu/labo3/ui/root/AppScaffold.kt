package ua.edu.chnu.labo3.ui.root

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ua.edu.chnu.labo3.ui.theme.labo3Theme

@Composable
fun AppScaffold() {
    labo3Theme {
        val navController = rememberNavController()

        Scaffold(contentWindowInsets = WindowInsets.ime) {
            AppNavHost(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}
