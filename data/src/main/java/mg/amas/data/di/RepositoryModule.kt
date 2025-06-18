package mg.amas.data.di

import mg.amas.data.repository.CartRepositoryImpl
import mg.amas.data.repository.CategoryRepositoryImpl
import mg.amas.data.repository.OrderRepositoryImpl
import mg.amas.data.repository.ProductRepositoryImpl
import mg.amas.data.repository.UserRepositoryImpl
import mg.amas.domain.repository.CartRepository
import mg.amas.domain.repository.CategoryRepository
import mg.amas.domain.repository.OrderRepository
import mg.amas.domain.repository.ProductRepository
import mg.amas.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<ProductRepository> { ProductRepositoryImpl(get()) }
        single<CategoryRepository> { CategoryRepositoryImpl(get()) }
        single<CartRepository> { CartRepositoryImpl(get()) }
        single<OrderRepository> { OrderRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }
