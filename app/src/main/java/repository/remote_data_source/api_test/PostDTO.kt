package repository.remote_data_source.api_test

import com.google.gson.annotations.SerializedName

data class PostDTO(
    @SerializedName("userId") var userId: Int,
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("body") var body: String
)