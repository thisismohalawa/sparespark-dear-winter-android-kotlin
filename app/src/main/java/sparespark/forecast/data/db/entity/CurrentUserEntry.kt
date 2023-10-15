package sparespark.forecast.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity(tableName = "current_user")
data class CurrentUserEntry(
    val userId: String,
    val userName: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_USER_ID
}
