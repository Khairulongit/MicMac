package com.tchnodynamic.micmac

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nex3z.notificationbadge.NotificationBadge
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.Models.CartModel
import com.tchnodynamic.herbalpoint.Models.ProductModel
import com.tchnodynamic.micmac.databinding.ActivityProductDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    var productId: String = ""
    var sellerid: String = ""
    var productquantity: Int = 0
    var productprice: Int = 0
    var totalprice: Int = 0
    var alreadyincart: Boolean = false
    private var totalallcart: Int = 0

    private var Savecurrentdate: String = ""
    private var Savecurrenttime: String = ""
    private var productimg: String = ""
    private var type: String? = "Productdetails"
    private var orderid: String? = null
    private var cartListRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
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

        getcart()


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)
        textView.text = ""

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }

        //Toolbar Logic Above


        val button = findViewById<View>(R.id.number_btn) as ElegantNumberButton

        binding.productDetailsDesc.isEnabled = false
        binding.productDetailsName.isEnabled = false
        binding.productDetailsPrice.isEnabled = false
        binding.productDetailsAvailability.isEnabled = false

        productId = intent.getStringExtra("pid").toString()
        type = intent.getStringExtra("type")
        sellerid = intent.getStringExtra("sid").toString()
        orderid = intent.getStringExtra("oid").toString()
        productprice = intent.getStringExtra("productprice")!!.toInt()
        productquantity = intent.getStringExtra("productquantity")!!.toInt()

        binding.productDetailsPrice.setText(productprice.toString())

        if (type == "cartadapter") {

            productprice /= productquantity
            binding.addProductToCart.text = "Update Cart"

        }



        button.number = productquantity.toString()


        getproductdetails(productId, sellerid)



        button.setOnClickListener(ElegantNumberButton.OnClickListener {

            productquantity = button.number.toInt()
            totalprice = productprice * productquantity
            binding.productDetailsPrice.setText(totalprice.toString())

        })



        binding.addProductToCart.setOnClickListener {

            if (type == "cartadapter") {

                updatecart(orderid!!)
            } else {

                alreadyincart()
            }


        }


    }

    private fun alreadyincart() {

        val CartListref =
            FirebaseDatabase.getInstance().reference.child("MicMax").child("Cart List")
                .child("User View")?.child(
                FirebaseAuth.getInstance().currentUser!!.uid
            )?.child("In Cart Item")


        CartListref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (cartvalue in snapshot.children) {

                    val cart = cartvalue.getValue(CartModel::class.java)

                    if (productId == cart?.postid) {

                        alreadyincart = true

//                            val intent = Intent(applicationContext, CartActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            startActivity(intent)
//                            finish()
                    }
                }
                Log.wtf("alreadyincart() inside ", alreadyincart.toString())

                if (alreadyincart) {

//                        Toast.makeText(applicationContext, "Item Already in Cart", Toast.LENGTH_SHORT).show()
//                        finish()
                } else {
                    addTocartList()
                }

            }


            override fun onCancelled(error: DatabaseError) {
            }

        }
        )

        Toast.makeText(applicationContext, "Item Already in Cart", Toast.LENGTH_SHORT).show()

    }


    private fun updatecart(orderid: String) {

        FirebaseDatabase.getInstance().reference.child("MicMax").child("Cart List")
            .child("User View")?.child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )?.child("In Cart Item").child(orderid).child("quantity").setValue(binding.numberBtn.number)
        FirebaseDatabase.getInstance().reference.child("MicMac").child("Cart List")
            .child("User View")?.child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )?.child("In Cart Item").child(orderid).child("price")
            .setValue(binding.productDetailsPrice.text.toString())
        Toast.makeText(applicationContext, "Item Updated in Cart", Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, CartActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()


    }


    private fun addTocartList() {

        var calendar: Calendar = Calendar.getInstance()
        var currentdate = SimpleDateFormat("MMM dd,yyyy")
        Savecurrentdate = currentdate.format(calendar.time)

        var currenttime = SimpleDateFormat("HH:mm:ss a")
        Savecurrenttime = currenttime.format(calendar.time)

        cartListRef =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Cart List")
                .child("User View")?.child(
                FirebaseAuth.getInstance().currentUser!!.uid
            )?.child("In Cart Item")


        val cartmap = HashMap<String, Any>()
        val orderid = cartListRef?.push()?.key.toString() // will create random number

        Log.wtf("productimg",productimg)
        cartmap["postid"] = productId
        cartmap["useridphone"] = "phone"
        cartmap["productname"] = binding.productDetailsName.text.toString()
        cartmap["price"] = binding.productDetailsPrice.text.toString()
        cartmap["productimg"] = productimg
        cartmap["date"] = Savecurrentdate
        cartmap["time"] = Savecurrenttime
        cartmap["quantity"] = binding.numberBtn.number
        cartmap["discount"] = ""
        cartmap["sellerid"] = sellerid
        cartmap["orderid"] = orderid

//        cartListRef?.child(orderid)?.updateChildren(cartmap)?.addOnCompleteListener {
        cartListRef?.child(orderid)?.updateChildren(cartmap)?.addOnCompleteListener {
            if (it.isSuccessful) {

                Toast.makeText(applicationContext, "Item Added to Cart", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, CartActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }

    }


    private fun getproductdetails(productId: String, sellerid: String) {

        val Productref =
            FirebaseDatabase.getInstance().reference.child("MicMac").child("Products")
                .child("adminaddprd")
        Productref.child(productId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {

                    val product: ProductModel? = snapshot.getValue(ProductModel::class.java)

                    binding.productDetailsName.setText(product?.productname)
                    binding.productDetailsDesc.setText(product?.description)
                    binding.productDetailsAvailability.setText("Available : " +product?.availability)
                    productimg = product?.productimg.toString()

                    Picasso.get().load(product?.productimg).placeholder(R.drawable.profile)
                        .into(binding.productImageDeytails)


                }


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.searchmenu)
        item.isVisible = false
        val view = menu.findItem(R.id.updatecart).actionView
        val badge = view.findViewById<View>(R.id.badge) as NotificationBadge
//        badge.setText(totalcount.toString())
        if (Prevalent.UserType != "Seller") {
            getcart()
        }
        badge.setText(totalallcart.toString())
//        updatecartcount()
        view.setOnClickListener {
            //                Toast.makeText(MainActivity.this, "Notthing", Toast.LENGTH_SHORT).show();
            startActivity(Intent(applicationContext, CartActivity::class.java))
            finish();
        }




        // SEARCHVIEW LOGIC IS ABOVE
        return super.onCreateOptionsMenu(menu)
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


