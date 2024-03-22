package com.crypto.ticker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crypto.ticker.R
import com.crypto.ticker.data.local.database.CoinsListEntity
import com.crypto.ticker.databinding.ItemCoinsListBinding
import com.crypto.ticker.util.ImageLoader
import com.crypto.ticker.util.UIHelper
import com.crypto.ticker.util.extensions.dollarString
import com.crypto.ticker.util.extensions.emptyIfNull

//listener for add to favourite and item click
interface OnItemClickCallback {
    fun onItemClick(symbol: String, id: String)
    fun onFavouriteClicked(symbol: String)
}

class CoinsListAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<CoinsListAdapter.CoinsListViewHolder>() {

    private val coinsList: ArrayList<CoinsListEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        val binding =
            ItemCoinsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        holder.bind(coinsList[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = coinsList.size

    fun updateList(list: List<CoinsListEntity>) {
        this.coinsList.clear()
        this.coinsList.addAll(list)
        notifyDataSetChanged()
    }

    class CoinsListViewHolder(private val binding: ItemCoinsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // bind the data with the list view item
        fun bind(model: CoinsListEntity, onItemClickCallback: OnItemClickCallback) {
            binding.coinsItemSymbolTextView.text = model.symbol
            binding.coinsItemNameTextView.text = model.name.emptyIfNull()
            binding.coinsItemPriceTextView.text = model.price.dollarString()

            UIHelper.showChangePercent(binding.coinsItemChangeTextView, model.changePercent)

            binding.favouriteImageView.setImageResource(
                if (model.isFavourite) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            )

            binding.favouriteImageView.setOnClickListener {
                onItemClickCallback.onFavouriteClicked(model.symbol)
            }

            ImageLoader.loadImage(binding.coinsItemImageView, model.image ?: "")

            binding.root.setOnClickListener {
                onItemClickCallback.onItemClick(
                    model.symbol,
                    model.id ?: model.symbol
                )
            }
        }
    }
}