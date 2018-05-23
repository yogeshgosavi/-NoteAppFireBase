package com.codekul.notekeeping


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyDialogs : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        when (tag) {
            "title blank" -> {
                return AlertDialog.Builder(activity as Context)
                        .setTitle("You need to have Title")
                        .setMessage("Please enter title to save note")
                        .setPositiveButton("Sure",
                                { dialog, which -> null })
                        .create()
            }

            "data empty" ->{
                return  AlertDialog.Builder(activity as Context)
                        .setTitle("You need to Enter data")
                        .setMessage("Please enter data to save note")
                        .setPositiveButton("Sure",
                                { dialog, which -> null })
                        .create()
            }
            "delete" ->{
                return  AlertDialog.Builder(activity as Context)
                        .setTitle("You Sure you want to delete this note ?")
                        .setMessage("You can't recover it after it's done")
                        .setIcon(R.drawable.ic_delete_black_24dp)
                        .setPositiveButton("Sure",
                                { dialog, which -> null })
                        .setNegativeButton("No Thanks",{
                            dialog, which -> null
                        })
                        .create()
            }
        }

            return Dialog(activity as Context)
        }
    }



