package mg.amas.domain.usecase

import mg.amas.domain.repository.ProductRepository

class GetProductUseCase(
    private val repository: ProductRepository,
) {
    suspend fun execute(category: Int?) = repository.getProducts(category)
}
