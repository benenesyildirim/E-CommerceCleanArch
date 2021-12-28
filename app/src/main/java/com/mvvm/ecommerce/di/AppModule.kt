package com.mvvm.ecommerce.di

import com.mvvm.ecommerce.common.Constants.BASE_URL
import com.mvvm.ecommerce.data.remote.ItemApi
import com.mvvm.ecommerce.data.remote.repository.ProductRepositoryImpl
import com.mvvm.ecommerce.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): ItemApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(api: ItemApi): ProductRepository{
        return ProductRepositoryImpl(api)
    }
}