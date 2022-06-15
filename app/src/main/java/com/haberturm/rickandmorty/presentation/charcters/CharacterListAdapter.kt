package com.haberturm.rickandmorty.presentation.charcters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haberturm.rickandmorty.databinding.CharactersItemBinding
import com.haberturm.rickandmorty.entities.CharacterUi



class CharacterListAdapter(
    private val listener: ActionClickListener
) :
    RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    private val characters: ArrayList<CharacterUi> = arrayListOf()

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(CharactersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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

    class ViewHolder(private var binding: CharactersItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterUi){
            binding.character = character
            binding.executePendingBindings()
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