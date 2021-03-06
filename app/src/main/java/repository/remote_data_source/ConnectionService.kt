package repository.remote_data_source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConnectionService {
    fun getRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}