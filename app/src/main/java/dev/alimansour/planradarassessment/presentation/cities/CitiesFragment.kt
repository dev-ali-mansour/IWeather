package dev.alimansour.planradarassessment.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.data.local.LocalDataSource
import dev.alimansour.planradarassessment.data.local.LocalDataSourceImpl
import dev.alimansour.planradarassessment.data.local.WeatherDatabase
import dev.alimansour.planradarassessment.data.local.WeatherDatabase_Impl
import dev.alimansour.planradarassessment.data.remote.RemoteDataSource
import dev.alimansour.planradarassessment.data.remote.RemoteDataSourceImpl
import dev.alimansour.planradarassessment.data.remote.WeatherApi
import dev.alimansour.planradarassessment.data.repository.WeatherRepositoryImpl
import dev.alimansour.planradarassessment.databinding.FragmentCitiesBinding
import dev.alimansour.planradarassessment.domain.model.CityData
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import dev.alimansour.planradarassessment.presentation.historical.HistoricalAdapter
import dev.alimansour.planradarassessment.presentation.historical.HistoricalViewModel
import dev.alimansour.planradarassessment.util.Status

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null
    private lateinit var citiesAdapter: CitiesAdapter

    private val viewModel: CitiesViewModel by lazy {
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(WeatherApi.retrofitService)
        val database = WeatherDatabase.getInstance(requireContext().applicationContext)
        val localDataSource: LocalDataSource = LocalDataSourceImpl(database)
        val repository: WeatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
        val viewModelFactory = CitiesViewModelFactory(repository)

        ViewModelProvider(requireActivity(), viewModelFactory).get(CitiesViewModel::class.java)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)
        citiesAdapter = CitiesAdapter(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = citiesAdapter

            viewModel.citiesData.observe(viewLifecycleOwner, { resource ->
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            citiesAdapter = CitiesAdapter(list)
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