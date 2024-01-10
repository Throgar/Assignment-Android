package com.goodrequest.hiring.ui

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.goodrequest.hiring.R
import com.goodrequest.hiring.databinding.ItemBinding
import com.goodrequest.hiring.databinding.ItemLoadingBinding

class PokemonAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Any>()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
                Item(view)
            }
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                LoadingItem(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {
            is Item -> showItems(holder, position)
            is LoadingItem -> showLoading(holder, position)
            else -> throw IllegalArgumentException("Invalid view type")
        }


    override fun getItemCount(): Int =
        items.size

    override fun getItemViewType(position: Int): Int =
        if (position >= items.size-1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    private fun showItems(viewHolder: Item, position: Int) {
        viewHolder.show(items[position] as Pokemon)
    }

    private fun showLoading(viewHolder: LoadingItem, position: Int) {
        viewHolder.show()
    }

    /**
     * @param pokemons includes one hidden LoadingItem item
      */
    fun show(pokemons: List<Any>) {
        items.clear()
        items.addAll(pokemons)
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<Any> {
        return items
    }
}

class Item(view: View): RecyclerView.ViewHolder(view) {
    private val ui = ItemBinding.bind(view)

    fun show(pokemon: Pokemon) {
        ui.image.load(pokemon.detail?.image) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        ui.name.text = pokemon.name
        ui.move.text = pokemon.detail?.move
        ui.weight.text = pokemon.detail?.weight.toString()
    }
}

class LoadingItem(view: View): RecyclerView.ViewHolder(view) {
    private val ui = ItemLoadingBinding.bind(view)

    fun show() {
        ui.loadMoreBar.visibility = VISIBLE
        ui.loadMoreBtn.visibility = GONE
    }
}