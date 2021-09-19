package com.tchnodynamic.micmac.Admin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityConfirmFinalOrdersBinding
import androidx.lifecycle.Observer
import com.tchnodynamic.micmac.AllProducts
import com.tchnodynamic.micmac.Prevalent


class ConfirmFinalOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmFinalOrdersBinding

    var orderid: String = ""
    var buyerid: String = ""
    var ostatus: String = ""
    var productid: String = ""
    var prdamount: String = ""
    var producquantity: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmFinalOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.interneton.visibility = View.VISIBLE
                binding.nointenetlayout.visibility = View.GONE

            } else {
                binding.interneton.visibility = View.GONE
                binding.nointenetlayout.visibility = View.VISIBLE
            }

        })


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Order Info"
        textView.foregroundGravity = Gravity.CENTER_HORIZONTAL
        textView.textSize = 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        binding.adminFinalProductDate.isEnabled = false
        binding.adminFinalProductTime.isEnabled = false
        binding.adminFinalProductDetailsName.isEnabled = false
        binding.adminFinalProductQuantity.isEnabled = false
        binding.adminFinalProductPrice.isEnabled = false
        binding.adminFinalProductAddress.isEnabled = false



        orderid = intent.getStringExtra("oid").toString()
        buyerid = intent.getStringExtra("bid").toString()
        ostatus = intent.getStringExtra("ostatus").toString()
        productid = intent.getStringExtra("pid").toString()
        prdamount = intent.getStringExtra("prdamount").toString()
        producquantity = intent.getStringExtra("pquantity").toString()




        Log.wtf("ostatus", ostatus)


        getproductdetails(orderid)


        if (Prevalent.UserType == "Seller") {

            binding.adminDeliveryConfirm.visibility = View.VISIBLE
            binding.adminFinalConfirm.visibility = View.VISIBLE
            binding.adminFinalNotconfirm.visibility = View.VISIBLE
        }


        when (ostatus) {

            "Pending Approval" -> {
                binding.adminDeliveryConfirm.visibility = View.INVISIBLE
            }
            "CONFIRMED" -> {
                binding.adminFinalNotconfirm.visibility = View.INVISIBLE
                binding.adminFinalConfirm.visibility = View.INVISIBLE
            }
            "CANCELLED" -> {
                binding.adminDeliveryConfirm.visibility = View.INVISIBLE
                binding.adminFinalNotconfirm.visibility = View.INVISIBLE
                binding.adminFinalConfirm.visibility = View.INVISIBLE
            }

            else -> {
                binding.adminFinalNotconfirm.visibility = View.INVISIBLE
                binding.adminFinalConfirm.visibility = View.INVISIBLE
                binding.adminDeliveryConfirm.visibility = View.INVISIBLE

            }
        }


        binding.adminDeliveryConfirm.setOnClickListener {


            val sellerref =  FirebaseDatabase.getInstance().reference.child("MicMac").child("Sellers")
                .child(Prevalent.Sellerid)

            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(buyerid).child(orderid).child("state").setValue("DELIVERED")

            sellerref.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalorderamt = snapshot.child("sellertotalamunt").value.toString().toInt()
                    totalorderamt += prdamount.toInt()
                    sellerref.child("sellertotalamunt").setValue(totalorderamt.toString())

                    var totalorderqnty = snapshot.child("sellertotorder").value.toString().toInt()
                    totalorderqnty += producquantity.toInt()
                    sellerref.child("sellertotorder").setValue(totalorderqnty.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            val intent: Intent = if (Prevalent.UserType == "Seller") {
                Intent(this, AdminAllOrders::class.java)
            } else {
                Intent(this, AdminAllOrders::class.java)
            }
            startActivity(intent)
            finish()
        }

        binding.adminFinalConfirm.setOnClickListener {

            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(buyerid).child(orderid).child("state").setValue("CONFIRMED")
            val intent: Intent = if (Prevalent.UserType == "Seller") {
                Intent(this, AdminAllOrders::class.java)
            } else {
                Intent(this, AdminAllOrders::class.java)
            }
            startActivity(intent)
            finish()


        }



        binding.adminFinalNotconfirm.setOnClickListener {


            val productref =  FirebaseDatabase.getInstance().reference.child("MicMac").child("Products")
                .child("adminaddprd")

            productref.child(productid).addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalavailable = snapshot.child("availability").value.toString().toInt()

                    totalavailable += producquantity.toInt()


                    productref.child(productid).child("availability").setValue(totalavailable.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(buyerid).child(orderid).child("state").setValue("CANCELLED")
            val intent: Intent = if (Prevalent.UserType == "Seller") {
                Intent(this, AdminAllOrders::class.java)
            } else {
                Intent(this, AdminAllOrders::class.java)
            }
            startActivity(intent)
            finish()

        }
    }


    private fun getproductdetails(orderid: String) {

        val Productref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(buyerid)
        Productref.child(orderid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    val product: OrderModel? = snapshot.getValue(OrderModel::class.java)



                    Picasso.get().load(product?.productimg).placeholder(R.drawable.profile)
                        .into(binding.adminFinalProductImageDeytails)
                    binding.adminFinalProductDate.setText(product?.date)
                    binding.adminFinalProductTime.setText(product?.time)
                    binding.adminFinalProductDetailsName.setText(product?.productname)
                    binding.adminFinalProductQuantity.setText("Ordered Quantity :"+product?.quantity)
                    binding.adminFinalProductPrice.setText(product?.totalamount)
                    binding.adminFinalProductAddress.setText(product?.address)

                }


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

    override fun onBackPressed() {


        val intent: Intent = if (Prevalent.UserType == "Seller") {
            Intent(this, AdminAllOrders::class.java)

        } else {
            Intent(this, AllProducts::class.java)
//            Log.wtf("islam")
        }
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}








