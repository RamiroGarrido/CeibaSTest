package co.com.ceiba.mobile.pruebadeingreso.father

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.ActivityFatherBinding
import repository.remote_data_source.ConnectionService
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import utilities.Constants

class FatherActivity:AppCompatActivity() {

    val constants= Constants()
    val connectionService = ConnectionService()

    lateinit var bindingMain: ActivityFatherBinding
    lateinit var navController: NavController
    lateinit var toolbar: Toolbar
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            bindingMain = DataBindingUtil.setContentView(this, R.layout.activity_father)
            bindingMain.lifecycleOwner = this
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            // Fragment id of the top view
            appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_main))
            setupActionBarWithNavController(navController, appBarConfiguration)
            navController.navigate(R.id.nav_main)
        }
        catch (e:Exception){
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
}