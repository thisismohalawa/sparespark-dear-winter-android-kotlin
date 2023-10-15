package sparespark.forecast.data.repository.user

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sparespark.forecast.core.internal.awaitTaskCompletable
import sparespark.forecast.core.internal.toUser
import sparespark.forecast.core.internal.toUserEntry
import sparespark.forecast.core.wrapper.DataResult
import sparespark.forecast.data.db.dao.CurrentUserDao
import sparespark.forecast.data.model.User

class UserRepositoryImpl(
    private val context: Context,
    private val userDao: CurrentUserDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : UserRepository {

    private suspend fun updateLocalUser(user: User) = withContext(Dispatchers.IO) {
        userDao.upsert(user.toUserEntry)
    }

    private suspend fun deleteLocalUser() = withContext(Dispatchers.IO) {
        userDao.deleteUser()
    }

    /*
    *
    *
    * */

    override suspend fun getPackageVersionName(): DataResult<Exception, String?> =
        DataResult.build {
            "version: ${
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            }"
        }

    override suspend fun getFirebaseAuthUser(): DataResult<Exception, User?> = DataResult.build {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            updateLocalUser(firebaseUser.toUser)
            firebaseUser.toUser
        } else null
    }

    override suspend fun signOutCurrentUser(): DataResult<Exception, Unit> = DataResult.build {
        auth.signOut()
        deleteLocalUser()
    }

    override suspend fun signInGoogleUser(idToken: String): DataResult<Exception, Unit> =
        DataResult.build {
            // request credential from google, give it to firebase auth.
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            awaitTaskCompletable(auth.signInWithCredential(credential))
        }
}
