package mg.amas.amasstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import mg.amas.amasstore.model.UIProductModel
import mg.amas.amasstore.navigation.CartScreen
import mg.amas.amasstore.navigation.CartSummaryScreen
import mg.amas.amasstore.navigation.HomeScreen
import mg.amas.amasstore.navigation.LoginScreen
import mg.amas.amasstore.navigation.OrdersScreen
import mg.amas.amasstore.navigation.ProductDetails
import mg.amas.amasstore.navigation.ProfileScreen
import mg.amas.amasstore.navigation.RegisterScreen
import mg.amas.amasstore.navigation.SearchScreen
import mg.amas.amasstore.navigation.UserAddressRoute
import mg.amas.amasstore.navigation.UserAddressRouteWrapper
import mg.amas.amasstore.navigation.productNavType
import mg.amas.amasstore.navigation.userAddressNavType
import mg.amas.amasstore.ui.features.home.HomeScreen
import mg.amas.amasstore.ui.features.product_details.ProductDetailsScreen
import mg.amas.amasstore.ui.features.user_address.UserAddressScreen
import mg.amas.amasstore.ui.theme.AmasStoreTheme
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmasStoreTheme {
                val shouldShowBottomNav = remember { mutableStateOf(true) }
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value, enter = fadeIn()) {
                            BottomNavigationBar(navController)
                        }
                    },
                ) {
                    Surface(modifier = Modifier.fillMaxSize().padding(it)) {
                        NavHost(
                            navController = navController,
                            startDestination = if (AmasStoreSession.getUser() != null) HomeScreen else LoginScreen,
                        ) {
                            composable<HomeScreen> {
                                HomeScreen(navController = navController)
                                shouldShowBottomNav.value = true
                            }
                            composable<SearchScreen> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(text = "Search Page")
                                }
                            }
                            composable<CartSummaryScreen> {
                                shouldShowBottomNav.value = false
                                mg.amas.amasstore.ui.features.summary
                                    .CartSummaryScreen(navController)
                            }
                            composable<CartScreen> {
                                shouldShowBottomNav.value = true
                                mg.amas.amasstore.ui.features.cart
                                    .CartScreen(navController)
                            }
                            composable<OrdersScreen> {
                                shouldShowBottomNav.value = true
                                mg.amas.amasstore.ui.features.orders
                                    .OrdersScreen(navController = navController)
                            }
                            composable<LoginScreen> {
                                shouldShowBottomNav.value = false
                                mg.amas.amasstore.ui.features.auth.login
                                    .LoginScreen(navController = navController)
                            }
                            composable<RegisterScreen> {
                                shouldShowBottomNav.value = false
                                mg.amas.amasstore.ui.features.auth.register
                                    .RegisterScreen(navController = navController)
                            }
                            composable<ProfileScreen> {
                                shouldShowBottomNav.value = true
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(text = "Profile Page")
                                }
                            }

                            composable<ProductDetails>(typeMap = mapOf(typeOf<UIProductModel>() to productNavType)) {
                                shouldShowBottomNav.value = false
                                val productRoute = it.toRoute<ProductDetails>()
                                ProductDetailsScreen(
                                    navController = navController,
                                    productRoute.product,
                                )
                            }

                            composable<UserAddressRoute>(typeMap = mapOf(typeOf<UserAddressRouteWrapper>() to userAddressNavType)) {
                                shouldShowBottomNav.value = false
                                val userAddressRoute = it.toRoute<UserAddressRoute>()
                                UserAddressScreen(
                                    navController,
                                    userAddressRoute.userAddressRouteWrapper.userAddress,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val currentRoute =
            navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route
        val items =
            listOf(
                BottomNavItems.Home,
                BottomNavItems.Orders,
                BottomNavItems.Profile,
            )

        items.forEach { item ->
            val isSelected = currentRoute?.substringBefore("?") == item.route::class.qualifiedName
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { startRoute ->
                            popUpTo(startRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(text = item.title, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                },
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.title) },
                colors =
                    NavigationBarItemDefaults.colors().copy(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                    ),
            )
        }
    }
}

sealed class BottomNavItems(
    val route: Any,
    val title: String,
    val icon: Int,
) {
    object Home : BottomNavItems(HomeScreen, "Home", R.drawable.ic_home)

    // object Search : BottomNavItems(Screen.Search.route, "Search", R.drawable.ic_search)

    // object Cart : BottomNavItems(CartScreen, "Cart", R.drawable.ic_cart)

    object Orders : BottomNavItems(OrdersScreen, "Orders", R.drawable.ic_orders)

    object Profile : BottomNavItems(ProfileScreen, "Profile", R.drawable.ic_profile_bn)
}
