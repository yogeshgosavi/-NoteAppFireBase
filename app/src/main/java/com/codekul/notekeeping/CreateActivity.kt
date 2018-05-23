package com.codekul.notekeeping

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.content_create.*
import java.io.File

class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        var bndl = intent.extras
        var filenamereceived = bndl?.getString("filename")
        if(filenamereceived!= null) {
            retrieve(filenamereceived)
        }
        fab.setOnClickListener { view ->

            var file_name = titleofNote.text.toString()
            var file_details = description.text.toString()
            if(file_name.equals("")){
                showDialog("title blank")
            }else if (file_details.equals("")){
                showDialog("data empty")
            }else if(file_name.isNotBlank() && file_details.isNotBlank()){
                saveNote(file_name,file_details)
                Snackbar.make(view, "Your Note Saved", Snackbar.LENGTH_LONG)
                        .setAction("Go Back", {
                            back()
                        }).show()
            }
        }
    }

    fun saveNote(filename:String, filedetails:String){
        if(filename.isNotBlank() && filedetails.isNotBlank()){
            openFileOutput("$filename.txt", Context.MODE_PRIVATE).use {
                it.write(filedetails.toByteArray())
            }
        }
        else {
        }
    }

    fun retrieve(filename: String) {
        val isp = openFileInput("$filename.txt")
        val dt = isp.bufferedReader().use {
            it.readLine()
        }
        titleofNote.setText(filename)
        description.setText(dt)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                filesDir.list().forEach {
                    if(it == "${titleofNote.text}.txt"){
                        deleteFile(it)
                    }
                }
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        back()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun back() {
        val backInt = Intent()
        setResult(Activity.RESULT_OK, backInt)
        finish()
    }

    fun showDialog(tag : String) = MyDialogs().show(supportFragmentManager,tag)

}
