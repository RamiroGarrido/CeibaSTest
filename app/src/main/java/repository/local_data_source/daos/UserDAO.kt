package repository.local_data_source.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import repository.local_data_source.entities.UserEntity

@Dao
interface UserDAO {
    @Insert
    fun insertUserList(userList: List<UserEntity>):List<Long>
    @Query("select * from user_table")
    fun getAllUsers(): List<UserEntity>
    @Query("delete from user_table")
    fun deleteAllUser():Int
}