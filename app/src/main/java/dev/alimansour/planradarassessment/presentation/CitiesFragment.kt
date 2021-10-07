package dev.alimansour.planradarassessment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.alimansour.planradarassessment.databinding.FragmentCitiesBinding
import dev.alimansour.planradarassessment.domain.model.LocationData
import dev.alimansour.planradarassessment.presentation.cities.CitiesAdapter

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesFragment : Fragment() {

    private var _binding: FragmentCitiesBinding? = null
    private lateinit var citiesAdapter: CitiesAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitiesBinding.inflate(inflater, container, false)

        citiesAdapter = CitiesAdapter(
            listOf(
                LocationData("London", "UK", 20.0, "Cloudy", 0.45, 20),
                LocationData("Paris", "FR", 20.0, "Cloudy", 0.45, 20),
                LocationData("Vienna", "AUT", 20.0, "Cloudy", 0.45, 20),
             )
        )
        binding.citiesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = citiesAdapter
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}