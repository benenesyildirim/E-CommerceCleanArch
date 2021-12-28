package com.mvvm.ecommerce.presentation.shopping_cart

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.mvvm.ecommerce.R
import com.mvvm.ecommerce.common.Constants.EMPTY_CART_IMAGE
import com.mvvm.ecommerce.common.Constants.PARAM_PRODUCT
import com.mvvm.ecommerce.common.Constants.PARAM_PROD_LIST_SP
import com.mvvm.ecommerce.common.Constants.PARAM_SP_NAME
import com.mvvm.ecommerce.common.Constants.SHOP_404_IMAGE
import com.mvvm.ecommerce.common.Constants.SHOP_SUCCESS_IMAGE
import com.mvvm.ecommerce.common.Utils
import com.mvvm.ecommerce.data.remote.dto.RequestDto
import com.mvvm.ecommerce.databinding.FragmentShoppingCartBinding
import com.mvvm.ecommerce.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {
    private lateinit var soldOutItem: Product
    private var soldOutProductAvailable: Boolean = false
    private lateinit var productListAdapter: ShoppingCartListAdapter
    private var productAlreadyAdded: Boolean = false
    private lateinit var binding: FragmentShoppingCartBinding
    private val viewModel: ShoppingCartViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private var productList = mutableListOf<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sharedPreferences = activity!!.getSharedPreferences(PARAM_SP_NAME, Context.MODE_PRIVATE)

            val selectedProduct = arguments?.get(PARAM_PRODUCT) as Product

            var productListJson =
                sharedPreferences.getString(PARAM_PROD_LIST_SP, Gson().toJson(productList))

            convertProductListFromJson(productListJson)

            increaseProductAmount(selectedProduct)

            if (productList.isEmpty() || !productAlreadyAdded) productList.add(selectedProduct)

            productListJson = Gson().toJson(productList)

            saveProductListToSp(productListJson)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(layoutInflater)

        observeBuyingResponse()

        buyProductsListener()

        setCartListAdapter()

        return binding.root
    }

    private fun convertProductListFromJson(productListJson: String?) {
        try {
            productList =
                Gson().fromJson(productListJson, Array<Product>::class.java).toMutableList()
        } catch (e: IllegalStateException) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProductListToSp(productListJson: String?) {
        sharedPreferences.edit().apply() {
            putString(PARAM_PROD_LIST_SP, productListJson)
            apply()
        }
    }

    private fun setCartListAdapter() {
        productListAdapter =
            ShoppingCartListAdapter({
                // plus amount listener
                increaseProductAmount(it)
            }, {
                // minus amount listener
                decreaseProductAmount(it)
            }) {
                // delete product listener
                showBottomDialog(getString(R.string.product_will_be_deleted), it.image, it)
            }

        productListAdapter.setList(productList)

        binding.shoppingCartListRv.adapter = productListAdapter
    }

    private fun observeBuyingResponse() {
        with(viewModel) {
            productsLiveData.observe(viewLifecycleOwner, {
                if (it.data?.code() == 200) {

                    showBottomDialog(getString(R.string.thanks_for_buying), SHOP_SUCCESS_IMAGE)
                    productList = mutableListOf()
                    updateList()
                } else if (it.data?.code() == 404) {
                    showBottomDialog(
                        it.data.message() ?: "An unexpected error occurred",
                        SHOP_404_IMAGE
                    )
                }
            })
        }
    }

    private fun buyProductsListener() {
        binding.buyAllBtn.setOnClickListener {
            if (getShoppingCartList().size > 0) {
                if (soldOutProductAvailable) showBottomDialog(getString(R.string.sold_out), soldOutItem.image)
                else viewModel.postProducts(getShoppingCartList())
            } else {
                if (soldOutProductAvailable) showBottomDialog(getString(R.string.sold_out), soldOutItem.image)
                else showBottomDialog(getString(R.string.empty_list), EMPTY_CART_IMAGE)
            }
        }
    }

    private fun showBottomDialog(title: String, imageUrl: String?, product: Product? = null) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_design, null)
        val positiveBtn = view.findViewById<Button>(R.id.bottom_sheet_positive_btn)
        val negativeBtn = view.findViewById<Button>(R.id.bottom_sheet_negative_btn)
        val textView = view.findViewById<TextView>(R.id.bottom_sheet_tv)
        val imageView = view.findViewById<ImageView>(R.id.bottom_sheet_image)

        textView.text = title
        Utils.loadImage(imageView, imageUrl ?: product?.image)

        positiveBtn.setOnClickListener {
            when (title) {
                getString(R.string.product_will_be_deleted) -> {
                    productList.remove(product)
                    updateList()
                }
                getString(R.string.thanks_for_buying) -> {
                    Navigation.findNavController(binding.root).popBackStack()
                }
                getString(R.string.sold_out) -> {
                    if (getShoppingCartList().size > 0){
                        viewModel.postProducts(getShoppingCartList())
                    } else {
                        productList = mutableListOf()
                        updateList()
                        Navigation.findNavController(binding.root).popBackStack()
                    }
                }
            }
            dialog.dismiss()
        }

        negativeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)

        dialog.setContentView(view)
        dialog.show()
    }

    private fun decreaseProductAmount(product: Product) {
        productList.map {
            if (it.id == product.id) {
                productAlreadyAdded = true
                if (it.amount == 1) {
                    showBottomDialog(getString(R.string.product_will_be_deleted), it.image, it)
                } else {
                    it.amount--
                }
            }
        }

        updateList()
    }

    private fun updateList() {
        saveProductListToSp(Gson().toJson(productList))

        productListAdapter.setList(productList)
    }

    private fun increaseProductAmount(product: Product) {
        productList.map {
            if (it.id == product.id) {
                productAlreadyAdded = true
                it.amount++
            }
        }

        saveProductListToSp(Gson().toJson(productList))

        if (this::productListAdapter.isInitialized)
            productListAdapter.setList(productList)
    }

    private fun getShoppingCartList(): ArrayList<RequestDto> {
        val requestList: ArrayList<RequestDto> =
            mutableListOf<RequestDto>() as ArrayList<RequestDto>
        for (product in productList) {
            val request = RequestDto(product.id, product.amount)
            if (request.id == 3) {
                soldOutProductAvailable = true
                soldOutItem = product
            } else {
                requestList.add(request)
            }
        }
        return requestList
    }
}