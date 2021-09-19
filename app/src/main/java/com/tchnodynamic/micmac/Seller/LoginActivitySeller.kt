package com.tchnodynamic.micmac.Seller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.micmac.Admin.AdminCategoryActivity
import com.tchnodynamic.micmac.LoginUser
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.Prevalent.Companion.Sellerid
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityLoginAdminBinding
import com.tchnodynamic.micmac.databinding.ActivityLoginSellerBinding

class LoginActivitySeller : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSellerBinding
    var database = FirebaseDatabase.getInstance()
    var pincode: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Prevalent.UserType = "Seller"

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


//        database.getReference().child("Admin").child("Admin1").get().
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.status)
        }



        binding.btnSigninseller.setOnClickListener {

            var userID  = binding.useridseller.text.toString()

            val pincoderef =
                database.reference.child("MicMac").child("Sellers").child(userID)
            pincoderef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.wtf("dataSnapshot", dataSnapshot.value.toString())
                    pincode = dataSnapshot.value.toString()
                    Log.wtf("pincode", pincode)

                    if (binding.emaillogin.text.toString() == pincode) {
                        Prevalent.Sellerid=userID
                        val intent = Intent(applicationContext, SellerConsoleActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Invalid Pin", Toast.LENGTH_SHORT).show()
                        return
                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        }


    }


    override fun onBackPressed() {

        val intent = Intent(this, LoginUser::class.java)
        startActivity(intent)
        finish()
    }
}

