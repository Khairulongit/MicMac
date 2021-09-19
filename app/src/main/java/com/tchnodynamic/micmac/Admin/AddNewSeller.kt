package com.tchnodynamic.micmac.Admin

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
import com.tchnodynamic.micmac.AllProducts
import com.tchnodynamic.micmac.LoginUser
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityAddNewSellerBinding
import com.tchnodynamic.micmac.databinding.ActivityBuyerRegistrationBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNewSeller : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewSellerBinding
    val mAuth= FirebaseAuth.getInstance()

    val nameregex = "[a-zA-Z][a-zA-Z ]*".toRegex()
    val phoneregex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$".toRegex()
    val pincoderegex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$".toRegex()
    val addrregex = "^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F]*$".toRegex()

    private var Savecurrentdate: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewSellerBinding.inflate(layoutInflater)
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

        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }

        //Toolbar Logic Above

        binding.submitFormAddSeller.setOnClickListener {
            registerseller()
        }
    }

    private fun registerseller() {

        val name=binding.sellername.text.toString()
        val sellerid=binding.sellerid.text.toString()
        val sellerpin=binding.sellerpwd.text.toString()
        val contactnumber=binding.sellercontact.text.toString()
        val address=binding.selleraddress.text.toString()


        when {

            TextUtils.isEmpty(name) -> Toast.makeText(this, "Seller Name is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(contactnumber) -> Toast.makeText(this, "Seller Phone is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(sellerpin) -> Toast.makeText(this, "Seller PIN Password Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(address) -> Toast.makeText(this, "Seller Address is Empty", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(sellerid) -> Toast.makeText(this, "Seller ID is Empty", Toast.LENGTH_SHORT).show()


            (!name.matches(nameregex))-> {
                Toast.makeText(this, "WARNING: Enter a valid name !", Toast.LENGTH_SHORT).show() }

            (!contactnumber.matches(phoneregex))->{
                Toast.makeText(this, "WARNING: Enter a valid Number !", Toast.LENGTH_SHORT).show() }

//            (!address.matches(addrregex))->{
//                Toast.makeText(this, "WARNING: Enter Valid Address!", Toast.LENGTH_SHORT).show() }

//            (!sellerpin.matches(pincoderegex))->{
//                Toast.makeText(this, "WARNING: Enter Valid Password PIN!", Toast.LENGTH_SHORT).show() }



            else -> {

                val sellerdialog = ProgressDialog(this)
                sellerdialog.setTitle("Creating New Seller")
                sellerdialog.setMessage("Please wait,while we are Creating  the Seller..")
                sellerdialog.setCanceledOnTouchOutside(false)
                sellerdialog.show()

                val rootref = FirebaseDatabase.getInstance().reference.child("MicMac").child("Sellers")

                val sellermap = java.util.HashMap<String, Any>()
                var calendar: Calendar = Calendar.getInstance()
                var currentdate = SimpleDateFormat("MMM dd,yyyy")
                Savecurrentdate = currentdate.format(calendar.time)


                sellermap.put("sellername", name)
                sellermap.put("sellerid", sellerid)
                sellermap.put("sellerpin", sellerpin)
                sellermap.put("sellercontact", contactnumber)
                sellermap.put("selleraddress", address)
                sellermap.put("sellertotuser", "0")
                sellermap.put("sellertotorder", "0")
                sellermap.put("sellertotalamunt", "0")
                sellermap.put("sellerregisdate", Savecurrentdate)

                rootref.child(sellerid).updateChildren(sellermap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Seller Information Updated Successful", Toast.LENGTH_SHORT).show()
                            sellerdialog.dismiss()

                            val intent = Intent(this, AdminCategoryActivity::class.java)
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

    override fun onBackPressed() {

        val intent = Intent(this, AdminCategoryActivity::class.java)
        startActivity(intent)
        finish()
    }
}






