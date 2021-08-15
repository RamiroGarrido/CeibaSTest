package co.com.ceiba.mobile.pruebadeingreso.modules

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.local_data_source.room.LocalDB
import repository.remote_data_source.api_test.EndpointsTestAPI
import utilities.Constants

open class FatherViewModel(
    val dbService: LocalDB,
    val remoteService: EndpointsTestAPI,
    val constants: Constants
) : ViewModel() {
    var vProgressBar = MutableLiveData<Int?>()
    var errorMessage = MutableLiveData<String?>()

    init {
        vProgressBar.value = null
        errorMessage.value = null
    }

    //Deletes all the tables inside the Local DB
    fun deleteAllRecordsDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dbService.clearAllTables()
                //LOG CREATED FOR CEIBA DEVELOPERS TO WATCH RESULTS ON CONSOLE
                Log.i("RGM ROWS D -> ", "ALL ROWS DELETED!")
            } catch (e: Exception) {
                Log.i(constants.TAG_ERROR, e.message!!)
                errorMessage.value = e.message!!
            }
        }
    }
}