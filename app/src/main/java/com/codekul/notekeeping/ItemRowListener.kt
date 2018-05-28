package com.codekul.notekeeping

interface ItemRowListener {
fun onItemTouched(fbnttitle: String? , fbntdata: String?, timestamp: String? ,objectId: String?)
}