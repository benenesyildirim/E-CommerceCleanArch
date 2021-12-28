package com.mvvm.ecommerce.domain.use_cases

import com.mvvm.ecommerce.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun getProducts() = repository.getProducts()
}