package mg.amas.amasstore

sealed class Screen(
    val route: String,
) {
    object Home : Screen("home")

    object Cart : Screen("cart")

    object Profile : Screen("profile")

    object Search : Screen("search")
}
