package com.kotlin.campusconnect.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlin.campusconnect.databinding.FragmentPosterDetailBinding
import com.kotlin.campusconnect.models.Poster

class PosterDetailFragment : Fragment() {
    private var _binding: FragmentPosterDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPosterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val poster = arguments?.getSerializable("poster") as Poster
        bindPosterDetails(poster)
    }

    private fun bindPosterDetails(poster: Poster) {
        binding.tvTitle.text = poster.title
        binding.tvDescription.text = poster.description
        binding.tvUserName.text = poster.userName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}