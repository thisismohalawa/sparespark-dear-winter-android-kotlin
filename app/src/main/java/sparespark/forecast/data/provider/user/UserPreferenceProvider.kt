package sparespark.forecast.data.provider.user

import androidx.lifecycle.LiveData

interface UserPreferenceProvider {
    suspend fun getSignedUserName(): LiveData<String>
}
