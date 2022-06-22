package com.haberturm.rickandmorty.presentation.episodes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haberturm.rickandmorty.R
import com.haberturm.rickandmorty.databinding.EpisodesItemBinding
import com.haberturm.rickandmorty.presentation.entities.EpisodeUi


class EpisodesListAdapter(
    private val listener: ActionClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<EpisodesListAdapter.ViewHolder>() {

    private val episodes: ArrayList<EpisodeUi> = arrayListOf()

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            EpisodesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodes = episodes[position]
        holder.itemView.setOnClickListener {
            listener.showDetail(episodes.id)
        }
        holder.bind(episodes)
    }

    fun submitUpdate(update: List<EpisodeUi>) {
        val callback = BooksDiffCallback(episodes, update)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(callback)

        episodes.clear()
        episodes.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(
        private var binding: EpisodesItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: EpisodeUi) {
            binding.episodeName.text = episode.name
            binding.episodeAdditional.text =
                context.resources.getString(
                    R.string.episode_additional_template,
                    episode.episode,
                    episode.airDate
                )
        }
    }

    class BooksDiffCallback(
        private val oldCharacters: List<EpisodeUi>,
        private val newCharacters: List<EpisodeUi>
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