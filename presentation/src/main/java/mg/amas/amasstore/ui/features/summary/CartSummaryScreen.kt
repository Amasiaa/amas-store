package mg.amas.amasstore.ui.features.summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.amas.amasstore.R
import mg.amas.amasstore.model.UserAddress
import mg.amas.amasstore.navigation.HomeScreen
import mg.amas.amasstore.navigation.UserAddressRoute
import mg.amas.amasstore.navigation.UserAddressRouteWrapper
import mg.amas.amasstore.ui.features.user_address.USER_ADDRESS_SCREEN
import mg.amas.amasstore.utils.CurrencyUtils
import mg.amas.domain.model.CartItemModel
import mg.amas.domain.model.CartSummary
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartSummaryScreen(
    navController: NavController,
    viewModel: CartSummaryViewModel = koinViewModel(),
) {
    val address = remember { mutableStateOf<UserAddress?>(null) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
        ) {
            Text(
                text = "Cart Summary",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        val uiState = viewModel.uiState.collectAsState()

        LaunchedEffect(navController) {
            val savedState = navController.currentBackStackEntry?.savedStateHandle
            savedState?.getStateFlow(USER_ADDRESS_SCREEN, address.value)?.collect { userAddress ->
                address.value = userAddress
            }
        }
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) {
            when (val event = uiState.value) {
                is CartSummaryEvent.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(60.dp))
                        Text(text = "Loading...", style = MaterialTheme.typography.titleMedium)
                    }
                }

                is CartSummaryEvent.Error -> {
                    Text(
                        text = event.message,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is CartSummaryEvent.Success -> {
                    Column {
                        AddressBar(address = address.value?.toString() ?: "") {
                            navController.navigate(UserAddressRoute(userAddressRouteWrapper = UserAddressRouteWrapper(address.value)))
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        SummaryScreenContent(event.summary)
                    }
                }

                is CartSummaryEvent.PlaceOrder -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_success),
                            contentDescription = null,
                        )
                        Text(
                            text = "Order Placed: ${event.orderId}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Button(
                            onClick = { navController.popBackStack(HomeScreen, inclusive = false) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(text = "Continue Shopping", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
        if (uiState.value !is CartSummaryEvent.PlaceOrder) {
            Button(
                onClick = { viewModel.placeOrder(address.value!!) },
                modifier = Modifier.fillMaxWidth(),
                enabled = address.value != null,
            ) {
                Text(text = "Checkout", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun SummaryScreenContent(cartSummary: CartSummary) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray.copy(alpha = 0.4f))
                .padding(8.dp),
    ) {
        item {
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                text = "Order Summary:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }

        items(cartSummary.summaryData.items) { cartItem ->
            ProductRow(cartItem)
        }
        item {
            Column {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp))
                AmountRow("Subtotal", cartSummary.summaryData.subtotal)
                AmountRow("Tax", cartSummary.summaryData.tax)
                AmountRow("Shipping", cartSummary.summaryData.shipping)
                AmountRow("Discount", cartSummary.summaryData.discount)
                AmountRow("Total", cartSummary.summaryData.total)
            }
        }
    }
}

@Composable
fun AmountRow(
    title: String,
    amount: Double,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style =
                if (title == "Total") {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600)
                } else {
                    MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                },
            color = if (title == "Total") MaterialTheme.colorScheme.primary else Color.Gray,
        )
        Text(
            text = CurrencyUtils.formatPrice(amount),
            style =
                MaterialTheme.typography.titleSmall.copy(
                    fontWeight = if (title == "Total") FontWeight.W600 else FontWeight.Normal,
                    fontSize = if (title == "Total") 16.sp else 14.sp,
                ),
            color = if (title == "Total") MaterialTheme.colorScheme.primary else Color.Gray,
        )
    }
}

@Composable
fun ProductRow(cartItemModel: CartItemModel) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = cartItemModel.productName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
        )
        Text(
            text = "${CurrencyUtils.formatPrice(cartItemModel.price)} x ${cartItemModel.quantity}",
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
        )
    }
}

@Composable
fun AddressBar(
    address: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .padding(8.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_address),
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray.copy(alpha = 0.4f)),
            contentScale = ContentScale.Inside,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                text = "Shipping Address",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
            )
            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}
