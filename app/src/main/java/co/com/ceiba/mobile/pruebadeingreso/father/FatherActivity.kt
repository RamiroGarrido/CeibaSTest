package co.com.ceiba.mobile.pruebadeingreso.father

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.ActivityFatherBinding
import repository.remote_data_source.ConnectionService
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import repository.local_data_source.room.LocalDB
import repository.remote_data_source.api_test.EndpointsTestAPI
import utilities.Constants

class FatherActivity : AppCompatActivity() {

    lateinit var bindingMain: ActivityFatherBinding
    lateinit var viewModel: FatherViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    //The constants and the services are instantiated ONCE and are used by all fragments
    val constants = Constants()
    private val connectionService = ConnectionService()
    lateinit var dbService: LocalDB
    lateinit var remoteService: EndpointsTestAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_father)
            bindingMain.lifecycleOwner = this
            remoteService = connectionService.getRetrofit(constants.URL_BASE)
                .create(EndpointsTestAPI::class.java)
            dbService = LocalDB.getInstance(applicationContext)
            viewModel = ViewModelProvider(this).get(FatherViewModel::class.java)
            bindingMain.fatherViewModel = viewModel
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            // Fragment id of the top view
            appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_main))
            setupActionBarWithNavController(navController, appBarConfiguration)
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
        }
    }


    //Pops the top view fragment when the up button is clicked on the toolbar
    override fun onSupportNavigateUp(): Boolean {
        return try {
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
            false
        }
    }

    override fun onBackPressed() {
        // We don't want to go back to the splash screen
    }

    //Function intended to be used globally
    fun showMessage(message: String) {
        try {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
        }
    }

    //Checks if there's internet available
    fun isInternetAvailable(): Boolean {
        try {
            var result = false
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //Current versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
            //Legacy versions
            else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }
            return result
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
            return false
        }
    }

    //On destroy the database connection is closed
    override fun onDestroy() {
        try {
            super.onDestroy()
            dbService.close()
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
        }
    }
}