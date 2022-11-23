package com.bilibililevel6.play

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bilibililevel6.R

class PlayVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, PlayVideoActivity::class.java))
        }
    }
}