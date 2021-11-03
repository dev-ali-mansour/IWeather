package dev.alimansour.iweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.alimansour.iweather.R
import dev.alimansour.iweather.databinding.ActivityMainBinding
import dev.alimansour.iweather.databinding.SearchBottomSheetBinding
import dev.alimansour.iweather.presentation.cities.CitiesViewModel
import dev.alimansour.iweather.presentation.historical.HistoricalViewModel
import dev.alimansour.iweather.util.ActionState
import dev.alimansour.iweather.util.Resource
import dev.alimansour.iweather.util.dp
import dev.alimansour.iweather.util.hideKeyboard
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var isProgressBarVisible: Boolean
        get() = binding.progressBar.isVisible
        set(value) {
            binding.progressBar.isVisible = value
        }

    @Inject
    lateinit var citiesViewModel: CitiesViewModel

    @Inject
    lateinit var historicalViewModel: HistoricalViewModel

    var toolbarTitle: String
        get() = binding.toolbarTitle.text.toString()
        set(value) {
            binding.toolbarTitle.text = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            val params = androidx.appcompat.widget.Toolbar.LayoutParams(
                androidx.appcompat.widget.Toolbar.LayoutParams.WRAP_CONTENT,
                androidx.appcompat.widget.Toolbar.LayoutParams.WRAP_CONTENT
            )
            if (destination.id == R.id.CitiesFragment) {
                params.setMargins(90.dp, 90.dp, 0.dp, 0.dp)
                binding.fab.visibility = View.VISIBLE

            } else {
                params.setMargins(0.dp, 90.dp, 0.dp, 0.dp)
                binding.fab.visibility = View.GONE
            }
            binding.toolbarTitle.layoutParams = params
            toolbarTitle = destination.label.toString()
        }
        setupActionBarWithNavController(navController, appBarConfiguration)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectActionFlow()
            }
        }
        binding.fab.setOnClickListener { view ->
            view.visibility = View.GONE
            searchForCity()
        }
        historicalViewModel.startUpdateHistoricalWorker()
    }

    private suspend fun collectActionFlow() {
        citiesViewModel.actionFlow.collect { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.isVisible = true
                is Resource.Success -> {
                    if (resource.data == ActionState.Add) {
                        binding.progressBar.isVisible = false
                        Snackbar.make(
                            binding.root,
                            "City was added successfully",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Snackbar.make(
                        binding.root,
                        resource.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Search for a new city to add it to the database
     */
    private fun searchForCity() {
        try {
            val sheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
            val sheetBinding: SearchBottomSheetBinding = SearchBottomSheetBinding
                .inflate(LayoutInflater.from(this), null, false)
            sheetDialog.apply {
                setContentView(sheetBinding.root)
                setCancelable(true)
                show()

                setOnDismissListener {
                    binding.fab.visibility = View.VISIBLE
                }

                sheetBinding.searchCityEditText.setOnEditorActionListener(
                    TextView.OnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            hideKeyboard()

                            val cityName = sheetBinding.searchCityEditText.text.toString()
                            if (cityName.isNotEmpty()) {
                                Timber.d("Searching for city: $cityName")
                                citiesViewModel.addCity(cityName)
                            }
                            sheetDialog.dismiss()

                            return@OnEditorActionListener true
                        }
                        false
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}