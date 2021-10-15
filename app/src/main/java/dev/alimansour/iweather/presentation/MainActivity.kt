package dev.alimansour.iweather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dev.alimansour.iweather.R
import dev.alimansour.iweather.databinding.ActivityMainBinding
import dev.alimansour.iweather.databinding.SearchBottomSheetBinding
import dev.alimansour.iweather.presentation.cities.CitiesViewModel
import dev.alimansour.iweather.util.dp
import dev.alimansour.iweather.util.hideKeyboard
import dev.alimansour.iweather.util.isConnected
import timber.log.Timber
import javax.inject.Inject

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: CitiesViewModel

    /* private val viewModel: CitiesViewModel by lazy {
         val remoteDataSource: RemoteDataSource = RemoteDataSourceImpl(WeatherApi.retrofitService)
         val database = WeatherDatabase.getInstance(applicationContext)
         val localDataSource: LocalDataSource = LocalDataSourceImpl(database)
         val repository: WeatherRepository = WeatherRepositoryImpl(remoteDataSource, localDataSource)
         val viewModelFactory = CitiesViewModelFactory(repository)

         ViewModelProvider(this, viewModelFactory).get(CitiesViewModel::class.java)
     }
 */
    var toolbarTitle: String
        get() = binding.toolbarTitle.text.toString()
        set(value) {
            binding.toolbarTitle.text = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyApplication).appComponent
            .mainComponentBuilder()
            .context(this)
            .activity(this)
            .build()
            .inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
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

        binding.fab.setOnClickListener { view ->
            view.visibility = View.GONE
            searchForCity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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
                            if (isConnected()) {
                                val cityName = sheetBinding.searchCityEditText.text.toString()
                                if (cityName.isNotEmpty()) {
                                    Timber.d("Searching for city: $cityName")
                                    viewModel.addCity(cityName)
                                }
                                sheetDialog.dismiss()
                            } else {
                                val message = "You are not connected to internet!"
                                Timber.e(message)
                                Snackbar.make(sheetBinding.root, message, Snackbar.LENGTH_LONG)
                                    .show()
                            }
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