package dev.alimansour.iweather.presentation.historical

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.alimansour.iweather.databinding.ItemHistoricalBinding
import dev.alimansour.iweather.domain.model.CityData
import dev.alimansour.iweather.domain.model.HistoricalData
import dev.alimansour.iweather.presentation.details.DetailsActivity
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HistoricalAdapter @Inject constructor() :
    RecyclerView.Adapter<HistoricalAdapter.AccountViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<HistoricalData>() {
        override fun areItemsTheSame(oldItem: HistoricalData, newItem: HistoricalData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoricalData, newItem: HistoricalData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding: ItemHistoricalBinding = ItemHistoricalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        try {
            val historicalData = differ.currentList[position]
            holder.binding.data = historicalData

            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DetailsActivity::class.java)
                intent.putExtra("data", historicalData)
                it.context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class AccountViewHolder(val binding: ItemHistoricalBinding) :
        RecyclerView.ViewHolder(binding.root)
}