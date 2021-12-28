package com.mvvm.ecommerce

import com.google.gson.GsonBuilder
import com.mvvm.ecommerce.retrofit.Constants.LISTING_SUCCESS_JSON
import com.mvvm.ecommerce.data.remote.ItemApi
import com.mvvm.ecommerce.data.remote.dto.ProductDto
import com.mvvm.ecommerce.data.remote.repository.ProductRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class ProductsNetworkDataSourceTest {
    private val mockWebServer = MockWebServer()

    private val builder = OkHttpClient.Builder()
        .connectTimeout(1,TimeUnit.SECONDS)
        .readTimeout(1,TimeUnit.SECONDS)
        .writeTimeout(1,TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
    .setLenient().create()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(builder)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ItemApi::class.java)

    private val productsRepository = ProductRepositoryImpl(api)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchSuccessTest() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(LISTING_SUCCESS_JSON))

        runBlocking {
            val actualSuccessList = productsRepository.getProducts()

            val expectedSuccessList = listOf(
                ProductDto(
                    1,
                    "Şampuan",
                    "13",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fsampuan.jpeg?v=1561027551321"
                ),ProductDto(
                    2,
                    "Deodorant",
                    "26",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fdeodorant.jpeg?v=1561027551696"
                ),ProductDto(
                    3,
                    "Diş Fırçası",
                    "19.95",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fdis-fircasi.jpeg?v=1561027551798"
                ),ProductDto(
                    4,
                    "Pahalı Şampuan",
                    "135",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fsampuan.jpeg?v=1561027551321"
                ),ProductDto(
                    5,
                    "Lüks Deodorant",
                    "260",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fdeodorant.jpeg?v=1561027551696"
                ),ProductDto(
                    6,
                    "Elit Diş Fırçası",
                    "190.95",
                    "TRY",
                    "https://cdn.glitch.com/a28552e7-44e1-4bbd-b298-5745e70c2209%2Fdis-fircasi.jpeg?v=1561027551798"
                )
            )

            assertEquals(expectedSuccessList, actualSuccessList.body())
        }
    }
}