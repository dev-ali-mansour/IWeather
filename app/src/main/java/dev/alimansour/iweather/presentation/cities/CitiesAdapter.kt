package dev.alimansour.iweather.presentation.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
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
    private val callback = object : DiffUtil.ItemCallback<CityData>(){
        override fun areItemsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CityData, newItem: CityData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding: ItemCityBinding = ItemCityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        try {
            val city = differ.currentList[position]
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
        return differ.currentList.size
    }

    inner class AccountViewHolder(val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root)
}