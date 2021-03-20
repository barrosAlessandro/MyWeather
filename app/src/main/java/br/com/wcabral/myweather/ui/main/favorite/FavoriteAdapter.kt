package br.com.wcabral.myweather.ui.main.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.wcabral.myweather.data.local.model.Favorite
import br.com.wcabral.myweather.databinding.ItemFavoriteBinding

class FavoriteAdapter : ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(SearchDiff()) {

    private var onClick: OnClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: Favorite) {
            binding.tvFavTitle.text = favorite.cityName
            binding.btnFavAdapter.setOnClickListener {
                onClick?.onClick(favorite)
            }
        }

    }

    fun setOnClickListener(onclick: OnClick) {
        this.onClick = onclick
    }

    interface OnClick {
        fun onClick(
            favorite: Favorite?
        )
    }

    class SearchDiff : DiffUtil.ItemCallback<Favorite>() {

        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite) = oldItem == newItem

    }
}