package com.tchnodynamic.micmac.Models


class CustModel {

    var buyername: String =""
    var buyercontact: String=""
    var email: String=""
    var custordernymber: String=""
    var buyeraddress: String=""
    var totalpurchaseditem: Int=0
    var totalpurchasedamount: Int=0
    var buyerselller: String=""

    constructor(){}

    constructor(
        buyername: String,
        buyercontact: String,
        email: String,
        custordernymber: String,
        buyeraddress: String,
        totalpurchaseditem: Int,
        totalpurchasedamount: Int,
        buyerselller: String
    ) {
        this.buyername = buyername
        this.buyercontact = buyercontact
        this.email = email
        this.custordernymber = custordernymber
        this.buyeraddress = buyeraddress
        this.totalpurchaseditem = totalpurchaseditem
        this.totalpurchasedamount = totalpurchasedamount
        this.buyerselller=buyerselller
    }
}