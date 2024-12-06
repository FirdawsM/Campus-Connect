package com.kotlin.campusconnect.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.adapters.NewsAdapter
import com.kotlin.campusconnect.databinding.FragmentFavoriteBinding
import com.kotlin.campusconnect.models.Article
import com.kotlin.campusconnect.ui.NewsActivity
import com.kotlin.campusconnect.ui.viewmodels.NewsViewModel
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var noItemCard: CardView
    private lateinit var errorText: TextView
    private var isError = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
        setupRecyclerView()
        observeFavorites()
    }

    private fun setupUI(view: View) {
        newsViewModel = (activity as NewsActivity).newsViewModel
        noItemCard = view.findViewById(R.id.noItem)
        val viewError = layoutInflater.inflate(R.layout.no_item, null)
        errorText = viewError.findViewById(R.id.errorText)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter().apply {
            setOnItemClickListener { article ->
                findNavController().navigate(
                    R.id.action_favoritesFragment_to_articleFragment,
                    Bundle().apply { putSerializable("article", article) }
                )
            }
        }

        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = true

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val article = newsAdapter.differ.currentList[position]
                    newsViewModel.deleteArticle(article)

                    Snackbar.make(binding.root, "Remove Success", Snackbar.LENGTH_LONG)
                        .setAction("Undo") { newsViewModel.addNewsToFavorites(article) }
                        .show()
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.recyclerFavourites)
    }

    private fun observeFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.getFavoriteNews().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { articles ->
                            newsAdapter.differ.submitList(articles)
                            if (articles.isNotEmpty()) hideErrorMessage()
                            else showErrorMessage("No saved articles")
                        }
                    }
                    is Resource.Error -> {
                        showErrorMessage(response.message ?: "Error loading favorites")
                    }
                    is Resource.Loading -> {
                        // Handle loading if needed
                    }
                }
            }
        }
    }

    private fun hideErrorMessage() {
        noItemCard.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        noItemCard.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}