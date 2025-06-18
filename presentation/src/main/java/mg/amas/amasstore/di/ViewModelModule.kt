package mg.amas.amasstore.di

import mg.amas.amasstore.ui.features.auth.login.LoginViewModel
import mg.amas.amasstore.ui.features.auth.register.RegisterViewModel
import mg.amas.amasstore.ui.features.cart.CartViewModel
import mg.amas.amasstore.ui.features.home.HomeViewModel
import mg.amas.amasstore.ui.features.orders.OrdersViewModel
import mg.amas.amasstore.ui.features.product_details.ProductDetailsViewModel
import mg.amas.amasstore.ui.features.summary.CartSummaryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { HomeViewModel(get(), get()) }
        viewModel { ProductDetailsViewModel(get()) }
        viewModel { CartSummaryViewModel(get(), get()) }
        viewModel { CartViewModel(get(), get(), get()) }
        viewModel { OrdersViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { RegisterViewModel(get()) }
    }
