package mg.amas.amasstore.ui.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import mg.amas.amasstore.R
import mg.amas.amasstore.model.UIProductModel
import mg.amas.amasstore.navigation.CartScreen
import mg.amas.amasstore.navigation.ProductDetails
import mg.amas.domain.model.Product
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value
    val loading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }
    val featured =
        remember {
            mutableStateOf<List<Product>>(emptyList())
        }
    val popular =
        remember {
            mutableStateOf<List<Product>>(emptyList())
        }
    val categories =
        remember {
            mutableStateOf<List<String>>(emptyList())
        }

    Scaffold {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
        ) {
            when (uiState) {
                is HomeScreenUIEvents.Loading -> {
                    loading.value = true
                    error.value = null
                }
                is HomeScreenUIEvents.Success -> {
                    val data = uiState
                    featured.value = data.featured
                    popular.value = data.popularProducts
                    categories.value = data.categories
                    loading.value = false
                    error.value = null
                }
                is HomeScreenUIEvents.Error -> {
                    val message = uiState.message
                    // Text(text = message, color = Color.Red, fontSize = 18.sp)
                    error.value = message
                    loading.value = false
                }
            }
            HomeContent(
                featured.value,
                popular.value,
                categories.value,
                loading.value,
                error.value,
                onTap = {
                    navController.navigate(ProductDetails(UIProductModel.fromProduct(it)))
                },
                onCartClicked = { navController.navigate(CartScreen) },
            )
        }
    }
}

@Composable
fun ProfileHeader(onCartClicked: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        Row(
            modifier =
                Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile_1),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Hello", style = MaterialTheme.typography.bodySmall)
                Text(text = "John Doe", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
        }
        Row(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.notificatino),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(8.dp),
                contentScale = ContentScale.Inside,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_cart),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(8.dp)
                        .clickable { onCartClicked() },
                contentScale = ContentScale.Inside,
            )
        }
    }
}

@Composable
fun HomeContent(
    featured: List<Product>,
    popularProducts: List<Product>,
    categories: List<String>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onTap: (Product) -> Unit,
    onCartClicked: () -> Unit,
) {
    LazyColumn {
        item {
            ProfileHeader(onCartClicked = onCartClicked)
            Spacer(modifier = Modifier.size(16.dp))
            SearchBar(value = "", onTextChanged = {})
            Spacer(modifier = Modifier.size(16.dp))
        }
        item {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp))
                    Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
                }
            }

            errorMessage?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
            }
            if (categories.isNotEmpty()) {
                LazyRow {
                    items(categories, key = { it }) {
                        val isVisible = remember { mutableStateOf(false) }
                        LaunchedEffect(true) {
                            isVisible.value = true
                        }
                        AnimatedVisibility(visible = isVisible.value, enter = fadeIn() + expandVertically()) {
                            Text(
                                text = it.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier =
                                    Modifier
                                        .padding(horizontal = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(8.dp),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (featured.isNotEmpty()) {
                HomeProductRow(products = featured, title = "Featured", onTap = onTap)
                Spacer(modifier = Modifier.size(16.dp))
            }
            if (popularProducts.isNotEmpty()) {
                HomeProductRow(products = popularProducts, title = "Popular Products", onTap = onTap)
            }
        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onTextChanged: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onTextChanged,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            ),
        placeholder = {
            Text(text = "Search for products")
        },
    )
}

@Composable
fun HomeProductRow(
    products: List<Product>,
    title: String,
    onTap: (Product) -> Unit,
) {
    Column {
        Box(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier =
                    Modifier.align(Alignment.CenterStart),
            )
            Text(
                text = "View all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier =
                    Modifier.align(Alignment.CenterEnd),
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        LazyRow {
            items(products, key = { it.id }) { product ->
                val isVisible = remember { mutableStateOf(false) }
                LaunchedEffect(true) {
                    isVisible.value = true
                }
                AnimatedVisibility(visible = isVisible.value, enter = fadeIn() + expandVertically()) {
                    ProductItem(product = product, onTap = onTap)
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onTap: (Product) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(horizontal = 8.dp)
                .size(width = 126.dp, height = 144.dp)
                .clickable { onTap(product) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(96.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W600,
            )
        }
    }
}
