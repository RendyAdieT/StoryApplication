package com.example.storyapp.ui.main.detailstory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.imageLoader
import coil.request.ImageRequest
import com.example.storyapp.data.remote.response.stories.Story
import com.example.storyapp.databinding.FragmentDetailStoryBinding
import com.example.storyapp.ui.BaseFragment

class DetailStoryFragment : BaseFragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = Story(
            createdAt = "",
            description = arguments?.getString("description") ?: "",
            id = arguments?.getString("id") ?: "",
            lat = arguments?.getDouble("lat") ?: 0.0,
            lon = arguments?.getDouble("lon") ?: 0.0,
            name = arguments?.getString("name") ?: "",
            photoUrl = arguments?.getString("photo_url") ?: ""
        )

        binding.story = data

        val request = ImageRequest.Builder(requireContext())
            .data(data.photoUrl)
            .target(
                onSuccess = {
                    startPostponedEnterTransition()
                },
                onError = {
                    startPostponedEnterTransition()
                }
            )
            .build()

        requireActivity().application.imageLoader.enqueue(request)

        binding.executePendingBindings()
    }
}