package com.tchnodynamic.herbalpoint.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

import com.tchnodynamic.herbalpoint.Models.CartModel
import com.tchnodynamic.micmac.CartActivity
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.ProductDetails
import com.tchnodynamic.micmac.R

class CartAdapter(private val mContext: Context,
                  private val mProduct: List<CartModel>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.cart_item_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.wtf("KISLAM","CArtAdapter")

        val product = mProduct[position]




        holder.cartorderprdname.text = product.productname
        holder.cartuserphonenumber.text = product.useridphone
        holder.cartorderquantity.text=product.quantity
        holder.cartitemprice.text=product.price
        Picasso.get().load(product!!.productimg).placeholder(R.drawable.profile).into(holder.cartorderimg)


        holder.itemView.setOnClickListener {


            if (Prevalent.UserType == "Buyer") {


                val options = arrayOf<CharSequence>(
                        "Edit",
                        "Remove"
                )

                val builder = AlertDialog.Builder(mContext)
                builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                    if (which == 0) {
                        val intent = Intent(mContext, ProductDetails::class.java)
                        intent.putExtra("pid", product.postid)
                        intent.putExtra("sid", product.sellerid)
                        intent.putExtra("oid", product.orderid)
                        intent.putExtra("productprice", product.price)
                        intent.putExtra("productquantity", product.quantity)
                        intent.putExtra("type", "cartadapter")
                        mContext.startActivity(intent)

                    } else {

                        FirebaseDatabase.getInstance().reference.child("MicMac").child("Cart List").child("User View").child(FirebaseAuth.getInstance().currentUser!!.uid).child("In Cart Item").child(product.orderid).removeValue()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(mContext, "Item Removed Successfully", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(mContext, CartActivity::class.java)
                                        mContext.startActivity(intent)


                                    }
                                }

                    }


                })

                builder.show()

            } else {

                val options = arrayOf<CharSequence>(
                        "Yes",
                        "No"
                )

                val builder = AlertDialog.Builder(mContext)
                builder.setTitle("Have You Shipped this Products?")
                builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                    if (which == 0) {

                        Toast.makeText(mContext, "Yet To Confirm", Toast.LENGTH_SHORT).show()

                    } else {


                    }
                })







                builder.show()

            }
        }


    }

    override fun getItemCount(): Int {

        return mProduct.size
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartorderimg: ImageView = itemView.findViewById(R.id.cartimg)
        var cartorderprdname: TextView = itemView.findViewById(R.id.cartorderItemName)
        var cartuserphonenumber: TextView = itemView.findViewById(R.id.cartconatctorder)
        var cartuserdatetime: TextView = itemView.findViewById(R.id.cartordertime)
        var cartusershippingaddress: TextView = itemView.findViewById(R.id.cartdeliveryaddress)
        var cartorderquantity: TextView = itemView.findViewById(R.id.booktime)
        var cartitemprice: TextView = itemView.findViewById(R.id.cartpriceinOrder)


        init {
//            productImage = itemView.findViewById(R.id.product_image)

        }
    }


}


