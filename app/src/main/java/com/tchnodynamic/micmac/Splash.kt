package com.tchnodynamic.micmac

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tchnodynamic.micmac.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val networkConnection = NetworkConnection(applicationContext)
//        networkConnection.observe(this, Observer { isConnected ->
//            if (isConnected) {
//
//                binding.groupinvisible.visibility = View.GONE
//                binding.groupvisible.visibility = View.VISIBLE
//            } else {
//                binding.groupinvisible.visibility = View.GONE
//                binding.groupvisible.visibility = View.VISIBLE
//
//            }
//
//        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.white, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.white)
        }

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@Splash, LoginUser::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }
}



