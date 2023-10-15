package sparespark.forecast.data.provider.user

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sparespark.forecast.data.db.dao.CurrentUserDao

class UserPreferenceProviderImpl(
    private val userDao: CurrentUserDao
) : UserPreferenceProvider {

    override suspend fun getSignedUserName(): LiveData<String> = withContext(Dispatchers.IO) {
        return@withContext userDao.getCurrentUserName()
    }
}
