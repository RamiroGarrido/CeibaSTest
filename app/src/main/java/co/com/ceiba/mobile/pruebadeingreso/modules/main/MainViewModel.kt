package co.com.ceiba.mobile.pruebadeingreso.modules.main

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.com.ceiba.mobile.pruebadeingreso.modules.FatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import repository.local_data_source.entities.UserEntity
import repository.local_data_source.room.LocalDB
import repository.remote_data_source.api_test.EndpointsTestAPI
import repository.remote_data_source.api_test.UserDTO
import utilities.Constants

class MainViewModel(
    dbService: LocalDB,
    remoteService: EndpointsTestAPI,
    constants: Constants
) : FatherViewModel(dbService, remoteService, constants) {

    var userList = MutableLiveData<List<UserDTO>?>()
    var filteredUserList = MutableLiveData<List<UserDTO>?>()
    var recyclerViewState: Parcelable?=null

    init {
        userList.value = null
        filteredUserList.value = null
    }


    //Fetch the user list from the Rest API
    suspend fun getUserListAPI(): Boolean {
        val job = viewModelScope.async {
            try {
                val response = remoteService.getUserList()
                if (response.isSuccessful) {
                    val result = response.body()
                    userList.value = result
                    insertUserListDB(result)
                    return@async true
                } else {
                    errorMessage.value = response?.code().toString()
                    return@async false
                }
            } catch (e: Exception) {
                Log.i(constants.TAG_ERROR, e.message!!)
                errorMessage.value = e.message!!
                return@async false
            }
        }
        return job.await()
    }

    //Fetch the user list from the Local DB
    suspend fun getUserListDB(): Boolean {
        val job = viewModelScope.async(Dispatchers.IO) {
            try {
                val result = dbService.userDAO().getAllUsers()
                if (result != null) {
                    if (result.isNotEmpty()) {
                        val obtainedResult: MutableList<UserDTO>? = mutableListOf()
                        for (entity in result) {
                            obtainedResult!!.add(
                                UserDTO(
                                    entity.idFetched,
                                    entity.name,
                                    entity.email,
                                    entity.phone
                                )
                            )
                        }
                        userList.postValue(obtainedResult)
                        return@async true
                    }
                }
                return@async false
            } catch (e: Exception) {
                Log.i(constants.TAG_ERROR, e.message!!)
                errorMessage.value = e.message!!
                return@async false
            }
        }
        return job.await()
    }

    //Inserts the user list on the Local DB
    private fun insertUserListDB(result: List<UserDTO>?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (result != null) {
                    if (result.isNotEmpty()) {
                        val obtainedResult: MutableList<UserEntity>? = mutableListOf()
                        for (dto in result) {
                            obtainedResult!!.add(
                                UserEntity(
                                    dto.id,
                                    dto.name,
                                    dto.email,
                                    dto.phone
                                )
                            )
                        }
                        dbService.userDAO().deleteAllUser()
                        var rowsInserted =
                            dbService.userDAO().insertUserList(obtainedResult!!.toList())
                    }
                }
            } catch (e: Exception) {
                Log.i(constants.TAG_ERROR, e.message!!)
                errorMessage.value = e.message!!
            }
        }
    }

}