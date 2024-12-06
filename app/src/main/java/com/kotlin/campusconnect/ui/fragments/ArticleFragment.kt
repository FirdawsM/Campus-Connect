package com.kotlin.campusconnect.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.databinding.FragmentArticleBinding
import com.kotlin.campusconnect.ui.NewsActivity
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsViewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupContent()
        setupObservers()
        setupListeners()
    }

    private fun setupViewModel() {
        newsViewModel = (activity as NewsActivity).newsViewModel
        newsViewModel.getFavoriteNewsByPublishedAt(args.article.publishedAt ?: "")
    }

    private fun setupContent() {
        with(args.article) {
            binding.apply {
                articleTitle.text = title
                articleSource.text = source?.name
                articleDateTime.text = publishedAt?.substring(0, 10)
                articleContent.text = description
                articleAuthor.text = author

                Glide.with(this@ArticleFragment)
                    .load(urlToImage)
                    .placeholder(R.drawable.image_newspaper)
                    .error(R.drawable.image_newspaper)
                    .into(articleImage)
            }
        }
    }

    private fun setupObservers() {
        newsViewModel.isArticleSaved.observe(viewLifecycleOwner) { isSaved ->
            binding.btnBookmark.setImageResource(
                if (isSaved) R.drawable.baseline_bookmark_24
                else R.drawable.ic_bookmark
            )
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnBookmark.setOnClickListener {
                if (newsViewModel.isArticleSaved.value == true) {
                    handleUnbookmark()
                } else {
                    handleBookmark()
                }
            }
        }
    }

    private fun handleBookmark() {
        newsViewModel.addNewsToFavorites(args.article)
        Snackbar.make(
            requireView(),
            "Added to favorites",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleUnbookmark() {
        newsViewModel.deleteArticleByPublishedAt(args.article.publishedAt ?: "")
        Snackbar.make(requireView(), "Remove Success", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                newsViewModel.addNewsToFavorites(args.article)
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}