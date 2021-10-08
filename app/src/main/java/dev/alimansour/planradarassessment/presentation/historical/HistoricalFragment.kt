package dev.alimansour.planradarassessment.presentation.historical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.data.local.LocalDataSource
import dev.alimansour.planradarassessment.data.local.LocalDataSourceImpl
import dev.alimansour.planradarassessment.data.local.WeatherDatabase
import dev.alimansour.planradarassessment.data.remote.RemoteDataSource
import dev.alimansour.planradarassessment.data.remote.RemoteDataSourceImpl
import dev.alimansour.planradarassessment.data.remote.WeatherApi
import dev.alimansour.planradarassessment.data.repository.WeatherRepositoryImpl
import dev.alimansour.planradarassessment.databinding.FragmentHistoricalBinding
import dev.alimansour.planradarassessment.domain.repository.WeatherRepository
import dev.alimansour.planradarassessment.presentation.MainActivity
import dev.alimansour.planradarassessment.presentation.cities.CitiesViewModel
import dev.alimansour.planradarassessment.presentation.cities.CitiesViewModelFactory
import dev.alimansour.planradarassessment.util.Status

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalFragment : Fragment() {

    private var _binding: FragmentHistoricalBinding? = null
    private lateinit var historicalAdapter: HistoricalAdapter

    private val viewModel: HistoricalViewModel by lazy {
        val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(WeatherApi.retrofitService)
        val database = WeatherDatabase.getInstance(requireContext().applicationContext)
        val localDataSource: LocalDataSource = LocalDataSourceImpl(database)
        val repository: WeatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
        val viewModelFactory = HistoricalViewModelFactory(repository)

        ViewModelProvider(requireActivity(), viewModelFactory).get(HistoricalViewModel::class.java)
    }

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

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = HistoricalAdapter(listOf())

            viewModel.historicalData.observe(viewLifecycleOwner, { resource ->
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            historicalAdapter = HistoricalAdapter(list)
                            adapter = historicalAdapter
                        }
                    }
                }
            })
        }

        viewModel.getHistoricalDataList(city.id)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}