package com.kotlin.campusconnect.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin.campusconnect.R
import com.kotlin.campusconnect.adapters.PosterAdapter
import com.kotlin.campusconnect.databinding.FragmentCampusBinding
import com.kotlin.campusconnect.models.Poster
import com.kotlin.campusconnect.ui.NewsActivity
import com.kotlin.campusconnect.ui.viewmodels.CampusViewModel
import com.kotlin.campusconnect.util.Resource
import kotlinx.coroutines.launch

class CampusFragment : Fragment(R.layout.fragment_campus) {
    private var _binding: FragmentCampusBinding? = null
    private val binding get() = _binding!!
    private lateinit var campusViewModel: CampusViewModel
    private lateinit var posterAdapter: PosterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCampusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        observePosters()
    }

    private fun setupUI() {
        campusViewModel = (activity as NewsActivity).campusViewModel
        binding.fabCreatePoster.setOnClickListener {
            findNavController().navigate(R.id.action_campusFragment_to_newsPosterFragment)
        }
    }

    private fun setupRecyclerView() {
        posterAdapter = PosterAdapter().apply {
            setOnItemClickListener { poster ->
                findNavController().navigate(
                    R.id.action_campusFragment_to_posterDetailFragment,
                    Bundle().apply { putSerializable("poster", poster) }
                )
            }
            setOnLongClickListener { poster ->
                campusViewModel.deletePoster(poster)
                Snackbar.make(binding.root, "Poster deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { campusViewModel.addPoster(poster) }
                    .show()
                true
            }
        }

        binding.rvPosters.apply {
            adapter = posterAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = true
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val poster = posterAdapter.differ.currentList[position]
                    campusViewModel.deletePoster(poster)
                }
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.rvPosters)
    }

    private fun observePosters() {
        viewLifecycleOwner.lifecycleScope.launch {
            campusViewModel.getPosters().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { posters ->
                            posterAdapter.differ.submitList(posters)
                        }
                    }
                    is Resource.Error -> {
                        // Handle error
                    }
                    is Resource.Loading -> {
                        // Handle loading
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}