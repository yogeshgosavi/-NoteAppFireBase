package com.codekul.notekeeping



 class NoteFB {

    companion object Factory {
        fun create(): NoteFB = NoteFB()
    }

    var fbnttitle: String? = null
    var fbntdata: String? = null
    var timestamp: String? = null
    var objectId: String? = null

}