package dev.alimansour.planradarassessment.presentation.historical

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.databinding.FragmentHistoricalBinding
import dev.alimansour.planradarassessment.presentation.MainActivity
import dev.alimansour.planradarassessment.presentation.MyApplication
import dev.alimansour.planradarassessment.util.Status
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalFragment : Fragment() {

    private var _binding: FragmentHistoricalBinding? = null

    @Inject
    lateinit var historicalAdapter: HistoricalAdapter

    @Inject
    lateinit var viewModel: HistoricalViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        runCatching {
            // Inject fragment to MainComponent
            (requireActivity().application as MyApplication).appComponent
                .mainComponentBuilder()
                .context(requireContext())
                .activity(requireActivity())
                .build()
                .inject(this)
        }.onFailure { it.printStackTrace() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = HistoricalFragmentArgs.fromBundle(requireArguments())
        val city = args.city

        (requireActivity() as MainActivity).toolbarTitle = "${city.name}, Historical"

        _binding = FragmentHistoricalBinding.inflate(inflater, container, false)
        historicalAdapter.setHistoricalList(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = historicalAdapter

            viewModel.historicalData.observe(viewLifecycleOwner, { resource ->
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            historicalAdapter.setHistoricalList(list)
                            adapter = historicalAdapter
                        }
                    }
                }
            })
        }

        viewModel.getHistoricalDataList(city.id)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}