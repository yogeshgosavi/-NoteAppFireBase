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

data class note(val title: String, val data: String?)

class Adapter (context: Context ,val notes: ArrayList<note>) : RecyclerView.Adapter<Adapter.ViewHolder>()  {
    private var rowListener: ItemRowListener = context as ItemRowListener
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.note_card,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val note:note = notes[position]
        holder?.titleview?.text = note.title
        holder?.dataview?.text = note.data
        holder?.cardView?.setOnClickListener {
            rowListener.onItemTouched(note.title,note.data)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleview = itemView.findViewById(R.id.title) as TextView
        val dataview = itemView.findViewById(R.id.data) as TextView
        var cardView = itemView.findViewById(R.id.cardview_note_card) as CardView
    }

}