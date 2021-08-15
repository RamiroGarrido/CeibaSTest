package repository.local_data_source.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
data class PostEntity(
    @ColumnInfo(name = "user_id")
    var userId: Int,
    @ColumnInfo(name = "id_fetched")
    var idFetched: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "body")
    var body: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}