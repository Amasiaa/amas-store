package mg.amas.amasstore.ui.features.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mg.amas.domain.model.OrdersData
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrdersViewModel = koinViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Orders",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
        val uiState = viewModel.uiState.collectAsState()
        // tab Row
        val tabs = listOf("All", "Pending", "Delivered", "Cancelled")
        val selectedTab = remember { mutableIntStateOf(0) }
        TabRow(selectedTabIndex = selectedTab.intValue) {
            tabs.forEachIndexed { index, title ->
                Box(modifier = Modifier.clickable { selectedTab.intValue = index }) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(8.dp).align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        when (val event = uiState.value) {
            is OrdersEvent.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.titleMedium)
                }
            }
            is OrdersEvent.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = event.message,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Red,
                    )
                }
            }
            is OrdersEvent.Success -> {
                val orders = event.orders
                when (selectedTab.intValue) {
                    0 -> {
                        OrderList(orders = orders)
                    }
                    1 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Pending"))
                    }
                    2 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Delivered"))
                    }
                    3 -> {
                        OrderList(orders = viewModel.filterOrders(orders, "Cancelled"))
                    }
                }
            }
        }
    }
}

@Composable
fun OrderList(orders: List<OrdersData>) {
    if (orders.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "No Orders")
        }
    } else {
        LazyColumn {
            items(orders, key = { order -> order.id }) {
                OrderItem(order = it)
            }
        }
    }
}

@Composable
fun OrderItem(order: OrdersData) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray.copy(alpha = 0.4f))
                .padding(8.dp),
    ) {
        Text(text = "Order Id: ${order.id}")
        Text(text = "Order Date: ${order.orderDate}")
        Text(text = "Total Amount: ${order.totalAmount}")
        Text(text = "Status: ${order.status}")
    }
}
