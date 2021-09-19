package com.tchnodynamic.micmac

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tchnodynamic.micmac.databinding.ActivityBuyerRegistrationBinding

class BuyerRegistration : AppCompatActivity() {
    private lateinit var binding: ActivityBuyerRegistrationBinding
    val mAuth= FirebaseAuth.getInstance()

    val nameregex = "[a-zA-Z][a-zA-Z ]*".toRegex()
    val phoneregex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$".toRegex()
    val pincoderegex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$".toRegex()
    val addrregex = "^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F]*$".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // testing github



        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if (isConnected){
                binding.internetoff.visibility= View.GONE
                binding.interneton.visibility= View.VISIBLE

            }else{
                binding.internetoff.visibility= View.VISIBLE
                binding.interneton.visibility= View.GONE
            }

        })

        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }

        //Toolbar Logic Above

        binding.submitFormBuyer.setOnClickListener {
            registerbuyer()
        }
    }

    private fun registerbuyer() {

        val name=binding.buyername.text.toString()
        val contactnumber=binding.contactnumber.text.toString()
        val address=binding.buyeraddress.text.toString()
        val pincode=binding.buyerpincide.text.toString()


        when {

            TextUtils.isEmpty(name) -> Toast.makeText(this, "Your Name is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(contactnumber) -> Toast.makeText(this, "Your Phone is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(pincode) -> Toast.makeText(this, "Your Pincode is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(address) -> Toast.makeText(this, "Your Delivery Address is Empty", Toast.LENGTH_SHORT).show()


            (!name.matches(nameregex))-> {
                Toast.makeText(this, "WARNING: Enter a valid name !", Toast.LENGTH_SHORT).show() }

            (!contactnumber.matches(phoneregex))->{
                Toast.makeText(this, "WARNING: Enter a valid Number !", Toast.LENGTH_SHORT).show() }

//            (!address.matches(addrregex))->{
//                Toast.makeText(this, "WARNING: Enter Valid Address!", Toast.LENGTH_SHORT).show() }

            (!pincode.matches(pincoderegex))->{
                Toast.makeText(this, "WARNING: Enter Valid PinCode!", Toast.LENGTH_SHORT).show() }



            else -> {

                val sellerdialog = ProgressDialog(this)
                sellerdialog.setTitle("Updating User Account")
                sellerdialog.setMessage("Please wait,while we are checking the credentials..")
                sellerdialog.setCanceledOnTouchOutside(false)
                sellerdialog.show()

                var sid = mAuth.currentUser!!.uid
                val rootref = FirebaseDatabase.getInstance().reference.child("MicMac").child("Buyers")

                val sellermap = java.util.HashMap<String, Any>()

                sellermap.put("buyercontact", contactnumber)
                sellermap.put("buyerpincode", pincode)
                sellermap.put("buyeraddress", address)
                sellermap.put("buyername", name)
                sellermap.put("buyerselller", "seller1")
                sellermap.put("totalpurchaseditem", 0)
                sellermap.put("totalpurchasedamount", 0)

                rootref.child(sid).updateChildren(sellermap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Buyer Information Updated Successful", Toast.LENGTH_SHORT).show()
                            sellerdialog.dismiss()

                            val intent = Intent(this, AllProducts::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            task.exception?.let { regisexception ->
                                throw regisexception

                            }
                        }
                    }


            }
        }
    }
}






