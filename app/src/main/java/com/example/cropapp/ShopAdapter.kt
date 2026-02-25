package com.example.cropapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val shops: List<Shop>,
    private val onShopClick: (Shop) -> Unit,
    private val onShareClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shops[position]
        holder.bind(shop, onShopClick, onShareClick)
    }

    override fun getItemCount(): Int = shops.size

    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvShopName: TextView = itemView.findViewById(R.id.tvShopName)
        private val tvShopAddress: TextView = itemView.findViewById(R.id.tvShopAddress)
        private val btnShareSms: ImageButton = itemView.findViewById(R.id.btnShareSms)
        private val shopInfoLayout: LinearLayout = itemView.findViewById(R.id.shop_info_layout)

        fun bind(shop: Shop, onShopClick: (Shop) -> Unit, onShareClick: (Shop) -> Unit) {
            tvShopName.text = shop.name
            tvShopAddress.text = shop.address

            shopInfoLayout.setOnClickListener { onShopClick(shop) }
            btnShareSms.setOnClickListener { onShareClick(shop) }
        }
    }
}