package com.haberturm.rickandmorty.presentation.locations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.LocationsItemBinding
import com.haberturm.rickandmorty.entities.LocationUi

class LocationListAdapter(
    private val listener: ActionClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {

    private val locations: ArrayList<LocationUi> = arrayListOf()

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LocationsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        holder.itemView.setOnClickListener {
            listener.showDetail(location.id)
        }
        holder.bind(location)
    }

    fun submitUpdate(update: List<LocationUi>) {
        val callback = BooksDiffCallback(locations, update)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)

        locations.clear()
        locations.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private var binding: LocationsItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationUi) {
            binding.locationName.text = location.name
            binding.locationAdditional.text =
                context.resources.getString(
                    R.string.location_additional_template,
                    location.type,
                    location.dimension
                )
            binding.executePendingBindings()
        }
    }

    class BooksDiffCallback(
        private val oldCharacters: List<LocationUi>,
        private val newCharacters: List<LocationUi>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldCharacters.size
        }

        override fun getNewListSize(): Int {
            return newCharacters.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCharacters[oldItemPosition].id == newCharacters[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCharacters[oldItemPosition].id == newCharacters[newItemPosition].id
        }
    }

    interface ActionClickListener {
        fun showDetail(id: Int)
    }
}