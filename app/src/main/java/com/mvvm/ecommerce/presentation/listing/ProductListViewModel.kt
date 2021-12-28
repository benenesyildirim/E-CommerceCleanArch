package com.mvvm.ecommerce.presentation.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.ecommerce.common.Resource
import com.mvvm.ecommerce.data.remote.dto.ProductDto
import com.mvvm.ecommerce.domain.use_cases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    init {
        getCharacters()
    }

    private val _productsLiveData = MutableLiveData<Resource<List<ProductDto>>>()
    val productsLiveData: LiveData<Resource<List<ProductDto>>> get() = _productsLiveData

    private fun getCharacters() = viewModelScope.launch {
        try {
            getProductsUseCase.getProducts().let {
                _productsLiveData.postValue(Resource.Loading())

                _productsLiveData.postValue(Resource.Success(it.body()!!))
            }
        } catch (e: HttpException) {
            _productsLiveData.postValue(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            _productsLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}