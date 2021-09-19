package com.tchnodynamic.herbalpoint.Models

class Users {



     var userid: String = ""
     var email: String = ""
     var displayname: String = ""
     var photourl: String = ""


  constructor(){}


    constructor(userid: String, email: String, displayname: String, photourl: String) {
        this.userid = userid
        this.email = email
        this.displayname = displayname
        this.photourl = photourl
    }


}




