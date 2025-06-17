package mg.amas.domain.repository

import mg.amas.domain.model.CategoriesListModel
import mg.amas.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories(): ResultWrapper<CategoriesListModel>
}
