package com.codekul.notekeeping

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

//data class note(val title: String, val data: String?)

class Adapter (context: Context ,val notes: MutableList<NoteFB>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private var rowListener: ItemRowListener = context as ItemRowListener
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = NoteFB.create()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val objectId = itemList.objectId
//        var title = itemList.fbnttitle
//        var data = itemList.fbntdata
//        var timestamp = itemList.timestamp
//
//        Log.i("@codekul","ONCreateViewHOlder $objectId,$title,$data,$timestamp")
//        val view:View
//        val vh : ViewHolder
//        view = LayoutInflater.from(parent?.context).inflate(R.layout.note_card,parent,false)
//        vh = ViewHolder(view)
//        view.tag = vh
//        vh.titleview.text = title
//        vh.dataview.text = data
//        vh.timeview.text = timestamp
//
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.note_card,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note:NoteFB = notes[position]
        holder?.titleview?.text = note.fbnttitle
        holder?.dataview?.text = note.fbntdata
        holder?.timeview?.text = note.timestamp
        val objectId: String ?= note.objectId
        Log.i("@codekul"," from OnBindViewHOlder \n " +
                "title :   ${note.fbnttitle} " +
                "Data :  ${note.fbntdata} " +
                "timestamp :  ${note.timestamp}" +
                "ObjectId : ${note.objectId}"
        )
        holder?.cardView?.setOnClickListener {
            rowListener.onItemTouched(note.fbnttitle,note.fbntdata,note.timestamp,objectId)
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleview = itemView.findViewById<TextView>(R.id.title)
        val dataview = itemView.findViewById<TextView>(R.id.data)
        val timeview  =itemView.findViewById<TextView>(R.id.timestamp)
        var cardView = itemView.findViewById<CardView>(R.id.cardview_note_card)
    }

}