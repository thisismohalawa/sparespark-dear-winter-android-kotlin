package sparespark.forecast.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import sparespark.forecast.data.repository.user.UserRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository, Dispatchers.Main) as T
    }
}
