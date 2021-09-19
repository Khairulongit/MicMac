package com.tchnodynamic.micmac

import android.content.Intent
import android.graphics.Typeface
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Adapters.MyOrdersAdapter
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.micmac.databinding.ActivityMyOrdersBinding

class MyOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyOrdersBinding

    private var myOrdersAdapter: MyOrdersAdapter? = null
    private var orderList: MutableList<OrderModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.wtf("KISLAM", "MyOrdersActivity")


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Your Current Orders!!"
        textView.gravity = Gravity.CENTER_HORIZONTAL


        binding.addnew.setOnClickListener {
            binding.filtereddata1.visibility = View.VISIBLE

        }



        binding.allOrders1.setOnClickListener {
            binding.allOrders1.setTypeface(binding.allOrders1.typeface, Typeface.BOLD)
            getmyordersbuyer()

        }




        binding.pendingOrders1.setOnClickListener {
            binding.pendingOrders1.setTypeface(binding.pendingOrders1.typeface, Typeface.BOLD)
            binding.confirmOrders1.setTypeface(binding.confirmOrders1.typeface, Typeface.NORMAL)
            binding.cancelOrders1.setTypeface(binding.cancelOrders1.typeface, Typeface.NORMAL)
            binding.delievrOrders1.setTypeface(binding.delievrOrders1.typeface, Typeface.NORMAL)
            binding.allOrders1.setTypeface(binding.allOrders1.typeface, Typeface.NORMAL)
            getspecificorders("Pending Approval")
        }

        binding.confirmOrders1.setOnClickListener {

            binding.filtereddata1.visibility = View.GONE
            binding.confirmOrders1.setTypeface(binding.confirmOrders1.typeface, Typeface.BOLD)
            binding.pendingOrders1.setTypeface(binding.pendingOrders1.typeface, Typeface.NORMAL)
            binding.cancelOrders1.setTypeface(binding.cancelOrders1.typeface, Typeface.NORMAL)
            binding.delievrOrders1.setTypeface(binding.delievrOrders1.typeface, Typeface.NORMAL)
            binding.allOrders1.setTypeface(binding.allOrders1.typeface, Typeface.NORMAL)
            getspecificorders("CONFIRMED")
        }
        binding.cancelOrders1.setOnClickListener {
            binding.cancelOrders1.setTypeface(binding.cancelOrders1.typeface, Typeface.BOLD)
            binding.confirmOrders1.setTypeface(binding.confirmOrders1.typeface, Typeface.NORMAL)
            binding.pendingOrders1.setTypeface(binding.pendingOrders1.typeface, Typeface.NORMAL)
            binding.delievrOrders1.setTypeface(binding.delievrOrders1.typeface, Typeface.NORMAL)
            binding.allOrders1.setTypeface(binding.allOrders1.typeface, Typeface.NORMAL)
            getspecificorders("CANCELLED")
        }

        binding.delievrOrders1.setOnClickListener {
            binding.delievrOrders1.setTypeface(binding.delievrOrders1.typeface, Typeface.BOLD)
            binding.cancelOrders1.setTypeface(binding.cancelOrders1.typeface, Typeface.NORMAL)
            binding.confirmOrders1.setTypeface(binding.confirmOrders1.typeface, Typeface.NORMAL)
            binding.pendingOrders1.setTypeface(binding.pendingOrders1.typeface, Typeface.NORMAL)
            binding.allOrders1.setTypeface(binding.allOrders1.typeface, Typeface.NORMAL)
            getspecificorders("DELIVERED")
        }

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.orderRecycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.orderRecycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        //Toolbar Logic Above


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.orderRecycleView)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        orderList = ArrayList()
        myOrdersAdapter = this?.let { MyOrdersAdapter(it, orderList as ArrayList<OrderModel>) }

        recyclerView!!.adapter = myOrdersAdapter


        when (Prevalent.UserType) {
            "Seller" -> {
                getmyorderssller(Prevalent.Sellerid)
            }

            "Admin" -> {
                getmyorderAdmin()
            }

            "Buyer" -> {
                getmyordersbuyer()
            }


        }


    }

    private fun getmyorderAdmin() {
        val buyerAllOrders =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")

        buyerAllOrders.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderList?.clear()

                for (orderValue in snapshot.children) {

                    val order = orderValue.getValue(OrderModel::class.java)
                    order?.orderid = orderValue.key.toString()

                    Log.wtf("ordervaluefinal", orderValue.key.toString())


                    val Orderbyseller =
                        FirebaseDatabase.getInstance().reference.child("MicMac")
                            .child("Placed Orders")
                            .child(orderValue.key.toString())


                    Orderbyseller.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ordervaluefinal in snapshot.children) {


                                val product = ordervaluefinal.getValue(OrderModel::class.java)


                                if (product != null) {
                                    orderList?.add(product)
                                }
                                myOrdersAdapter!!.notifyDataSetChanged()

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

    private fun getmyorderssller(sellerid: String) {

        val buyerAllOrders =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")


//        orderList?.clear()


        buyerAllOrders.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderList?.clear()

                for (orderValue in snapshot.children) {

                    val order = orderValue.getValue(OrderModel::class.java)
                    order?.orderid = orderValue.key.toString()

                    Log.wtf("ordervaluefinal", orderValue.key.toString())


                    val Orderbyseller =
                        FirebaseDatabase.getInstance().reference.child("MicMac")
                            .child("Placed Orders")
                            .child(orderValue.key.toString())


                    Orderbyseller.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ordervaluefinal in snapshot.children) {


                                val product = ordervaluefinal.getValue(OrderModel::class.java)


                                if (product != null&&product.sellerid==sellerid) {
                                    orderList?.add(product)
                                }
                                myOrdersAdapter!!.notifyDataSetChanged()

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


    private fun getspecificorders(state: String) {


        val Orderref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)

        Orderref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                orderList?.clear()

                for (orderValue in snapshot.children) {

                    val order = orderValue.getValue(OrderModel::class.java)


                    if (order != null && order.state == state) {
                        orderList!!.add(order)
                    }
                    myOrdersAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }


    private fun getmyordersbuyer() {

        val buyerAllOrders =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Placed Orders")
                .child(
                    FirebaseAuth.getInstance().currentUser!!.uid
                )


//        orderList?.clear()


        buyerAllOrders.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList?.clear()

                for (orders in snapshot.children) {


                    val order = orders.getValue(OrderModel::class.java)


                    if (order != null) {
                        orderList!!.add(order)
                    }
                    myOrdersAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }


    override fun onBackPressed() {


//        val intent: Intent = if (Prevalent.UserType=="Seller") {
//            Intent(this, AdminCategoryActivity::class.java)
//        } else{
        val intent: Intent = Intent(this, AllProducts::class.java)
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
