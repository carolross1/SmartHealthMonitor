package mx.utng.smarthealthmonitor.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.utng.smarthealthmonitor.ui.screens.DashboardScreen
import mx.utng.smarthealthmonitor.ui.screens.HistorialScreen
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    // Shared ViewModel instance across screens
    val viewModel: DashboardViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onVerHistorial = { navController.navigate(Screen.Historial.route) },
                viewModel = viewModel
            )
        }
        composable(Screen.Historial.route) {
            HistorialScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
