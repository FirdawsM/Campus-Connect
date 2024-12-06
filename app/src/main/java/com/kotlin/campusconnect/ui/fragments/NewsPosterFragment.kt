package com.kotlin.campusconnect.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kotlin.campusconnect.databinding.FragmentNewsPosterBinding
import com.kotlin.campusconnect.models.Poster
import com.kotlin.campusconnect.ui.NewsActivity
import com.kotlin.campusconnect.ui.viewmodels.CampusViewModel
import kotlinx.coroutines.launch

class NewsPosterFragment : Fragment() {
    private var _binding: FragmentNewsPosterBinding? = null
    private val binding get() = _binding!!
    private lateinit var campusViewModel: CampusViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsPosterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        campusViewModel = (activity as NewsActivity).campusViewModel

        // Set up the click listener for the "Create Poster" button
        binding.btnCreatePoster.setOnClickListener {
            // Retrieve the user input for the poster title and description
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()

            // Retrieve the selected image URI (if any)
            val imageUri = binding.ivPosterImage.tag as? Uri

            // Call the createPoster function to save the new poster
            createPoster(title, description, imageUri)
        }
    }

    private fun createPoster(title: String, description: String, imageUri: Uri?) {
        // Use the CampusViewModel to create a new poster
        viewLifecycleOwner.lifecycleScope.launch {
            campusViewModel.createPoster(title, description, imageUri)

            // Navigate back to the previous screen after creating the poster
            navigateBack()
        }
    }

    private fun navigateBack() {
        // Navigate back to the previous screen
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}