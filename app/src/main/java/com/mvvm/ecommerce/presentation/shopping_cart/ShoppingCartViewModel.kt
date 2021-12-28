package com.mvvm.ecommerce.presentation.shopping_cart

import androidx.lifecycle.*
import com.mvvm.ecommerce.common.Constants.PARAM_PRODUCT
import com.mvvm.ecommerce.common.Resource
import com.mvvm.ecommerce.data.remote.dto.RequestDto
import com.mvvm.ecommerce.domain.model.Product
import com.mvvm.ecommerce.domain.use_cases.PostProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val postProductsUseCase: PostProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var product: Product

    init {
        savedStateHandle.get<Product>(PARAM_PRODUCT)?.let { product ->
            this.product = product
        }
    }

    private val _productsLiveData = MutableLiveData<Resource<Response<ResponseBody>>>()
    val productsLiveData: LiveData<Resource<Response<ResponseBody>>> get() = _productsLiveData

    fun postProducts(shoppingCartList: ArrayList<RequestDto>) = viewModelScope.launch {
        try {
            postProductsUseCase.postProducts(shoppingCartList).let {
                _productsLiveData.postValue(Resource.Success(it))
            }
        } catch (e: HttpException) {
            _productsLiveData.postValue(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            _productsLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}