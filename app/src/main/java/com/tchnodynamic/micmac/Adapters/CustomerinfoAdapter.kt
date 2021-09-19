package com.tchnodynamic.micmac.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tchnodynamic.micmac.Admin.ConfirmFinalOrdersActivity
import com.tchnodynamic.micmac.Models.CustModel
import com.tchnodynamic.micmac.R

class CustomerinfoAdapter(val mContext: Context, private val mProduct:List<CustModel>) : 
    RecyclerView.Adapter<CustomerinfoAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.customer_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.wtf("KISLAM","MyOrdersAdapter")


        val custominfo = mProduct[position]


        holder.custname.text = custominfo.buyername
        holder.custaddress.text = custominfo.buyeraddress
        holder.custemail.text = custominfo.email
        holder.custphone.text=custominfo.buyercontact
        holder.custordernymber.text=custominfo.totalpurchaseditem.toString()
        holder.custtotordered.text=custominfo.totalpurchasedamount.toString()
        holder.cutomersellr.text=custominfo.buyerselller


//        holder.itemView.setOnClickListener{
//            if (Prevalent.UserType=="Seller") {
//                val intent = Intent(mContext, ConfirmFinalOrdersActivity::class.java)
//                intent.putExtra("oid", custominfo.orderid)
//                intent.putExtra("bid", custominfo.userid)
//                intent.putExtra("ostatus", custominfo.state)
//                mContext.startActivity(intent)
//            }

        }




    override fun getItemCount(): Int {

        return mProduct.size
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var custname: TextView
        var custphone: TextView
        var custemail: TextView
        var custordernymber: TextView
        var custaddress: TextView
        var custtotordered: TextView
        var cutomersellr: TextView



        init {
            custname = itemView.findViewById(R.id.custname)
            custphone = itemView.findViewById(R.id.custphone)
            custemail = itemView.findViewById(R.id.custemail)
            custordernymber = itemView.findViewById(R.id.custordernymber)
            custaddress = itemView.findViewById(R.id.custaddress)
            custtotordered = itemView.findViewById(R.id.custtotordered)
            cutomersellr = itemView.findViewById(R.id.customerseller)

        }
    }
}


