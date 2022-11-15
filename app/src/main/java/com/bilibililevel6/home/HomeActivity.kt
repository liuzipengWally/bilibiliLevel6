package com.bilibililevel6.home

import android.content.Intent
import android.os.Bundle
import com.bilibililevel6.BaseActivity
import com.bilibililevel6.databinding.ActivityHomeBinding
import com.bilibililevel6.login.LoginActivity

class HomeActivity : BaseActivity() {
    private val viewBinding by lazy {
        ActivityHomeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.LoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}