package com.bilibililevel6.play

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
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
                    ),
                    FRAGMENT_TAG
                ).commit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as PlayerFragment).seekBack()
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as PlayerFragment).seekForward()
            }
            KeyEvent.KEYCODE_ENTER,
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as PlayerFragment).playOrPause()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val FRAGMENT_TAG = "PlayerFragment"

        fun start(context: Context, aid: Int, cid: Int, bvid: String) {
            context.startActivity(Intent(context, PlayVideoActivity::class.java).apply {
                putExtra(PlayerFragment.AVID, aid)
                putExtra(PlayerFragment.CID, cid)
                putExtra(PlayerFragment.BVID, bvid)
            })
        }
    }
}