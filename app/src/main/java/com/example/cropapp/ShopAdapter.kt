package com.example.cropapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val shops: List<Shop>,
    private val onItemClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.bind(shop)
        holder.itemView.setOnClickListener { onItemClick(shop) }
    }

    override fun getItemCount(): Int = shops.size

    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvShopName: TextView = itemView.findViewById(R.id.tvShopName)
        private val tvShopAddress: TextView = itemView.findViewById(R.id.tvShopAddress)

        fun bind(shop: Shop) {
            tvShopName.text = shop.name
            tvShopAddress.text = shop.address
        }
    }
}