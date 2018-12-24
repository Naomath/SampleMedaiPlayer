package com.sample.samplemusicplayer

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener


class MainActivity : AppCompatActivity() {

    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler: Handler = Handler()

        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.sample_music)

        mp.setOnPreparedListener {
            seekBar.max = mp.duration
            mp.start()
            playCycle(mp, handler)
        }

        play.setOnClickListener {
            mp.start()
        }

        stop.setOnClickListener {
            mp.pause()
        }

        seekBar.max = mp.duration

        seekBar.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int, fromUser: Boolean
                ) {
                    //ツマミをドラッグした時に呼ばれる

                    //fromUserはユーザーの操作によって変えられた場合にtrue
                    if (fromUser) {
                        mp.seekTo(progress)
                        //seekTo()での引数はミリ秒なので直接progressをとってきてでよき
                    }
                }


                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    // ツマミに触れたときに呼ばれる
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // ツマミを離したときに呼ばれる
                }
            }
        )


    }

    fun playCycle(mp: MediaPlayer, handler: Handler) {
        seekBar.setProgress(mp.currentPosition)

        if (mp.isPlaying) {
            runnable = object : Runnable {
                override fun run() {
                    playCycle(mp, handler)
                }
            }
        }

        //多分ここで1sec遅らせることで１秒ごとに進むようになるはず
        handler.postDelayed(runnable, 1000)
    }

}
