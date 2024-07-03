package com.example.storyapp.ui.main.liststory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.example.storyapp.data.remote.response.stories.Story
import com.example.storyapp.databinding.CardStoriesBinding

class StoriesAdapter(private val callback: (story: Story) -> Unit) :
    PagingDataAdapter<Story, StoriesAdapter.StoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CardStoriesBinding.inflate(inflater, parent, false)
        return StoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.apply {
            bind(item)
            view.root.setOnClickListener {
                callback.invoke(item)
            }
        }
    }

    inner class StoriesViewHolder(val view: CardStoriesBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: Story) = with(view){
            story = item

            val drawable = CircularProgressDrawable(root.context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }

            ivItemPhoto.load(item.photoUrl) {
                allowHardware(false)
                placeholder(drawable)
            }

            executePendingBindings()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

