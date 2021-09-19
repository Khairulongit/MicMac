package com.tchnodynamic.micmac.Seller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.micmac.Adapters.CustomerinfoAdapter
import com.tchnodynamic.micmac.Admin.AdminCategoryActivity
import com.tchnodynamic.micmac.Models.CustModel
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityCustomerStatsBinding

class CustomerStatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerStatsBinding
    
    
    private var custAdapter: CustomerinfoAdapter? = null
    private var custList: MutableList<CustModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar

        val textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Your All Customers!!"
        textView.gravity = Gravity.CENTER_HORIZONTAL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }
        
        
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.customerRecycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.customerRecycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        //Toolbar Logic Above


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.customerRecycleView)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        custList = ArrayList()
        custAdapter = this?.let { CustomerinfoAdapter(it, custList as ArrayList<CustModel>) }

        recyclerView!!.adapter = custAdapter


        if (Prevalent.UserType=="Seller")
        {
            getallinfocustomersellerspecific(Prevalent.Sellerid)
        }else
        getallinfocustomer()


    }

    private fun getallinfocustomersellerspecific(sellerid: String) {

        val allcustomer =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Buyers")
//                .child(
//                    FirebaseAuth.getInstance().currentUser!!.uid
//                )


//        orderList?.clear()


        allcustomer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                custList?.clear()

                for (customers in snapshot.children) {


                    val customer = customers.getValue(CustModel::class.java)


                    if (customer != null && customer.buyerselller==sellerid) {
                        custList!!.add(customer)
                    }
                    custAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }

    private fun getallinfocustomer() {

        val allcustomer =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Buyers")
//                .child(
//                    FirebaseAuth.getInstance().currentUser!!.uid
//                )


//        orderList?.clear()


        allcustomer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                custList?.clear()

                for (customers in snapshot.children) {

                    Log.wtf("customer",customers.toString())


                    val customer = customers.getValue(CustModel::class.java)


                    if (customer != null) {
                        custList!!.add(customer)
                    }
                    custAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }


    override fun onBackPressed() {


        val intent:Intent = if (Prevalent.UserType=="Seller") {
            Intent(this, SellerConsoleActivity::class.java)
        } else{
            Intent(this, AdminCategoryActivity::class.java)
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
