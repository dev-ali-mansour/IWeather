package dev.alimansour.planradarassessment.presentation.cities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.databinding.FragmentCitiesBinding
import dev.alimansour.planradarassessment.presentation.MyApplication
import dev.alimansour.planradarassessment.util.Status
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    @Inject
    lateinit var viewModel: CitiesViewModel

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
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)
        citiesAdapter.setCitiesList(listOf())

        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = citiesAdapter

            viewModel.citiesData.observe(viewLifecycleOwner, { resource ->
                if (resource.status == Status.SUCCESS) {
                    resource.data?.let { list ->
                        if (list.isNotEmpty()) {
                            citiesAdapter.setCitiesList(list)
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