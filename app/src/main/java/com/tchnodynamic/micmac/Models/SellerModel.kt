package com.tchnodynamic.micmac.Models

class SellerModel {

    var sellername: String = ""
    var sellerid: String = ""
    var sellerpin: String = ""
    var sellercontact: String = ""
    var selleraddress: String = ""
    var sellertotuser: String = ""
    var sellertotorder: String = ""
    var sellertotalamunt: String = ""
    var sellerregisdate: String = ""

    constructor() {}
    constructor(
        sellername: String,
        sellerid: String,
        sellerpin: String,
        sellercontact: String,
        selleraddress: String,
        sellertotuser: String,
        sellertotorder: String,
        sellertotalamunt: String,
        sellerregisdate: String
    ) {
        this.sellername = sellername
        this.sellerid = sellerid
        this.sellerpin = sellerpin
        this.sellercontact = sellercontact
        this.selleraddress = selleraddress
        this.sellertotuser = sellertotuser
        this.sellertotorder = sellertotorder
        this.sellertotalamunt = sellertotalamunt
        this.sellerregisdate = sellerregisdate
    }


}