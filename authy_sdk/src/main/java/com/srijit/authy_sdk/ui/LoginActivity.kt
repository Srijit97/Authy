package com.srijit.authy_sdk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.powerspinner.IconSpinnerItem
import com.srijit.authy_sdk.R
import com.srijit.authy_sdk.databinding.ActivityLoginBinding
import com.srijit.authy_sdk.utils.AuthResult
import com.srijit.authy_sdk.utils.Authy
import com.srijit.authy_sdk.utils.LoginResult
import com.srijit.authy_sdk.utils.showToast

internal class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    var authy: Authy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.model = viewModel
        binding.lifecycleOwner = this

        setCallback()
        setOnClicks()
        initSpinner()
        setObservers()
    }

    private fun setCallback() {
        authy = intent.getParcelableExtra("authy") as? Authy
    }

    private fun initSpinner() {
        binding.spinnerView.apply {
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, _, selectedItem ->
                if (selectedItem.text == "Doctor")
                    viewModel.setUserType(isDoctorUserType = true)
                else
                    viewModel.setUserType(isDoctorUserType = false)
            }
            setSpinnerAdapter(SpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(
                        text = "Patient",
                        iconRes = R.drawable.user,
                        textColor = resources.getColor(R.color.white, null)
                    ),
                    IconSpinnerItem(
                        text = "Doctor",
                        iconRes = R.drawable.doctor,
                        textColor = resources.getColor(R.color.white, null)
                    )
                )
            )
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)
            lifecycleOwner = this@LoginActivity
        }
    }

    private fun setObservers() {
        viewModel.authenticationSuccessful.observe(this) {
            when (it) {
                is LoginResult.LoginError -> {
                    authy?.authResult?.invoke(AuthResult.LoginError)
                    showToast(it.errorMessage)
                }
                is LoginResult.LoginSuccessful -> {
                    authy?.authResult?.invoke(AuthResult.LoginSuccess(user = it.user,it.userLoginStatus))
                    showToast("Authentication successful")
                    finish()
                }
                is LoginResult.NoResult -> Unit
            }
        }
    }

    private fun setOnClicks() {
        binding.tvLogin.setOnClickListener {
            viewModel.setFlow(isLogin = true)
        }
        binding.tvSignUp.setOnClickListener {
            viewModel.setFlow(isLogin = false)
        }
        binding.ivGoogle.setOnClickListener {
            showToast("Feature not yet built")
        }
        binding.ivGoogle.setOnClickListener {
            showToast("Feature not yet built")
        }
        binding.ivGoogle.setOnClickListener {
            showToast("Feature not yet built")
        }
        binding.btPerformAuth.setOnClickListener {
            if (binding.spinnerView.selectedIndex == -1) {
                showToast("Please select user type")
                return@setOnClickListener
            }
            viewModel.authenticateWithEmailAndPassword()
        }

    }
}