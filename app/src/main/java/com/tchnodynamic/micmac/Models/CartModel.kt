package com.tchnodynamic.herbalpoint.Models

class CartModel {

    var postid: String =""
    var useridphone: String =""
    var productname: String =""
    var productimg: String =""
    var sellerid: String =""
    var price: String =""
    var quantity: String=""
    var discount: String=""
    var orderid: String=""


    constructor()
    constructor(postid: String, useridphone:String,productname: String,productimg:String,sellerid:String, price: String, quantity: String, discount: String,orderid:String) {
        this.postid = postid
        this.useridphone = useridphone
        this.productname = productname
        this.productimg = productimg
        this.sellerid = sellerid
        this.price = price
        this.quantity = quantity
        this.discount = discount
        this.orderid = orderid
    }


}