package com.mvvm.ecommerce.presentation.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.mvvm.ecommerce.R
import com.mvvm.ecommerce.common.Constants.PARAM_PRODUCT
import com.mvvm.ecommerce.common.Resource
import com.mvvm.ecommerce.databinding.FragmentListBinding
import com.mvvm.ecommerce.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater)

        observeCharacters(binding.root)

        return binding.root
    }

    private fun observeCharacters(view: View) {
        with(viewModel) {
            productsLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.cartLoadingGif.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        val productList = mutableListOf<Product>()
                        for (item in state.data!!) {
                            productList.add(
                                Product(
                                    item.id,
                                    item.name,
                                    item.price,
                                    item.currency,
                                    item.image
                                )
                            )
                        }

                        binding.shoppingCartListRv.apply {
                            adapter =
                                ProductListAdapter(productList) {
                                    val bundle = bundleOf(PARAM_PRODUCT to it)
                                    Navigation.findNavController(view)
                                        .navigate(
                                            R.id.action_listFragment_to_shoppingCartFragment,
                                            bundle
                                        )
                                }
                        }
                        binding.cartLoadingGif.visibility = GONE
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            context,
                            state.message ?: "An error occurred.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.cartLoadingGif.visibility = GONE
                    }
                }
            })
        }
    }
}