package mg.amas.domain.di

import mg.amas.domain.usecase.AddToCartUseCase
import mg.amas.domain.usecase.CartSummaryUseCase
import mg.amas.domain.usecase.DeleteProductUseCase
import mg.amas.domain.usecase.GetCartUseCase
import mg.amas.domain.usecase.GetCategoriesUseCase
import mg.amas.domain.usecase.GetProductUseCase
import mg.amas.domain.usecase.OrderListUseCase
import mg.amas.domain.usecase.PlaceOrderUseCase
import mg.amas.domain.usecase.UpdateQuantityUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        factory { GetProductUseCase(get()) }
        factory { GetCategoriesUseCase(get()) }
        factory { AddToCartUseCase(get()) }
        factory { GetCartUseCase(get()) }
        factory { UpdateQuantityUseCase(get()) }
        factory { DeleteProductUseCase(get()) }
        factory { CartSummaryUseCase(get()) }
        factory { PlaceOrderUseCase(get()) }
        factory { OrderListUseCase(get()) }
    }
