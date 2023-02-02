package com.bilibililevel6.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bilibililevel6.R
import com.bilibililevel6.databinding.ActivityLoginBinding
import com.bilibililevel6.extensions.dataStore
import com.bilibililevel6.extensions.launchObserve
import com.bilibililevel6.extensions.observeState
import com.bilibililevel6.extensions.showToast
import com.bilibililevel6.login.state.LoginUiEvent
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.flow.filter
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private var isGenerateQrCodeFailed = false
    private var timerTask: TimerTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewModel.generateQrCode()
        initObserver()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                viewModel.generateQrCode()
                viewBinding.maskView.visibility = View.GONE
                viewBinding.resultIconView.visibility = View.GONE
                viewBinding.loginHintText.text = getString(R.string.qr_code_hint)
                isGenerateQrCodeFailed = false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initObserver() = viewModel.apply {
        launchObserve {
            loginUiState.observeState { it.qrCodeLink }.filter { it.isNotEmpty() }.collect {
                try {
                    val barcodeEncoder = BarcodeEncoder()
                    val bitmap =
                        barcodeEncoder.encodeBitmap(
                            it,
                            BarcodeFormat.QR_CODE,
                            400,
                            400
                        )
                    viewBinding.qrCodeView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        launchObserve {
            loginUiState.observeState { it.qrCodeKey }.filter { it.isNotEmpty() }
                .collect { qrCodeKey ->
                    this@LoginActivity.dataStore.updateData { it.copy(qrCodeKey = qrCodeKey) }
                    timerTask = object : TimerTask() {
                        override fun run() {
                            login(qrCodeKey)
                        }
                    }
                    timer.schedule(timerTask, 100, 1000)
                }
        }

        launchObserve {
            loginUiEvent.collect {
                filterEvent(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask?.cancel()
        timerTask = null
    }

    private suspend fun filterEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.GenerateQrCodeFailed -> {
                viewBinding.maskView.visibility = View.VISIBLE
                viewBinding.resultIconView.visibility = View.VISIBLE
                viewBinding.loginHintText.text = getString(R.string.get_qr_code_failed)
                isGenerateQrCodeFailed = true
            }
            is LoginUiEvent.QrCodeExpire -> {
                viewBinding.maskView.visibility = View.VISIBLE
                viewBinding.resultIconView.visibility = View.VISIBLE
                viewBinding.loginHintText.text = getString(R.string.qr_code_expire_hint)
                isGenerateQrCodeFailed = true
            }
            is LoginUiEvent.ShowToast -> showToast(uiEvent.msg)
            is LoginUiEvent.LoginSuccess -> {
                dataStore.updateData {
                    it.copy(refreshToken = uiEvent.refreshToken, isLogged = true)
                }
                finish()
            }
            LoginUiEvent.QrCodeNotConfirm -> {
                viewBinding.maskView.visibility = View.VISIBLE
                viewBinding.resultIconView.visibility = View.VISIBLE
                viewBinding.resultIconView.setImageResource(R.drawable.ic_done_24)
                viewBinding.loginHintText.text = getString(R.string.login_confirm_hint)
            }
        }
    }

    companion object {
        private val timer = Timer("login")
    }
}