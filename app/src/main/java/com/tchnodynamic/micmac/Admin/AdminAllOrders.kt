package com.tchnodynamic.micmac.Admin

import android.content.Intent
import android.graphics.Typeface
import androidx.lifecycle.Observer
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Adapters.MyOrdersAdapter
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.micmac.NetworkConnection
import com.tchnodynamic.micmac.R
import com.tchnodynamic.micmac.databinding.ActivityAdminAllOrdersBinding

class AdminAllOrders : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAllOrdersBinding
    private var orderAdapter: MyOrdersAdapter? = null
    private var orderlist: MutableList<OrderModel>? = null
    private var status: String? = null
    var ordstaus: String = ""


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAllOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.recyOrderList.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE

            } else {
                binding.recyOrderList.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE
            }

        })


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.foregroundGravity = Gravity.CENTER_HORIZONTAL
        textView.textSize = 15F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }

        binding.filteradmin.setOnClickListener {
            binding.filtereddata.visibility = View.VISIBLE
        }

        binding.allOrders.setOnClickListener {
            binding.allOrders.setTypeface(binding.allOrders.typeface, Typeface.BOLD)
            getspeOrders(textView)

        }


        binding.pendingOrders.setOnClickListener {
            binding.pendingOrders.setTypeface(binding.pendingOrders.typeface, Typeface.BOLD)
            binding.confirmOrders.setTypeface(binding.confirmOrders.typeface, Typeface.NORMAL)
            binding.cancelOrders.setTypeface(binding.cancelOrders.typeface, Typeface.NORMAL)
            binding.delievrOrders.setTypeface(binding.delievrOrders.typeface, Typeface.NORMAL)
            binding.allOrders.setTypeface(binding.allOrders.typeface, Typeface.NORMAL)
            getspecificorders("Pending Approval")
        }
        binding.confirmOrders.setOnClickListener {
            binding.confirmOrders.setTypeface(binding.confirmOrders.typeface, Typeface.BOLD)
            binding.pendingOrders.setTypeface(binding.pendingOrders.typeface, Typeface.NORMAL)
            binding.cancelOrders.setTypeface(binding.cancelOrders.typeface, Typeface.NORMAL)
            binding.delievrOrders.setTypeface(binding.delievrOrders.typeface, Typeface.NORMAL)
            binding.allOrders.setTypeface(binding.allOrders.typeface, Typeface.NORMAL)
            getspecificorders("CONFIRMED")
        }
        binding.cancelOrders.setOnClickListener {
            binding.cancelOrders.setTypeface(binding.cancelOrders.typeface, Typeface.BOLD)
            binding.confirmOrders.setTypeface(binding.confirmOrders.typeface, Typeface.NORMAL)
            binding.pendingOrders.setTypeface(binding.pendingOrders.typeface, Typeface.NORMAL)
            binding.delievrOrders.setTypeface(binding.delievrOrders.typeface, Typeface.NORMAL)
            binding.allOrders.setTypeface(binding.allOrders.typeface, Typeface.NORMAL)
            getspecificorders("CANCELLED")
        }

        binding.delievrOrders.setOnClickListener {
            binding.delievrOrders.setTypeface(binding.delievrOrders.typeface, Typeface.BOLD)
            binding.cancelOrders.setTypeface(binding.cancelOrders.typeface, Typeface.NORMAL)
            binding.confirmOrders.setTypeface(binding.confirmOrders.typeface, Typeface.NORMAL)
            binding.pendingOrders.setTypeface(binding.pendingOrders.typeface, Typeface.NORMAL)
            binding.allOrders.setTypeface(binding.allOrders.typeface, Typeface.NORMAL)
            getspecificorders("DELIVERED")
        }


        // Toolbar Logic below


        //Toolbar Logic Above


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new post at top
        linearLayoutManager.stackFromEnd = true

        binding.recyOrderList.layoutManager = linearLayoutManager

        status = intent.getStringExtra("status").toString()



        orderlist = ArrayList()
        orderAdapter = this?.let { MyOrdersAdapter(it, orderlist as ArrayList<OrderModel>) }

        binding.recyOrderList!!.adapter = orderAdapter

//        if(status!=null){
        getspeOrders(textView)

//        }else
//            getOrders()


    }

    private fun getspecificorders(state: String) {


        val Orderref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")

        Orderref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderlist?.clear()

                for (orderValue in snapshot.children) {

                    val order = orderValue.getValue(OrderModel::class.java)
                    order?.orderid = orderValue.key.toString()

                    Log.wtf("ordervaluefinal",orderValue.key.toString())


                    val Orderbyseller =
                        FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                            .child(orderValue.key.toString())


                    Orderbyseller.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ordervaluefinal in snapshot.children) {


                                val product = ordervaluefinal.getValue(OrderModel::class.java)


                                if (product != null && state==product.state) {
                                    orderlist?.add(product)
                                }
                                orderAdapter!!.notifyDataSetChanged()

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }


                    })
                }


            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun getspeOrders(textView: TextView) {


        when (status) {
            "canorder" -> {
                status = "Cancelled"
                binding.orderstypr.text = "Cancelled Orders"
                textView.text = "Your Cancelled Orders"


            }
            "penorder" -> {
                status = "Approved"
                binding.orderstypr.text = "Pending Delivery"

                textView.text = "Your Pending Delivery Orders"

            }
            "delorder" -> {
                status = "Delivered"
                binding.orderstypr.text = "Delivered Orders"
                textView.text = "Your Delivered Orders"


            }
            else -> {
                status = "Pending Approval"
                binding.orderstypr.text = "New Orders"
                textView.text = "Your Pending Approval Orders"


            }
        }


        val Orderref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")

        Orderref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderlist?.clear()

                for (orderValue in snapshot.children) {

                    val order = orderValue.getValue(OrderModel::class.java)
                    order?.orderid = orderValue.key.toString()

                    Log.wtf("ordervaluefinal",orderValue.key.toString())


                    val Orderbyseller =
                        FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                            .child(orderValue.key.toString())


                    Orderbyseller.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ordervaluefinal in snapshot.children) {


                                val product = ordervaluefinal.getValue(OrderModel::class.java)


                                if (product != null) {
                                    orderlist?.add(product)
                                }
                                orderAdapter!!.notifyDataSetChanged()

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }


                    })
                }


            }


            override fun onCancelled(error: DatabaseError) {
            }
        })


    }


    override fun onBackPressed() {

        val intent = Intent(this, AdminCategoryActivity::class.java)
        startActivity(intent)
        finish()
    }
}


