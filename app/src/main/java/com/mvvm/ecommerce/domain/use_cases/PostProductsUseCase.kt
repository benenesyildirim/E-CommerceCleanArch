package com.mvvm.ecommerce.domain.use_cases

import com.mvvm.ecommerce.data.remote.dto.RequestDto
import com.mvvm.ecommerce.domain.repository.ProductRepository
import java.util.ArrayList
import javax.inject.Inject

class PostProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend fun postProducts(requestDto: ArrayList<RequestDto>) = repository.postProducts(requestDto)
}