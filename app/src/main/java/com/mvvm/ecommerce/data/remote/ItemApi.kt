package com.mvvm.ecommerce.data.remote

import com.mvvm.ecommerce.data.remote.dto.ProductDto
import com.mvvm.ecommerce.data.remote.dto.RequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.ArrayList

interface ItemApi {

    @GET("/listing")
    suspend fun getProducts(): Response<List<ProductDto>>

    @POST("/order")
    suspend fun postProducts(@Body body: ArrayList<RequestDto>): Response<ResponseBody>
}