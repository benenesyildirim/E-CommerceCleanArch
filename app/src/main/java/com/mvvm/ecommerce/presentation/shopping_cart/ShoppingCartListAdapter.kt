package com.mvvm.ecommerce.presentation.shopping_cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.ecommerce.databinding.ShoppingCartItemDesignBinding
import com.mvvm.ecommerce.domain.model.Product

class ShoppingCartListAdapter(
    val plusListener: (Product) -> Unit,
    val minusListener: (Product) -> Unit,
    val deleteListener: (Product) -> Unit
) : RecyclerView.Adapter<ShoppingCartListAdapter.ViewHolder>() {
    var productList: List<Product> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShoppingCartItemDesignBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(productList[position])

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(private val binding: ShoppingCartItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                this.product = product
                executePendingBindings()
                binding.plusIv.setOnClickListener { plusListener(product) }
                binding.minusIv.setOnClickListener { minusListener(product) }
                binding.cartItemDeleteIv.setOnClickListener { deleteListener(product) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(products: List<Product>){
        productList = products
        notifyDataSetChanged()
    }
}