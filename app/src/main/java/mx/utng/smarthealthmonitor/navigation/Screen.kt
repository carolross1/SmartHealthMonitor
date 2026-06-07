package mx.utng.smarthealthmonitor.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Historial : Screen("historial")
}
