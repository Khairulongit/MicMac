package com.tchnodynamic.herbalpoint.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.micmac.Admin.ConfirmFinalOrdersActivity
import com.tchnodynamic.micmac.Prevalent
import com.tchnodynamic.micmac.R

class MyOrdersAdapter (val mContext: Context,
private val mProduct:List<OrderModel>) : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.myorders_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.wtf("KISLAM","MyOrdersAdapter")


        val ordereditem = mProduct[position]


        holder.orderprdname.text = ordereditem.productname
        holder.userphonenumber.text = ordereditem.deliveryphone
        holder.userdatetime.text = "${ordereditem.time} on ${ordereditem.date}"
        holder.usershippingaddress.text = "${ordereditem.address}"
        holder.orderstatus.text=ordereditem.state
        holder.itemprice.text=ordereditem.totalamount
        holder.orderquantity.text=ordereditem.quantity

        val first=ordereditem.userid.subSequence(0,4)

        Log.wtf("state",ordereditem.state)

        holder.cartorderText.text="OD${ordereditem.userid.subSequence(0,4)}${ordereditem.orderid.subSequence(16,20)}"
        Picasso.get().load(ordereditem!!.productimg).placeholder(R.drawable.profile).into(holder.orderimg)


        holder.itemView.setOnClickListener{
            if (Prevalent.UserType=="Seller") {
                val intent = Intent(mContext, ConfirmFinalOrdersActivity::class.java)
                intent.putExtra("oid", ordereditem.orderid)
                intent.putExtra("bid", ordereditem.userid)
                intent.putExtra("pid", ordereditem.productid)
                intent.putExtra("pquantity", ordereditem.quantity)
                intent.putExtra("prdamount", ordereditem.totalamount)
                intent.putExtra("ostatus", ordereditem.state)
                mContext.startActivity(intent)
            }

        }

    }


    override fun getItemCount(): Int {

        return mProduct.size
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderimg: ImageView
        var orderprdname: TextView
        var userphonenumber: TextView
        var userdatetime: TextView
        var usershippingaddress: TextView
        var orderquantity: TextView
        var orderstatus: TextView
        var cartorderText: TextView
        var itemprice: TextView


        init {
            orderimg = itemView.findViewById(R.id.orderimg)
            cartorderText = itemView.findViewById(R.id.cartorderText)
            orderstatus = itemView.findViewById(R.id.orderstaus)
            orderprdname = itemView.findViewById(R.id.orderItemName)
            itemprice = itemView.findViewById(R.id.priceinOrder)
            orderquantity = itemView.findViewById(R.id.orderquantity)
            userphonenumber = itemView.findViewById(R.id.conatctorder)
            userdatetime = itemView.findViewById(R.id.ordertime)
            usershippingaddress = itemView.findViewById(R.id.deliveryaddress)

        }
    }
}


