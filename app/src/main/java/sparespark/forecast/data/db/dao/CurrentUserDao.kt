package sparespark.forecast.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sparespark.forecast.data.db.entity.CURRENT_USER_ID
import sparespark.forecast.data.db.entity.CurrentUserEntry
@Dao
interface CurrentUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(userEntry: CurrentUserEntry)

    @Query("select * from current_user where id = $CURRENT_USER_ID")
    fun getCurrentUser(): LiveData<CurrentUserEntry>

    @Query("select userName from current_user where id = $CURRENT_USER_ID")
    fun getCurrentUserName(): LiveData<String>

    @Query("DELETE FROM current_user")
    fun deleteUser()

}
