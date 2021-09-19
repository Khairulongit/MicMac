package com.tchnodynamic.micmac.Admin

import android.content.Intent
import android.os.Build
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.tchnodynamic.micmac.R
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.Models.ProductModel
import com.tchnodynamic.micmac.AllProducts
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.databinding.ActivityModifyProductsAdminBinding
import java.text.SimpleDateFormat
import java.util.*

class ModifyProductsAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityModifyProductsAdminBinding
    var productId: String = ""
    private var productref: DatabaseReference? = null
    private var Savecurrentdate: String = ""
    private var Savecurrenttime: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModifyProductsAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.internetoff.visibility = View.GONE
                binding.interneton.visibility = View.VISIBLE

            } else {
                binding.internetoff.visibility = View.VISIBLE
                binding.interneton.visibility = View.GONE
            }

        })

        // Toolbar Logic below

//        val toolbar = findViewById<Toolbar>(com.google.firebase.database.R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        val textView = findViewById<TextView>(com.google.firebase.database.R.id.toolbartext)

//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        productId = intent.getStringExtra("pid").toString()


//        productref= FirebaseDatabase.getInstance().reference.child("MicMac").child("Products").child(FirebaseAuth.getInstance().currentUser!!.uid).child(productId)
        productref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Products")
                .child("adminaddprd").child(productId)

        displaytheproduct()

        binding.modifyProduct.setOnClickListener {
            applychanges()
        }

        binding.deleteProduct.setOnClickListener {
            deleteproduct()
        }


    }

    private fun deleteproduct() {

        productref?.removeValue()?.addOnCompleteListener {
            if (it.isSuccessful) {

                val intent = Intent(this, AdminCategoryActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "This product is deleted", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun applychanges() {

        val pname = binding.modifyProductDetailsName.text.toString()
        val pPrice = binding.modifyProductDetailsPrice.text.toString()
        val pdescriptiion = binding.modifyProductDetailsDesc.text.toString()
        val pavailable = binding.modifyProductDetailsquantity.text.toString()

        when {
            TextUtils.isEmpty(pname) -> Toast.makeText(
                this,
                "Product Name is Empty",
                Toast.LENGTH_SHORT
            )
            TextUtils.isEmpty(pPrice) -> Toast.makeText(
                this,
                "Product Price is Empty",
                Toast.LENGTH_SHORT
            )
            TextUtils.isEmpty(pPrice) -> Toast.makeText(
                this,
                "Product Description is Empty",
                Toast.LENGTH_SHORT
            )

            TextUtils.isEmpty(pavailable) -> Toast.makeText(
                this,
                "Product Quantity is Empty",
                Toast.LENGTH_SHORT
            )

            else -> {

                var calendar: Calendar = Calendar.getInstance()

                var currentdate = SimpleDateFormat("MMM dd,yyyy")
                Savecurrentdate = currentdate.format(calendar.time)

                var currenttime = SimpleDateFormat("HH:mm:ss a")
                Savecurrenttime = currenttime.format(calendar.time)

                val updatepostmap = HashMap<String, Any>()
//                updatepostmap["postid"] = postId!!
                updatepostmap["date"] = Savecurrentdate
                updatepostmap["time"] = Savecurrenttime
                updatepostmap["availability"] = pavailable
                updatepostmap["description"] = pdescriptiion
//                updatepostmap["productimg"] = myUri
//                updatepostmap["category"] = categoryName
                updatepostmap["price"] = pPrice
                updatepostmap["productname"] = pname

                productref?.updateChildren(updatepostmap)?.addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Changes Applied Successfully KI",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, AdminCategoryActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        task.exception?.let {
                            throw it

                        }
                    }


//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                progressDialog.dismiss()


                }
            }


        }
    }


    private fun displaytheproduct() {

        val Productref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Products").child(
//            FirebaseAuth.getInstance().currentUser!!.uid).child(productId)
//                "CcXrsQnCmka1OKm9r5HBehJrdqq1"
                "adminaddprd"
            ).child(productId)
        Productref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val prodduct = snapshot.getValue<ProductModel>(ProductModel::class.java)
                    Picasso.get().load(prodduct!!.productimg).fit()
                        .placeholder(R.drawable.sampleprd)
                        .into(binding.modifyProductImageDeytails)
                    binding.modifyProductDetailsName.setText(prodduct!!.productname)
                    binding.modifyProductDetailsDesc.setText(prodduct.description)
                    binding.modifyProductDetailsPrice.setText(prodduct.price)
                    binding.modifyProductDetailsquantity.setText(prodduct.availability)

//                    Picasso.get().load(prodduct.productimg).into(binding.modifyProductImageDeytails)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, AllProducts::class.java)
        startActivity(intent)
        finish()
    }
}
