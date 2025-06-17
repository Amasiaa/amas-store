package mg.amas.data.repository

import mg.amas.domain.model.CategoriesListModel
import mg.amas.domain.network.NetworkService
import mg.amas.domain.network.ResultWrapper
import mg.amas.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    val networkService: NetworkService,
) : CategoryRepository {
    override suspend fun getCategories(): ResultWrapper<CategoriesListModel> = networkService.getCategories()
}
