package com.example.popchat

import android.provider.ContactsContract.CommonDataKinds.Email

class User {
    var name: String? = null
    var email: String? = null
    var uid: String?= null
    var img_url: String ?= null

    constructor(){}

    constructor(name: String?, email: String?, uid: String?, img_url: String?){
        this.email = email
        this.name = name
        this.uid = uid
        this.img_url = img_url
    }
}
