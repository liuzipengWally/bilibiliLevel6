package com.bilibililevel6.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bilibililevel6.R
import com.bilibililevel6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}