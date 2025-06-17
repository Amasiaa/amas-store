package mg.amas.amasstore.ui.features.user_address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mg.amas.amasstore.model.UserAddress

const val USER_ADDRESS_SCREEN = "user_address_screen"

@Composable
fun UserAddressScreen(
    navController: NavController,
    userAddress: UserAddress?,
) {
    var addressLine by remember { mutableStateOf(userAddress?.addressLine ?: "") }
    var city by remember { mutableStateOf(userAddress?.city ?: "") }
    var state by remember { mutableStateOf(userAddress?.state ?: "") }
    var postalCode by remember { mutableStateOf(userAddress?.postalCode ?: "") }
    var country by remember { mutableStateOf(userAddress?.country ?: "") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = addressLine,
            onValueChange = { addressLine = it },
            label = { Text(text = "Address Line") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text(text = "City") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text(text = "State") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = postalCode,
            onValueChange = { postalCode = it },
            label = { Text(text = "Postal Code") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text(text = "Country") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                val address =
                    UserAddress(
                        addressLine = addressLine,
                        city = city,
                        state = state,
                        postalCode = postalCode,
                        country = country,
                    )
                val previousBackStack = navController.previousBackStackEntry
                previousBackStack?.savedStateHandle?.set(USER_ADDRESS_SCREEN, address)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = addressLine.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && postalCode.isNotEmpty() && country.isNotEmpty(),
        ) {
            Text(text = "Save")
        }
    }
}
