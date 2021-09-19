package com.tchnodynamic.micmac.Seller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.tchnodynamic.micmac.Admin.AdminCategoryActivity
import com.tchnodynamic.micmac.Admin.SellerAddNewProduct
import com.tchnodynamic.micmac.LoginUser
import com.tchnodynamic.micmac.MyOrdersActivity
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityAdminCategoryBinding
import com.tchnodynamic.micmac.databinding.ActivitySellerConsoleBinding
import io.paperdb.Paper
import kotlin.system.exitProcess

class SellerConsoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerConsoleBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerConsoleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.intermetoff.visibility = View.GONE
                binding.interneton.visibility = View.VISIBLE

            } else {
                binding.intermetoff.visibility = View.VISIBLE
                binding.interneton.visibility = View.GONE
            }

        })

        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Welcome to MicMac"
        textView.foregroundGravity = Gravity.CENTER_HORIZONTAL
        textView.textSize = 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above


        binding.sellerLogout.setOnClickListener {
//            try {
//                Paper.book().destroy()
//            }
            run {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Exit")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    val intent =Intent(this, LoginUser::class.java)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }


        }

        binding.chkOrdersBtn.setOnClickListener {
            val intent = Intent(this, MyOrdersActivity::class.java)
            startActivity(intent)
        }

        binding.checkCustomerStatus.setOnClickListener {
            val intent = Intent(this, CustomerStatsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {

        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")
            builder.setPositiveButton("Yes") { dialog, which ->
                val intent =Intent(this, LoginUser::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }
    }
}

