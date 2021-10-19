package dev.alimansour.iweather.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.alimansour.iweather.databinding.FragmentCitiesBinding
import dev.alimansour.iweather.util.Resource
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
            layoutManager = LinearLayoutManager(activity)
            adapter = citiesAdapter

            viewModel.citiesData.observe(viewLifecycleOwner, { resource ->
                if (resource is Resource.Success) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            citiesAdapter.differ.submitList(list)
                            adapter = citiesAdapter
                        }
                    }
                }
            })
        }

        viewModel.getCities()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}