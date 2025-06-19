package mg.amas.amasstore.ui.features.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import mg.amas.amasstore.R
import mg.amas.amasstore.navigation.CartSummaryScreen
import mg.amas.domain.model.CartItemModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiEvent.collectAsState()
    val cartItems = remember { mutableStateOf(emptyList<CartItemModel>()) }
    val isLoading =
        remember {
            mutableStateOf(false)
        }
    val errorMsg =
        remember {
            mutableStateOf<String?>(null)
        }

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is CartEvent.Loading -> {
                isLoading.value = true
                errorMsg.value = null
            }
            is CartEvent.Success -> {
                isLoading.value = false

                val data = (uiState.value as CartEvent.Success).cartItems
                if (data.isEmpty()) {
                    // Show empty cart
                    errorMsg.value = "No items in cart"
                } else {
                    errorMsg.value = null
                    cartItems.value = data
                }
            }
            is CartEvent.Error -> {
                isLoading.value = false
                errorMsg.value = (uiState.value as CartEvent.Error).message
            }
        }
    }

    if (isLoading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp))
            Text(
                text = "Loading cart...",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

    if (errorMsg.value != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (errorMsg.value ==
                "No items in cart"
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(Color.Red.copy(alpha = 0.1f))
                            .padding(30.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.Red.copy(alpha = 0.4f)),
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "No items in Cart", style = MaterialTheme.typography.titleSmall)
            } else {
                Text(
                    text = errorMsg.value ?: "Something went wrong",
                    color = Color.Red,
                )
            }
        }
    }

    val isRefreshing = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        val pullToRefreshState = rememberPullToRefreshState()
        if (isRefreshing.value) {
            LaunchedEffect(isRefreshing.value) {
                viewModel.getCart()
                delay(500)
                isRefreshing.value = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            PullToRefreshBox(
                isRefreshing = isRefreshing.value,
                state = pullToRefreshState,
                onRefresh = {
                    isRefreshing.value = true
                },
                modifier =
                    Modifier.align(Alignment.TopCenter),
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = isRefreshing.value,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        state = pullToRefreshState,
                    )
                },
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = "Cart", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.size(8.dp))
                    val shouldShowList = !isLoading.value && errorMsg.value == null
                    AnimatedVisibility(visible = shouldShowList, enter = fadeIn(), modifier = Modifier.weight(1f)) {
                        LazyColumn {
                            items(cartItems.value) { item ->
                                CartItem(
                                    item,
                                    onRemove = { viewModel.removeItem(it) },
                                    onIncrement = { viewModel.incrementQuantity(it) },
                                    onDecrement = { viewModel.decrementQuantity(it) },
                                )
                            }
                        }
                    }
                    if (shouldShowList) {
                        Button(onClick = { navController.navigate(CartSummaryScreen) }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Checkout")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(
    item: CartItemModel,
    onIncrement: (CartItemModel) -> Unit,
    onDecrement: (CartItemModel) -> Unit,
    onRemove: (CartItemModel) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.4f)),
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(126.dp, 96.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = item.productName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            IconButton(onClick = { onRemove(item) }) {
                Image(painter = painterResource(id = R.drawable.ic_delete), contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onDecrement(item) }) {
                    Image(painter = painterResource(id = R.drawable.ic_subtract), contentDescription = null)
                }
                Text(text = item.quantity.toString())
                IconButton(onClick = { onIncrement(item) }) {
                    Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
                }
            }
        }
    }
}
