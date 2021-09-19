package com.tchnodynamic.micmac

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Adapters.CartAdapter
import com.tchnodynamic.herbalpoint.Models.CartModel
import com.tchnodynamic.micmac.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private var cartAdapter: CartAdapter? = null
    private var cartList: MutableList<CartModel>? = null
    private var overtotalproce: Int = 0
    private var productname: String = ""
    var childrencpunt: Long = 0
    var totalallcart: Int = 0
    private var productimg: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.nointenet.visibility = View.GONE
                binding.interneton.visibility = View.VISIBLE
            } else {
                binding.nointenet.visibility = View.VISIBLE
                binding.interneton.visibility = View.GONE

            }

        })


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        Prevalent.Activityname = "CartActivity"



        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new post at top
        linearLayoutManager.stackFromEnd = true  // new post at top
        binding.recyCartList.layoutManager = linearLayoutManager
        binding.recyCartList.setHasFixedSize(true)


        cartList = ArrayList()
        cartAdapter = this?.let { CartAdapter(it, cartList as ArrayList<CartModel>) }

        binding.recyCartList!!.adapter = cartAdapter
        cartList?.clear()

        getcart(textView)


        binding.addProductToCartnew.setOnClickListener {

            val intent = Intent(applicationContext, UserFinalAddress::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("solve", "solve")
            startActivity(intent)
            finish()


        }

        binding.addMoreToCart.setOnClickListener {
            val intent = Intent(applicationContext,AllProducts::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }


    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {

        val intent = Intent(applicationContext, AllProducts::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

    }


    private fun getcart(textView: TextView) {

        var CartListref = FirebaseDatabase.getInstance().reference.child("MicMac").child("Cart List").child("User View").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("In Cart Item")


        CartListref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                cartList?.clear()

                if (Prevalent.Activityname == "CartActivity") {

                    for (cartvalue in snapshot.children) {

                        Log.wtf("ZEESHn", "zeeshan")

                        val cart = cartvalue.getValue(CartModel::class.java)

                        if (cart != null) {
                            cartList!!.add(cart)

                            totalallcart += cart.price.toInt()
                            productname = cart.productname
                            productimg = cart.productimg

                        }

                        cartAdapter!!.notifyDataSetChanged()
                    }
//                    binding.totalPrice.text = "Total Amount = ₹${totalallcart.toString()}"
                    Log.wtf("kislam", totalallcart.toString())

                    if (totalallcart == 0) {
                        binding.addProductToCartnew.visibility = View.INVISIBLE
                        textView.text = "Your Cart is Empty"
                        return

                    } else {

                        textView.text = ("Total Amount = ₹${totalallcart.toString()}")
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
