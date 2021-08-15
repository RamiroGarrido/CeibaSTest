package co.com.ceiba.mobile.pruebadeingreso.modules.post

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.com.ceiba.mobile.pruebadeingreso.modules.FatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import repository.local_data_source.entities.PostEntity
import repository.local_data_source.room.LocalDB
import repository.remote_data_source.api_test.EndpointsTestAPI
import repository.remote_data_source.api_test.PostDTO
import repository.remote_data_source.api_test.UserDTO
import utilities.Constants

class PostViewModel(
    dbService: LocalDB,
    remoteService: EndpointsTestAPI,
    constants: Constants
) : FatherViewModel(dbService, remoteService, constants) {

    var userObtained: UserDTO? = null
    var postList = MutableLiveData<List<PostDTO>?>()

    init {
        vProgressBar.value = View.VISIBLE
        postList.value = null
    }


    //Fetch posts by user id from the Rest API
    suspend fun getPostByUserIdAPI(): Boolean {
        var job = viewModelScope.async {
            try {
                var response = remoteService.getPostByUserId(userObtained!!.id)
                if (response.isSuccessful) {
                    val result = response.body()
                    postList.value = result
                    insertPostListDB(result)
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
    suspend fun getPostByUserIdDB(): Boolean {
        var job = viewModelScope.async(Dispatchers.IO) {
            try {
                var result = dbService.postDAO().getPostsByUserId(userObtained!!.id)
                if (result != null) {
                    if (result.isNotEmpty()) {
                        var obtainedResult: MutableList<PostDTO>? = mutableListOf()
                        for (entity in result) {
                            obtainedResult!!.add(
                                PostDTO(
                                    entity.userId,
                                    entity.idFetched,
                                    entity.title,
                                    entity.body
                                )
                            )
                        }
                        postList.postValue(obtainedResult)
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

    //Inserts the post list on the Local DB
    private fun insertPostListDB(result: List<PostDTO>?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (result != null) {
                    if (result.isNotEmpty()) {
                        var obtainedResult: MutableList<PostEntity>? = mutableListOf()
                        for (dto in result) {
                            obtainedResult!!.add(
                                PostEntity(
                                    dto.userId,
                                    dto.id,
                                    dto.title,
                                    dto.body
                                )
                            )
                        }
                        //Deletes the previous data to insert the newly fetched
                        dbService.postDAO().deletePostByUserId(userObtained!!.id)
                        var rowsInserted =
                            dbService.postDAO().insertPostList(obtainedResult!!.toList())
                    }
                }
            } catch (e: Exception) {
                Log.i(constants.TAG_ERROR, e.message!!)
                errorMessage.value = e.message!!
            }
        }
    }
}