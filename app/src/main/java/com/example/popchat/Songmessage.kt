package com.example.popchat

class Songmessage {

    var coverImg: String? = null
    var title: String? = null
    var preview: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(title: String?, coverImg: String?, preview: String?, id:String?){
        this.coverImg = coverImg
        this.title = title
        this.preview = preview
        this.senderId = id
    }

}