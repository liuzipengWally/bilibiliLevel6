package com.bilibililevel6.play

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bilibililevel6.R
import com.bilibililevel6.home.popular.PopularFragment
import java.text.Bidi

class PlayVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        intent.apply {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    PlayerFragment.newInstance(
                        getIntExtra(PlayerFragment.AVID, 0),
                        getIntExtra(PlayerFragment.CID, 0),
                        getStringExtra(PlayerFragment.BVID) ?: ""
                    )
                ).commit()
        }
    }

    companion object {
        fun start(context: Context, aid: Int, cid: Int, bvid: String) {
            context.startActivity(Intent(context, PlayVideoActivity::class.java).apply {
                putExtra(PlayerFragment.AVID, aid)
                putExtra(PlayerFragment.CID, cid)
                putExtra(PlayerFragment.BVID, bvid)
            })
        }
    }
}