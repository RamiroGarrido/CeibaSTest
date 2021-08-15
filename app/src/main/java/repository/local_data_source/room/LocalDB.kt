package repository.local_data_source.room
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import repository.local_data_source.daos.PostDAO
import repository.local_data_source.daos.UserDAO
import repository.local_data_source.entities.PostEntity
import repository.local_data_source.entities.UserEntity
import utilities.Constants

@Database(
    entities = [UserEntity::class, PostEntity::class],
    version = 3,
    exportSchema = false
)
abstract class LocalDB : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO

    //ALLOW ACCESSING THE OBJECT WITHOUT AN INSTANCE
    companion object {
        @Volatile
        private var INSTANCE: LocalDB? = null

        fun getInstance(context: Context): LocalDB {
            val constants = Constants()
            //ALLOW ONLY ONE THEAD AT TIME TO ACCESS
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDB::class.java,
                        constants.DB_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}