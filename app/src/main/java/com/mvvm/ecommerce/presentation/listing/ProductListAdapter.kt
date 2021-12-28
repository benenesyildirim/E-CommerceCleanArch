package com.mvvm.ecommerce.presentation.listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.ecommerce.databinding.ShoppingItemDesignBinding
import com.mvvm.ecommerce.domain.model.Product

class ProductListAdapter(
    private val products: List<Product>, val listener: (Product) -> Unit
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoppingItemDesignBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(products[position])

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(private val binding: ShoppingItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                this.product = product
                executePendingBindings()
                binding.addToCartBtn.setOnClickListener { listener(product) }
            }
        }
    }
}