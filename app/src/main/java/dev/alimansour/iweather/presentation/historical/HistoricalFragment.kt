package dev.alimansour.iweather.presentation.historical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.alimansour.iweather.databinding.FragmentHistoricalBinding
import dev.alimansour.iweather.presentation.MainActivity
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@AndroidEntryPoint
class HistoricalFragment : Fragment() {

    private var _binding: FragmentHistoricalBinding? = null

    @Inject
    lateinit var historicalAdapter: HistoricalAdapter

    @Inject
    lateinit var historicalViewModel: HistoricalViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = HistoricalFragmentArgs.fromBundle(requireArguments())
        val city = args.city

        (requireActivity() as MainActivity).toolbarTitle = "${city.name}, Historical"

        _binding = FragmentHistoricalBinding.inflate(inflater, container, false)
        historicalAdapter.differ.submitList(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = historicalAdapter

            lifecycleScope.launchWhenStarted {
                collectHistoricalFlow()
                collectActionFlow()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            Timber.v("Refreshing historical data of saved cities")
            historicalViewModel.updateHistoricalData()
        }

        historicalViewModel.getHistorical(city.id)

        return binding.root
    }

    private suspend fun collectActionFlow() {
        (requireActivity() as MainActivity).apply {
            historicalViewModel.actionFlow.collect { resource ->
                when (resource) {
                    is Resource.Loading -> isProgressBarVisible = true
                    is Resource.Success -> {
                        isProgressBarVisible = false
                        Snackbar.make(
                            binding.root,
                            "Historical data was updated successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Error -> {
                        isProgressBarVisible = false
                        Timber.e(resource.message.toString())
                        Snackbar.make(
                            binding.root,
                            resource.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
    }

    private suspend fun collectHistoricalFlow() {
        binding.apply {
            historicalViewModel.historicalFlow.collect { resource ->
                when (resource) {
                    is Resource.Loading ->
                        swipeRefresh.post { swipeRefresh.isRefreshing = true }
                    is Resource.Success -> {
                        swipeRefresh.isRefreshing = false
                        resource.data?.let { list ->
                            if (list.isNotEmpty()) {
                                historicalAdapter.differ.submitList(list)
                                citiesRecyclerView.adapter = historicalAdapter
                            }
                        }
                    }
                    is Resource.Error -> {
                        swipeRefresh.isRefreshing = false
                        Snackbar.make(root, resource.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
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