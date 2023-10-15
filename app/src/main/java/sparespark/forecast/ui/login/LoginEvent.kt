package sparespark.forecast.ui.login

/*
* Login event sealed class represent different events..
*
* */
sealed class LoginEvent<out T> {
    object OnAuthButtonClick : LoginEvent<Nothing>()
    object OnStart : LoginEvent<Nothing>()
    data class OnGoogleSignInResult<out LoginResult>(val result: LoginResult) :
        LoginEvent<LoginResult>()
}