package dev.alimansour.iweather.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.alimansour.iweather.R
import dev.alimansour.iweather.databinding.FragmentCitiesBinding
import dev.alimansour.iweather.presentation.MainActivity
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@AndroidEntryPoint
class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    @Inject
    lateinit var viewModel: CitiesViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)
        citiesAdapter.differ.submitList(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = citiesAdapter

            lifecycleScope.launchWhenStarted { collectCitiesFlow() }

            viewModel.getCities()
            initSwipeToDelete()

            return binding.root
        }
    }

    private fun initSwipeToDelete() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val city = citiesAdapter.differ.currentList[position]
                viewModel.deleteCity(city)
                Snackbar.make(
                    binding.root,
                    "${city.name}, ${city.country} was deleted successfully!",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction(getString(R.string.undo)) {
                        viewModel.addCity(city.name)
                    }
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.citiesRecyclerView)
        }
    }

    /**
     * Collect citiesFlow from CitiesViewModel
     * Load data into RecyclerView if response is successful
     */
    private suspend fun collectCitiesFlow() {
        val mainActivity = requireActivity() as MainActivity
        viewModel.citiesFlow.collect { resource ->
            when (resource) {
                is Resource.Loading -> mainActivity.isProgressBarVisible = true
                is Resource.Success -> {
                    mainActivity.isProgressBarVisible = false
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            citiesAdapter.differ.submitList(list)
                            binding.citiesRecyclerView.adapter = citiesAdapter
                        }
                    }
                }
                is Resource.Error -> {
                    mainActivity.isProgressBarVisible = false
                    Snackbar.make(
                        binding.root,
                        resource.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}