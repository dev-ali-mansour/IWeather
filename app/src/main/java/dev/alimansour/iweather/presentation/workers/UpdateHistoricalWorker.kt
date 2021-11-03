package dev.alimansour.iweather.presentation.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.alimansour.iweather.domain.usecase.historical.UpdateHistoricalDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
@HiltWorker
class UpdateHistoricalWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    var useCase: UpdateHistoricalDataUseCase
) : Worker(appContext, workerParams) {

    override fun doWork(): Result = try {
       CoroutineScope(Dispatchers.IO).launch {
           useCase.execute()
       }
       Result.success()
   } catch (e: Exception) {
       Timber.e("Error updating historical data using worker: $e")
       Result.failure()
   }
}