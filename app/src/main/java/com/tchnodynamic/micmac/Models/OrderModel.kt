package com.tchnodynamic.herbalpoint.Models

class OrderModel {


    var productid: String =""
    var address: String =""
    var productimg: String =""
    var date: String =""
    var phone: String=""
    var pincode: String=""
    var productname: String=""
    var state: String=""
    var time: String =""
    var totalamount: String=""
    var userid:String=""
    var orderid:String=""
    var customername:String=""
    var deliveryphone:String=""
    var quantity:String=""
    var sellerid:String=""


    constructor(){}

    constructor(productid:String,address: String, productimg:String,pincoode: String, date: String, phone: String, productname: String, state: String, time: String, totalamount: String,userid:String,orderid:String,custname:String,deliveryphone:String,quantity:String,sellerid:String) {
        this.productid=productid
        this.address = address
        this.pincode = pincoode
        this.productimg=productimg
        this.date = date
        this.phone = phone
        this.productname = productname
        this.state = state
        this.time = time
        this.totalamount = totalamount
        this.userid = userid
        this.orderid=orderid
        this.customername=custname
        this.deliveryphone=deliveryphone
        this.quantity=quantity
        this.sellerid=sellerid
    }
}