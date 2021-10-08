package dev.alimansour.planradarassessment.util

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import dev.alimansour.planradarassessment.R
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object BindingAdapters {

    private const val IMAGE_URL = "http://openweathermap.org/img/wn"

    @JvmStatic
    @BindingAdapter("date")
    fun loadDateTime(txtDateTime: TextView, date: String) {
        runCatching {
            val mDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)!!
            val formatter = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.ENGLISH)
            txtDateTime.text = formatter.format(mDate)
        }.onFailure { it.printStackTrace() }
    }

    @JvmStatic
    @BindingAdapter("iconName")
    fun loadIcon(imageView: ImageView, iconName: String) {
        runCatching {
            Glide.with(imageView.context)
                .load("$IMAGE_URL/$iconName@2x.png")
                .into(imageView)
        }.onFailure { it.printStackTrace() }
    }
}