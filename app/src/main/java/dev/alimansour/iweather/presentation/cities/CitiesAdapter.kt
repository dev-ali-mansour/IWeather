package dev.alimansour.iweather.presentation.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import dev.alimansour.iweather.databinding.ItemCityBinding
import dev.alimansour.iweather.domain.model.CityData
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class CitiesAdapter @Inject constructor() :
    RecyclerView.Adapter<CitiesAdapter.AccountViewHolder>() {
    private lateinit var list: List<CityData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding: ItemCityBinding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        try {
            val city = list[position]
            holder.binding.cityName.text = city.title
            holder.itemView.setOnClickListener {
                val action =
                    CitiesFragmentDirections.actionCitiesFragmentToHistoricalFragment(city)
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

    /**
     * Set City list
     * @param cities City List
     */
    fun setCitiesList(cities: List<CityData>) {
        this.list = cities
        notifyDataSetChanged()
    }

    inner class AccountViewHolder(val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root)
}