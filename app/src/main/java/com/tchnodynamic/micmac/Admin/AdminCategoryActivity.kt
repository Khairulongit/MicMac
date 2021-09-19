package com.tchnodynamic.micmac.Admin

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
import com.tchnodynamic.micmac.*
import com.tchnodynamic.micmac.Seller.CustomerStatsActivity
import com.tchnodynamic.micmac.databinding.ActivityAdminCategoryBinding
import io.paperdb.Paper

class AdminCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminCategoryBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAdminCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if (isConnected){
                binding.intermetoff.visibility= View.GONE
                binding.interneton.visibility= View.VISIBLE

            }else{
                binding.intermetoff.visibility= View.VISIBLE
                binding.interneton.visibility= View.GONE
            }

        })

        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text="Admin's Console"
        textView.foregroundGravity= Gravity.CENTER_HORIZONTAL
        textView.textSize= 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above


        binding.tshirt.setOnClickListener{
            val intent = Intent(applicationContext, SellerAddNewProduct::class.java)
            intent.putExtra("category","tshirt")
            startActivity(intent)
        }

        binding.laptop.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","laptop")
            startActivity(intent)
        }

        binding.sunglass.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","sunglass")
            startActivity(intent)
        }

        binding.watches.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","watches")
            startActivity(intent)
        }

        binding.swater.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","swater")
            startActivity(intent)
        }

        binding.hats.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","hats")
            startActivity(intent)
        }

        binding.femaleShirt.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","femaleShirt")
            startActivity(intent)
        }

        binding.headphones.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","headphones")
            startActivity(intent)
        }

        binding.mobiles.setOnClickListener{
            val intent = Intent(this, SellerAddNewProduct::class.java)
            intent.putExtra("category","mobiles")
            startActivity(intent)
        }


        binding.adminLogout.setOnClickListener {

            run {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    Paper.book().destroy()
                    val intent: Intent =Intent(this, LoginUser::class.java)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }
        }

        binding.chkOrdersBtn.setOnClickListener {
            val intent = Intent(this, MyOrdersActivity ::class.java)
            startActivity(intent)
        }


        binding.chkAllCustomer.setOnClickListener {
            val intent = Intent(this, CustomerStatsActivity ::class.java)
            startActivity(intent)
        }

        binding.addNewSeller.setOnClickListener {
            val intent = Intent(this, AddNewSeller::class.java)
            startActivity(intent)
        }

        binding.chkAllSellers.setOnClickListener {
            val intent = Intent(this, CheckSellersData ::class.java)
            startActivity(intent)
        }

        binding.modifyProductxBtn.setOnClickListener {
            val intent = Intent(this, AllProducts ::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit")
            builder.setMessage("Are You Sure?")
            builder.setPositiveButton("Yes") { dialog, which ->
                val intent: Intent =Intent(this, LoginUser::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            val alert = builder.create()
            alert.show()
        }
    }

}