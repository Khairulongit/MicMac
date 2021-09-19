package com.tchnodynamic.micmac

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.tchnodynamic.micmac.databinding.ActivityTechnodynamicBinding

class Technodynamic : AppCompatActivity() {
    private lateinit var binding: ActivityTechnodynamicBinding


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityTechnodynamicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var textView = findViewById<TextView>(R.id.toolbartext)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.status)
        }


        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        binding.googleplay.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Thanks for Smart Thinking!", Toast.LENGTH_SHORT)
                .show()
            val i = Intent(Intent.ACTION_VIEW)
            //i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.technodynamics.technodynamicapp"));
            i.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.technodynamic.technodynamiclanding")
            startActivity(i)
        })
        binding.googleplay.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Thanks for Smart Thinking!", Toast.LENGTH_SHORT)
                .show()
            val i = Intent(Intent.ACTION_VIEW)
            i.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.technodynamic.technodynamiclanding")
            startActivity(i)
        })
        //return googlePlayStore;
    }
}


