package com.example.popchat

class message {

    var message: String? = null
    var senderId: String? = null
    var coverImg: String? = null
    var title: String? = null
    var preview: String? = null

    constructor(){}

    constructor(message: String?, senderId: String?,title: String?, coverImg: String?, preview: String?){
        this.coverImg = coverImg
        this.title = title
        this.preview = preview
        this.message = message
        this.senderId = senderId
    }

}