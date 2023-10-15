package sparespark.forecast.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import sparespark.forecast.core.Constants.ERROR_TRY_AGAIN
import sparespark.forecast.core.Constants.ERROR_VERSION_NAME
import sparespark.forecast.core.Constants.SIGNED_IN_SUCCESS
import sparespark.forecast.core.Constants.SIGN_IN
import sparespark.forecast.core.Constants.SIGN_IN_REQUEST_CODE
import sparespark.forecast.core.Constants.SIGN_OUT
import sparespark.forecast.core.Constants.UR_SIGNED_OUT
import sparespark.forecast.core.wrapper.DataResult
import sparespark.forecast.data.model.LoginResult
import sparespark.forecast.data.repository.user.UserRepository
import sparespark.forecast.ui.base.BaseViewModel
import sparespark.forecast.ui.login.LoginEvent
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val userRepo: UserRepository,
    uiContext: CoroutineContext,
) : BaseViewModel<LoginEvent<LoginResult>>(uiContext) {

    // control Logic, communicate with view based on what happen in particular state
    internal val authAttempt = MutableLiveData<Unit>()

    // UI auth status
    internal val signInStatusText = MutableLiveData<String>()
    internal val authButtonText = MutableLiveData<String>()
    internal val versionNameText = MutableLiveData<String?>()

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        loadingState.value = true
        getPackageAppVersionName()
        when (event) {
            is LoginEvent.OnStart -> getUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClicked()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
        }
    }

    private fun getUser() = launch {
        when (val result = userRepo.getFirebaseAuthUser()) {
            is DataResult.Value -> {
                userState.value = result.value

                if (result.value == null)
                    showSignedOutState()
                else
                    showSignedInState()

                hideLoading()
            }

            is DataResult.Error ->
                handleError()
        }
    }

    private fun getPackageAppVersionName() = launch {
        when (val result = userRepo.getPackageVersionName()) {
            is DataResult.Value -> versionNameText.value = result.value
            is DataResult.Error -> versionNameText.value = ERROR_VERSION_NAME
        }
    }

    private fun onAuthButtonClicked() = launch {
        if (userState.value == null)
            authAttempt.value = Unit // tell view to begin auth attempt...
        else signOutUser()
    }

    private fun signOutUser() = launch {
        when (userRepo.signOutCurrentUser()) {
            is DataResult.Value -> {
                userState.value = null
                hideLoading()
                showSignedOutState()
            }

            is DataResult.Error ->
                handleError()
        }
    }

    private fun onSignInResult(result: LoginResult) = launch {
        if (result.requestCode == SIGN_IN_REQUEST_CODE && result.userToken != null) {

            val createGoogleUserResult = userRepo.signInGoogleUser(result.userToken)

            if (createGoogleUserResult is DataResult.Value) {
                getUser()
            } else
                handleError()
        } else
            handleError()
    }


    private fun hideLoading() {
        loadingState.value = false
    }

    private fun showLoading() {
        loadingState.value = true
    }

    private fun handleError() {
        errorState.value = ERROR_TRY_AGAIN
    }

    private fun showSignedInState() {
        signInStatusText.value = SIGNED_IN_SUCCESS
        authButtonText.value = SIGN_OUT
    }

    private fun showSignedOutState() {
        signInStatusText.value = UR_SIGNED_OUT
        authButtonText.value = SIGN_IN
    }
}
