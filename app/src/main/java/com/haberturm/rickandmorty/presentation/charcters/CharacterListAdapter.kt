package com.haberturm.rickandmorty.presentation.charcters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.CharactersItemBinding
import com.haberturm.rickandmorty.presentation.entities.CharacterUi
import com.haberturm.rickandmorty.util.loadImage

class CharacterListAdapter(
    private val listener: ActionClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    private val characters: ArrayList<CharacterUi> = arrayListOf()

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            CharactersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]
        holder.itemView.setOnClickListener {
            listener.showDetail(character.id)
        }
        holder.bind(character)
    }

    fun submitUpdate(update: List<CharacterUi>) {
        val callback = BooksDiffCallback(characters, update)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)

        characters.clear()
        characters.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private var binding: CharactersItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterUi) {
            binding.characterName.text = character.name
            binding.characterImage.loadImage(character.image)
            binding.characterAdditional.text = context.resources.getString(
                R.string.character_additional_template,
                character.species,
                character.gender,
                character.status
            )
        }
    }

    class BooksDiffCallback(
        private val oldCharacters: List<CharacterUi>,
        private val newCharacters: List<CharacterUi>
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
            return oldCharacters[oldItemPosition].status == newCharacters[newItemPosition].status
        }
    }

    interface ActionClickListener {
        fun showDetail(id: Int)
    }
}