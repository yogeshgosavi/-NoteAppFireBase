package com.codekul.notekeeping

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.FileNotFoundException
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.MenuInflater
import com.codekul.notekeeping.R.id.action_view
import com.codekul.notekeeping.R.id.swipe_container


class MainActivity : AppCompatActivity(), ItemRowListener ,SearchView.OnQueryTextListener{
    override fun onQueryTextSubmit(query: String?): Boolean {


        return true //added by me
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true //added by me
    }

    override fun onItemTouched(title: String, data: String?) {
            Log.i("@yog","card_onClick")
            val intent = Intent(this,CreateActivity::class.java)
            val bndl = Bundle()
            bndl.putString("filename","$title")
            intent.putExtras(bndl)
            startActivityForResult(intent, 123)

    }

    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var rawData : ArrayList<note> = ArrayList()
    final private val RESULT_CODE = 11;
    var adapt: Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            var  i = Intent(this,CreateActivity::class.java)
            startActivity(i)
        }
        mSwipeRefreshLayout = swipe_container as SwipeRefreshLayout?
        mSwipeRefreshLayout?.setOnRefreshListener { updateList() }
        mSwipeRefreshLayout?.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)
        mSwipeRefreshLayout?.post{

                mSwipeRefreshLayout?.isRefreshing = true

        }
//        /**
//         * Showing Swipe Refresh animation on activity create
//         * As animation won't start on onCreate, post runnable is used
//         */
//        mSwipeRefreshLayout?.post {
//            mSwipeRefreshLayout?.isRefreshing = true
//        }

        val recyclerView = findViewById(R.id.cardview_note) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this,2)
        updateList()
        adapt= Adapter(this,rawData)
        recyclerView.adapter = adapt

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_view, menu)
      val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
       val searchView = menu.findItem(R.id.search_view).actionView as SearchView
       if(null!= searchView) {
           searchView.setSearchableInfo(
                   searchManager.getSearchableInfo(componentName))
           searchView.setIconifiedByDefault(true)
       }
        searchView.setOnQueryTextListener(this)



        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_view -> {
                var view_single : Drawable = resources.getDrawable(R.drawable.ic_view_single)
                var view_two : Drawable = resources.getDrawable(R.drawable.ic_view_two_card_layout)
                item.icon = view_single

                item.setOnMenuItemClickListener {
                    Log.i("@codekul","${item.icon == view_two}")
                    Log.i("@codekul","${item.icon == view_single}")

                    if(item.icon == view_single ){
//                if(item.icon.drawable.ic_view_two_card_layout) ){
                        val recyclerView = findViewById(R.id.cardview_note) as RecyclerView
                        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                        recyclerView.adapter = adapt
                        adapt?.notifyDataSetChanged()
                        item.setIcon(view_two)
                    } else if(item.icon == view_two){
//                    if(item.icon  == R.drawable.ic_view_two_card_layout as Drawable){
                        val recyclerView = findViewById(R.id.cardview_note) as RecyclerView
                        recyclerView.layoutManager = GridLayoutManager(this,2)
                        recyclerView.adapter = adapt
                        adapt?.notifyDataSetChanged()
                        item.setIcon(view_single)
                    }
                     true
                }

                return true
            }
            R.id.search_view -> {


                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_CODE -> if (resultCode == Activity.RESULT_OK) {
                updateList()
            }
        }
    }


    override fun onResume() {
        updateList()
        super.onResume()
    }

    override fun onRestart() {
        updateList()
        super.onRestart()
    }

    override fun onBackPressed() {
       updateList()
        super.onBackPressed()
    }

    fun  updateList(){
        rawData.clear()
        filesDir.list().forEach {
           try {
               if(it.isNotBlank()) {
                   val dtreceived = retrieve("${it.removeSuffix(".txt")}")
                   rawData.add(note("${it.removeSuffix(".txt")}",dtreceived))
               }
           }catch(e : FileNotFoundException){
               null
           }
        }
        adapt?.notifyDataSetChanged()
        mSwipeRefreshLayout?.isRefreshing = false
    }

    fun retrieve(filename: String): String? {
        val isp = openFileInput("$filename.txt")
        val dt = isp.bufferedReader().use {
            it.readLine()
        }
        return dt
    }
}

