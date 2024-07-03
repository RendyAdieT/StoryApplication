package com.example.storyapp.ui.main.liststory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentListStoryBinding
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.ViewModelFactory

class ListStoryFragment : BaseFragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StoriesAdapter

    private val listStoryViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListAdapter()

        binding.fabCreateStory.setOnClickListener {
            findNavController().navigate(R.id.action_listStoryFragment_to_createStoryFragment)
        }

        listStoryViewModel.stories.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            adapter.submitData(viewLifecycleOwner.lifecycle, data)
        }
    }

    private fun setupListAdapter() = with(binding.rvStories) {
        this@ListStoryFragment.adapter = StoriesAdapter { story ->
            val action = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(
                id = story.id,
                name = story.name,
                description = story.description,
                photoUrl = story.photoUrl
            )

            findNavController().navigate(action)
        }

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter = this@ListStoryFragment.adapter.withLoadStateFooter(
          LoadingStateAdapter {
                this@ListStoryFragment.adapter.retry()
            }
        )
    }
}