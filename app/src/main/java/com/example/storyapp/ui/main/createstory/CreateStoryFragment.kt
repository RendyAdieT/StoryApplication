package com.example.storyapp.ui.main.createstory

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.data.model.Result
import com.example.storyapp.data.remote.response.login.LoginResponse
import com.example.storyapp.data.remote.response.stories.PostStoryResponse
import com.example.storyapp.databinding.FragmentCreateStoryBinding
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.rotateBitmap
import com.example.storyapp.utils.uriToFile
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.Serializable

class CreateStoryFragment : BaseFragment() {

    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!

    private var gambarFile: File? = null

    private val createStoryViewModel: CreateStoryViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            gambarFile = myFile
            binding.ivImagePreview.setImageURI(selectedImg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btOpenCamera.setOnClickListener {
                findNavController().navigate(R.id.action_createStoryFragment_to_cameraFragment)
            }

            btOpenGallery.setOnClickListener {
                openGallery()
            }

            buttonAdd.setOnClickListener {
                uploadImage()
            }

            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<Bundle>(
                    CameraFragment.EXTRA_DATA
                )?.observe(viewLifecycleOwner) { bundle ->
                    val fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(CameraFragment.KEY_URI, Parcelable::class.java)
                    } else {
                        bundle.getParcelable(CameraFragment.KEY_URI)
                    } as? Uri? ?: return@observe

                    Log.d("OBSERVE DATA CAMERA", "$bundle")

                    val isBackCamera =
                        bundle?.getBoolean(CameraFragment.KEY_BACK_CAMERA, true) ?: true

                    val result = rotateBitmap(
                        BitmapFactory.decodeFile(fileUri.path),
                        isBackCamera
                    )

                    gambarFile = uriToFile(fileUri, requireContext())

                    ivImagePreview.setImageBitmap(result)
                }
        }
    }

    private fun openGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent, "Pilih Foto/Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun displayLoading(isLoading: Boolean) = with(binding) {
        pbCreateStory.isVisible = isLoading

        edAddDescription.isEnabled = !isLoading
        btOpenCamera.isEnabled = !isLoading
        btOpenGallery.isEnabled = !isLoading
        buttonAdd.isEnabled = !isLoading
        ivImagePreview.isEnabled = !isLoading
    }

    private fun uploadImage() {
        if (gambarFile == null) {
            showToast("Foto/Gambar wajib ada.")
            return
        }

        val descriptionText = binding.edAddDescription.text.toString()
        if (descriptionText.isEmpty()) {
            showToast("Deskripsi tidak boleh kosong.")
            return
        }

        displayLoading(true)

        val fileGambar = reduceFileImage(gambarFile as File)
        val description = descriptionText.toRequestBody("text/plain".toMediaType())
        val requestImageFile = fileGambar.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            fileGambar.name,
            requestImageFile
        )

        createStoryViewModel.postStory(imageMultipart, description)
            .observe(viewLifecycleOwner) { result ->
                if (result == null) return@observe
                checkResult(result)
            }

    }

    private fun checkResult(result: Result<PostStoryResponse>) {
        if (result is Result.Loading) {
            displayLoading(true)
            return
        }

        displayLoading(false)

        if (result is Result.Error) {
            showToast(result.error)
            return
        }

        val data = (result as Result.Success).data.message
        showToast(data, Toast.LENGTH_LONG)
        findNavController().navigate(R.id.action_createStoryFragment_to_listStoryFragment)
    }
}
