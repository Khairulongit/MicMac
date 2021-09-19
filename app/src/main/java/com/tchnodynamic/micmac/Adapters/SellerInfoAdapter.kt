package com.tchnodynamic.micmac.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tchnodynamic.micmac.Models.CustModel
import com.tchnodynamic.micmac.Models.SellerModel
import com.tchnodynamic.micmac.R

class SellerInfoAdapter (val mContext: Context, private val mProduct:List<SellerModel>) :
    RecyclerView.Adapter<SellerInfoAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.seller_layout, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.wtf("KISLAM","MyOrdersAdapter")


        val sellerminfo = mProduct[position]


        holder.sellername.text = sellerminfo.sellername
        holder.selleraddress.text = sellerminfo.selleraddress
        holder.sellerid.text = sellerminfo.sellerid
        holder.sellerphone.text=sellerminfo.sellercontact
        holder.sellertotamtbusiness.text=sellerminfo.sellertotalamunt
        holder.sellertotqnty.text=sellerminfo.sellertotorder
        holder.sellermemsince.text=sellerminfo.sellerregisdate
        holder.sellerpin.text=sellerminfo.sellerpin


    }




    override fun getItemCount(): Int {

        return mProduct.size
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sellername: TextView = itemView.findViewById(R.id.sellername)
        var sellerphone: TextView = itemView.findViewById(R.id.sellerphone)
        var sellerid: TextView = itemView.findViewById(R.id.sellerid)
        var sellertotamtbusiness: TextView = itemView.findViewById(R.id.sellertotamountordered)
        var selleraddress: TextView = itemView.findViewById(R.id.selleraddress)
        var sellertotqnty: TextView = itemView.findViewById(R.id.sellertotorderquantity)
        var sellermemsince: TextView = itemView.findViewById(R.id.sellermemsince)
        var sellerpin: TextView = itemView.findViewById(R.id.sellerpin)


    }
}


