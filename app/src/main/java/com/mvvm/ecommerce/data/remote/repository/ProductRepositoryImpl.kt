package com.mvvm.ecommerce.data.remote.repository

import com.mvvm.ecommerce.data.remote.ItemApi
import com.mvvm.ecommerce.data.remote.dto.ProductDto
import com.mvvm.ecommerce.data.remote.dto.RequestDto
import com.mvvm.ecommerce.domain.repository.ProductRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ItemApi
) : ProductRepository {
    override suspend fun getProducts(): Response<List<ProductDto>> = api.getProducts()

    override suspend fun postProducts(requestDto: ArrayList<RequestDto>): Response<ResponseBody> = api.postProducts(requestDto)
}