package sparespark.forecast.data.repository.user

import sparespark.forecast.core.wrapper.DataResult
import sparespark.forecast.data.model.User

interface UserRepository {
    suspend fun getPackageVersionName(): DataResult<Exception, String?>
    suspend fun getFirebaseAuthUser(): DataResult<Exception, User?>
    suspend fun signOutCurrentUser(): DataResult<Exception, Unit>
    suspend fun signInGoogleUser(idToken: String): DataResult<Exception, Unit>
}
