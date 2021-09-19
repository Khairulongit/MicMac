package com.tchnodynamic.micmac.Admin

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
import com.tchnodynamic.micmac.Adapters.SellerInfoAdapter
import com.tchnodynamic.micmac.Models.CustModel
import com.tchnodynamic.micmac.Models.SellerModel
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.Seller.SellerConsoleActivity
import com.tchnodynamic.micmac.databinding.ActivityCheckSellersDataBinding
import com.tchnodynamic.micmac.databinding.ActivityCustomerStatsBinding

class CheckSellersData : AppCompatActivity() {
    private lateinit var binding: ActivityCheckSellersDataBinding


    private var sellerAdapter: SellerInfoAdapter? = null
    private var sellerList: MutableList<SellerModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckSellersDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar

        val textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Your All Sellers!!"
        textView.gravity = Gravity.CENTER_HORIZONTAL

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.sellerRecycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.sellerRecycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        //Toolbar Logic Above


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.sellerRecycleView)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        sellerList = ArrayList()
        sellerAdapter = this?.let { SellerInfoAdapter(it, sellerList as ArrayList<SellerModel>) }

        recyclerView!!.adapter = sellerAdapter


        
        getallsellers()


    }


    private fun getallsellers() {

        val allsellers =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Sellers")

        

        allsellers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sellerList?.clear()

                for (sellers in snapshot.children) {

                    val selleromer = sellers.getValue(SellerModel::class.java)


                    if (selleromer != null) {
                        sellerList!!.add(selleromer)
                    }
                    sellerAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }


    override fun onBackPressed() {


        val intent: Intent = if (Prevalent.UserType=="Seller") {
            Intent(this, SellerConsoleActivity::class.java)
        } else{
            Intent(this, AdminCategoryActivity::class.java)
        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
