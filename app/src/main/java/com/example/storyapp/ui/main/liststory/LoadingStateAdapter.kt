package com.example.storyapp.ui.main.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapp.databinding.CardStoryLoadingBinding


class LoadingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStoryLoadingBinding.inflate(inflater, parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    class LoadingStateViewHolder(private val binding: CardStoryLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) = with(binding){
            pbRetry.isVisible = loadState is LoadState.Loading
            btRetry.isVisible = loadState is LoadState.Error
            tvRetry.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                tvRetry.text = loadState.error.localizedMessage
            }
        }
    }
}