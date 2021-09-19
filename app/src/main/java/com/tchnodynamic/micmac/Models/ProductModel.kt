package com.tchnodynamic.herbalpoint.Models

class ProductModel {

    var category: String =""
    var date: String =""
    var description: String =""
    var productid: String=""
    var price: String=""
    var productimg: String=""
    var productname: String =""
    var time: String=""
    var sellerid: String=""
    var orderid: String=""
    var availability: String =""


    constructor()


    constructor(category: String, date: String, description: String, productid: String, price: String, productimg: String, productname: String, time: String,sellerid:String,orderid:String,availability:String) {
        this.category = category
        this.date = date
        this.description = description
        this.productid = productid
        this.price = price
        this.productimg = productimg
        this.productname = productname
        this.time = time
        this.sellerid=sellerid
        this.orderid=orderid
        this.availability=availability
    }


}