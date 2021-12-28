package com.mvvm.ecommerce.domain.repository

import com.mvvm.ecommerce.data.remote.dto.ProductDto
import com.mvvm.ecommerce.data.remote.dto.RequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.ArrayList

interface ProductRepository {
     suspend fun getProducts(): Response<List<ProductDto>>

    suspend fun postProducts(requestDto: ArrayList<RequestDto>): Response<ResponseBody>
}