package co.com.ceiba.mobile.pruebadeingreso.modules.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import repository.local_data_source.room.LocalDB
import repository.remote_data_source.api_test.EndpointsTestAPI
import utilities.Constants

class PostVMFactory(private val dbService: LocalDB, private val remoteService: EndpointsTestAPI, private val constants: Constants): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PostViewModel::class.java)){
            return PostViewModel(dbService, remoteService,constants) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}