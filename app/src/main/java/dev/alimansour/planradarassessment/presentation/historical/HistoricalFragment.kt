package dev.alimansour.planradarassessment.presentation.historical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.databinding.FragmentHistoricalBinding
import dev.alimansour.planradarassessment.presentation.MainActivity
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
        ViewModelProvider(this).get(HistoricalViewModel::class.java)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val cityName = requireArguments().getString("cityName")
        (requireActivity() as MainActivity).toolbarTitle = "$cityName Historical"

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

        cityName?.let { viewModel.getHistoricalData(it) }

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