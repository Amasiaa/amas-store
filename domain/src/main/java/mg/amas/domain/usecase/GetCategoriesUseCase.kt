package mg.amas.domain.usecase

import mg.amas.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository,
) {
    suspend fun execute() = repository.getCategories()
}
