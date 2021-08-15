package repository.remote_data_source.api_test

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EndpointsTestAPI {

    @GET("users")
    suspend fun getUserList(): Response<List<UserDTO>>
    @GET("posts?")
    suspend fun getPostByUserId(@Query("userId")userId:Int):Response<List<PostDTO>>
}