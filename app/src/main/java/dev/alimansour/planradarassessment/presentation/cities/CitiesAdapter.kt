package dev.alimansour.planradarassessment.presentation.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import dev.alimansour.planradarassessment.databinding.ItemCityBinding
import dev.alimansour.planradarassessment.domain.model.LocationData
import dev.alimansour.planradarassessment.presentation.CitiesFragmentDirections

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesAdapter(private val list: List<LocationData>) :
    RecyclerView.Adapter<CitiesAdapter.AccountViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding: ItemCityBinding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        try {
            val city = list[position]
            val name = "${city.name}, ${city.country}"
            holder.binding.cityName.text = name
            holder.itemView.setOnClickListener {
                val action =
                    CitiesFragmentDirections.actionCitiesFragmentToHistoricalFragment(city.name)
                Navigation.findNavController(holder.binding.root)
                    .navigate(action)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AccountViewHolder(val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root)
}