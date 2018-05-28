package com.codekul.notekeeping

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.content_create.*
import java.text.SimpleDateFormat
import java.util.*

class CreateActivity : AppCompatActivity() {
    //Get Access to Firebase database, no need of any URL, Firebase
    //identifies the connection via the package name of the app
    lateinit var mDatabase: DatabaseReference
     var noteFB = NoteFB()
    var objectIDcurrent : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        var bndl = intent.extras
        var filenameReceived = bndl?.getString("fbnttitle")
        var filedataReceived = bndl?.getString("fbntdata")
        var timestamprecevied = bndl?.getString("timestamp")
        var objectIdreceived = bndl?.getString("objectId")
        objectIDcurrent = objectIdreceived

        Log.i("@codekul","ONCreate Create $objectIdreceived & $objectIDcurrent")
        if(bndl!= null) retrieveFB(filenameReceived, filedataReceived,timestamprecevied,objectIdreceived)

        mDatabase = FirebaseDatabase.getInstance().reference

        fab.setOnClickListener { view ->

            var file_name = titleofNote.text.toString()
            var file_details = description.text.toString()
            if(file_name.equals("")){
                showDialog("title blank")
            }else if (file_details.equals("")){
                showDialog("data empty")
            }else {
                if(objectIDcurrent==null)
                    FBsave(file_name , file_details,null)
                else {
                    FBmodify(objectIDcurrent!!)
                }
                Snackbar.make(view, "Your NoteFB Saved", Snackbar.LENGTH_LONG)
                        .setAction("Go Back", {
                            back()
                        }).show()
            }
        }
    }

    private fun retrieveFB(filenameReceived: String?, filedataReceived: String?, timestamprecevied: String?, objectIdreceived: String?) {
        titleofNote.setText(filenameReceived)
        description.setText(filedataReceived)
        var timestamp = timestamprecevied
        timestampView.text = timestamp
        objectIDcurrent = objectIdreceived

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
//                filesDir.list().forEach {
//                    if(it == "${titleofNote.text}.txt"){
//                        deleteFile(it)
//                    } }
                    FBdelete(objectIDcurrent)

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

    fun FBsave(filename: String?, filedetails: String?, objectID: String?){
        if(objectID == null) {
            noteFB = NoteFB.create()
            noteFB.fbnttitle = filename
            noteFB.fbntdata = filedetails

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            noteFB.timestamp = currentDate

            //We first make a push so that a new item is made with a unique ID
            val newItem = mDatabase.child(Constants.FIREBASE_ITEM).push()
            noteFB.objectId = newItem.key
            objectIDcurrent = noteFB.objectId
            //then, we used the reference to set the value on that ID
            newItem.setValue(noteFB)
            Toast.makeText(this, "Item saved with ID " + noteFB.objectId, Toast.LENGTH_SHORT).show()
        }
        else{
           FBmodify(objectID)

        }
    }


    fun FBmodify(ObjectID : String){
        val itemReference = mDatabase.child(Constants.FIREBASE_ITEM).child(ObjectID)
        itemReference.child("fbnttitle").setValue(titleofNote.text.toString())
        itemReference.child("fbntdata").setValue(description.text.toString())
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        itemReference.child("timestamp").setValue(currentDate.toString())
        timestampView.text = currentDate
        Toast.makeText(this, "Item saved with ID " + noteFB.objectId, Toast.LENGTH_SHORT).show()
    }

    fun FBdelete(ObjectID: String?){
        //get child reference in database via the ObjectID
        val itemReference = mDatabase.child(Constants.FIREBASE_ITEM).child(ObjectID)
        //deletion can be done via removeValue()
        itemReference.removeValue()
    }
}