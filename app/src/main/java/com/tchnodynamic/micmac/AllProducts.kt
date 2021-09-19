package com.tchnodynamic.micmac

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nex3z.notificationbadge.NotificationBadge
import com.tchnodynamic.herbalpoint.Adapters.ProductAdapterNEW
import com.tchnodynamic.herbalpoint.Models.CartModel
import com.tchnodynamic.herbalpoint.Models.ProductModel
import com.tchnodynamic.herbalpoint.Models.Users
import com.tchnodynamic.micmac.Admin.AdminCategoryActivity
import com.tchnodynamic.micmac.databinding.ActivityAllProductsBinding
import io.paperdb.Paper
import kotlin.system.exitProcess

class AllProducts : AppCompatActivity() {
    private lateinit var binding: ActivityAllProductsBinding
    var toggle: ActionBarDrawerToggle? = null
    lateinit var hearderview: View
    private var Searchiinput: String = ""
    private var totalallcart: Int = 0
    var searchView: SearchView? = null


    //    private var productAdapter:ProductAdapter?=null
    private var productAdapter: ProductAdapterNEW? = null
    private var productList: MutableList<ProductModel>? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        / Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)
        textView.text = ""

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.recycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.recycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        if (Prevalent.UserType == "Buyer") {

            binding.addnew.visibility = View.INVISIBLE
        }

        if (Prevalent.UserType == "Buyer") {
            hideItemseller()
            getproduct()

        } else {
            hideItem()
            getproductall()
        }


        binding.addnew.setOnClickListener {
            val intent = Intent(this, AdminCategoryActivity::class.java)
            startActivity(intent)

//            finish()
        }


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.recycle_view)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        productList = ArrayList()
        productAdapter = ProductAdapterNEW(applicationContext)
        productAdapter!!.setData(productList as ArrayList<ProductModel>)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView!!.adapter = productAdapter
//        recyclerView!!.adapter.setStateRestorationStrategy(StateRestorationStrategy.WHEN_NOT_EMPTY);
//        recyclerView!!.adapter.stateRe = productAdapter


    }

    private fun hideItem() {
    }

    private fun hideItemseller() {

    }


    private fun getcart() {

        var CartListref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Cart List")
                .child("User View").child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("In Cart Item")

        CartListref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (cartvalue in snapshot.children) {

                    val cart = cartvalue.getValue(CartModel::class.java)

                    if (cart != null) {

                        totalallcart += cart.quantity.toInt()

                    }
                }
//                badge.setText(totalallcart.toString())
                Log.wtf("shahnaz", totalallcart.toString())

            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun getproductall() {

        Prevalent.Activityname = "HomeActivityNew"
        val Sellerall =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Products")

//        binding.shimmerframelayout.stopShimmer()
//        binding.shimmerframelayout.visibility - View.GONE
//        binding.shimmerframelayout.clearAnimation()
        Sellerall.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList?.clear()


                for (userid in snapshot.children) {

                    val user = userid.getValue(Users::class.java)
                    user?.userid = userid.key.toString()


                    val Productall =
                        FirebaseDatabase.getInstance().reference.child("MicMac")
                            .child("Products").child("adminaddprd")
//                                userid.key.toString()
//                                userid.key.toString()
//                                userid.key.toString()
//                            )

                    Productall.addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (productvalue in snapshot.children) {
//
                                val product = productvalue.getValue(ProductModel::class.java)
                                product?.productid = productvalue.key.toString()

                                if (product != null) {
                                    productList!!.add(product)
                                }
                                productAdapter!!.notifyDataSetChanged()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.searchmenu)
        val updatecart = menu.findItem(R.id.updatecart)
        val userlogout = menu.findItem(R.id.userlogout)

        userlogout.setOnMenuItemClickListener {

            Paper.book().destroy()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginUser::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK+ Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return@setOnMenuItemClickListener true;


        }

        val view = menu.findItem(R.id.updatecart).actionView
        val badge = view.findViewById<View>(R.id.badge) as NotificationBadge

        if (Prevalent.UserType == "Buyer") {
            getcart()
        }
        badge.setText(totalallcart.toString())
//        updatecartcount()
        view.setOnClickListener {
            //                Toast.makeText(MainActivity.this, "Notthing", Toast.LENGTH_SHORT).show();
            startActivity(Intent(applicationContext, CartActivity::class.java))
            finish();
        }


        val contactherbal = menu.findItem(R.id.contactus);
        contactherbal.setOnMenuItemClickListener {


            Toast.makeText(
                this,
                "Mob : 9836415522 Email : herbalpoint.in@gmail.com",
                Toast.LENGTH_LONG
            ).show()
            return@setOnMenuItemClickListener true;


        }

        val contactdev = menu.findItem(R.id.contactdev);
        contactdev.setOnMenuItemClickListener {

            startActivity(Intent(applicationContext, Technodynamic::class.java))
            //                finish();
            return@setOnMenuItemClickListener true;

//        }(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getApplicationContext(), "In CArt", Toast.LENGTH_SHORT).show();
//
//            }
        }

        val prevorders = menu.findItem(R.id.prevorder);
        prevorders.setOnMenuItemClickListener {

            val intent = Intent(applicationContext, MyOrdersActivity::class.java)
            startActivity(intent)
//            finish()
            return@setOnMenuItemClickListener true;

//        }(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getApplicationContext(), "In CArt", Toast.LENGTH_SHORT).show();
//
//            }
        }



        if (Prevalent.UserType == "Buyer") {
            getcart()
//
        }


        if (Prevalent.UserType == "Admin") {
            view.isVisible = false
            updatecart.isVisible = false

        }


//        badge.setText(totalcount.toString())

//        if(Prevalent.UserType=="Seller"){
//
//            item.isVisible=false
//            view.isVisible=false
//            updatecart.isVisible=false
//            badge.isVisible=false
//        }
//        updatecartcount()
        view.setOnClickListener {
            //                Toast.makeText(MainActivity.this, "Notthing", Toast.LENGTH_SHORT).show();
            startActivity(Intent(applicationContext, CartActivity::class.java))
            //                finish();
        }


        // SEARCHVIEW LOGIC IS BELOW
        searchView = item.actionView as SearchView
        searchView!!.queryHint = "Type Product Name"
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                productAdapter?.filter?.filter(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                productAdapter?.filter?.filter(newText)
                return true
            }
        })


//        SEARCHVIEW LOGIC IS ABOVE
        return super.onCreateOptionsMenu(menu)
    }


    private fun getproduct() {

        val onlyseller =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Products").child(
//            FirebaseAuth.getInstance().currentUser!!.uid
                "adminaddprd"
            )


//        binding.shimmerframelayout.stopShimmer()
//        binding.shimmerframelayout.visibility - View.GONE
//        binding.shimmerframelayout.clearAnimation()

        productList?.clear()

        onlyseller.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for (productvalue in snapshot.children) {
                    val product = productvalue.getValue(ProductModel::class.java)




                    if (product != null) {
                        productList!!.add(product)
                    }
                    productAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })


    }


    override fun onBackPressed() {


        if (Prevalent.UserType == "Admin") {
            val intent: Intent = Intent(this, AdminCategoryActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            run {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    Paper.book().destroy()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginUser::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK+ Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }
        } //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)

    }
}
