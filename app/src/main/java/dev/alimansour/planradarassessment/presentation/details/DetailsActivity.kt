package dev.alimansour.planradarassessment.presentation.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.alimansour.planradarassessment.databinding.ActivityDetailsBinding
import dev.alimansour.planradarassessment.domain.model.HistoricalData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<HistoricalData>("data")
        data?.let {
            binding.data = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}