package repository.local_data_source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import repository.local_data_source.entities.PostEntity

@Dao
interface PostDAO {
    @Insert
    fun insertPostList(postList: List<PostEntity>):List<Long>
    @Query("select * from post_table")
    fun getAllPosts(): List<PostEntity>
    @Query("select * from post_table where user_id = :userId")
    fun getPostsByUserId(userId:Int): List<PostEntity>
    @Query("delete from post_table where user_id = :userId")
    fun deletePostByUserId(userId:Int):Int
    @Query("delete from post_table")
    fun deleteAllPost():Int
}