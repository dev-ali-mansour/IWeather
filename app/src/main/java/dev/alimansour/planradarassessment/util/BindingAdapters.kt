package dev.alimansour.planradarassessment.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("date")
    fun loadDateTime(txtDateTime: TextView, date: String) {
        runCatching {
            val mDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)!!
            val formatter = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.ENGLISH)
            txtDateTime.text = formatter.format(mDate)
        }.onFailure { it.printStackTrace() }
    }

}