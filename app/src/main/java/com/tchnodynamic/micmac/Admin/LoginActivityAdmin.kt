package com.tchnodynamic.micmac.Admin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.micmac.LoginUser
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityLoginAdminBinding

class LoginActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginAdminBinding
    var database = FirebaseDatabase.getInstance()
    var pincode: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Prevalent.UserType = "Admin"


//        database.getReference().child("Admin").child("Admin1").get().
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.status)
        }

        var value =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Admin Login")
                .orderByChild("pin").equals(712149)
        Log.wtf("islam", value.toString())






        binding.btnSignin.setOnClickListener {

            var value =
                FirebaseDatabase.getInstance().reference.child("MicMac").child("Admin Login")
                    .orderByChild("pin").equals("712149")
            Log.wtf("islam", value.toString())

            val pincoderef =
                database.reference.child("MicMac").child("Admin").child("pin")
            pincoderef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.wtf("dataSnapshot", dataSnapshot.value.toString())
                    pincode = dataSnapshot.value.toString()
                    Log.wtf("pincode", pincode)

                    if (binding.pwdlogin.text.toString() == pincode) {
                        val intent = Intent(applicationContext, AdminCategoryActivity::class.java)
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

