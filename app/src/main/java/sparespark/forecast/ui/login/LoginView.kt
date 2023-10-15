package sparespark.forecast.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sparespark.forecast.R
import sparespark.forecast.core.Constants.ERROR_SIGN_IN
import sparespark.forecast.core.Constants.SIGN_IN_REQUEST_CODE
import sparespark.forecast.core.view.makeToast
import sparespark.forecast.core.view.setClickListenerWithViewDelayEnabled
import sparespark.forecast.core.view.visible
import sparespark.forecast.data.model.LoginResult
import sparespark.forecast.databinding.LoginViewBinding
import sparespark.forecast.ui.base.ScopedFragment
import sparespark.forecast.ui.login.viewmodel.LoginViewModel
import sparespark.forecast.ui.login.viewmodel.LoginViewModelFactory

class LoginView : ScopedFragment<LoginViewBinding>(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var viewModel: LoginViewModel
    private val viewModelFactory: LoginViewModelFactory by instance()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): LoginViewBinding =
        LoginViewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
        viewModel.startObserving()
        setUpViewClickListener()
    }

    private fun LoginViewModel.startObserving() {
        handleEvent(LoginEvent.OnStart)
        loading.observe(viewLifecycleOwner) {
            binding.progressCircular.visible(it)
        }
        error.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) activity.makeToast(it)
        }
        signInStatusText.observe(viewLifecycleOwner) {
            binding.txtLoginStatus.text = it
        }
        authButtonText.observe(viewLifecycleOwner) {
            (binding.btnLogin.getChildAt(0) as TextView).text = it
        }
        authAttempt.observe(viewLifecycleOwner) {
            startSignInFlow()
        }
        versionNameText.observe(viewLifecycleOwner) {
            binding.textVersionName.text = it
        }
    }

    private fun setUpViewClickListener() {
        binding.btnLogin.setClickListenerWithViewDelayEnabled {
            viewModel.handleEvent(LoginEvent.OnAuthButtonClick)
        }
    }

    private fun startSignInFlow() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        *
        *  +TO DO
        * Enable Google Sign in method
        *
        * Add a support email address to your project in project settings.
        * Open link https://console.firebase.google.com/
        *
        * */
        var userToken: String? = null
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                userToken = account.idToken
                viewModel.handleEvent(
                    LoginEvent.OnGoogleSignInResult(
                        LoginResult(requestCode, userToken)
                    )
                )
            }
        } catch (exception: Exception) {
            activity.makeToast(ERROR_SIGN_IN)
        }
    }
}
