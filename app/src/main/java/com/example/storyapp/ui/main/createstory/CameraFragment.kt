package com.example.storyapp.ui.main.createstory

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.*
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentCameraBinding
import com.example.storyapp.ui.BaseFragment
import com.example.storyapp.utils.createFile

class CameraFragment : BaseFragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = DEFAULT_BACK_CAMERA

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            switchCamera.setOnClickListener {
                val newCameraSelector =
                    if (cameraSelector == DEFAULT_BACK_CAMERA) DEFAULT_FRONT_CAMERA
                    else DEFAULT_BACK_CAMERA
                if (newCameraSelector == cameraSelector) return@setOnClickListener
                cameraSelector = newCameraSelector
                startCamera()
            }

            captureImage.setOnClickListener { capturePhoto() }
        }
    }

    private fun capturePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    Log.d("CAMERA CAPTURE", "$e")
                    showToast("Gagal mengambil gambar dari kamera")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = output.savedUri ?: Uri.fromFile(photoFile)

                    val bundle = Bundle().apply {
                        putBoolean(KEY_BACK_CAMERA, cameraSelector == DEFAULT_BACK_CAMERA)
                        putParcelable(KEY_URI, uri)
                    }

                    findNavController().apply {
                        previousBackStackEntry?.savedStateHandle?.set(
                            EXTRA_DATA,
                            bundle
                        )
                    }.also {
                        it.popBackStack()
                    }
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder().build()

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.d("CAMERA LAUNCH", "$e")
                showToast("Gagal menampilkan kamera")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val KEY_BACK_CAMERA = "BACK_CAMERA"
        const val KEY_URI = "KEY_URI"
    }

}