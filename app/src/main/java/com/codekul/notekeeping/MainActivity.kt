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
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), ItemRowListener ,SearchView.OnQueryTextListener{

    //Get Access to Firebase database, no need of any URL, Firebase
    //identifies the connection via the package name of the app
    lateinit var mDatabase: DatabaseReference
    var noteItemList: MutableList<NoteFB>? = null
     var  adapter : Adapter? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    final private val RESULT_CODE = 11;

    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }

        private fun addDataToList(dataSnapshot: DataSnapshot) {

                mSwipeRefreshLayout?.isRefreshing = true

            val items = dataSnapshot.children.iterator()
            //Check if current database contains any collection
            if (items.hasNext()) {
                val toDoListindex = items.next()
                val itemsIterator = toDoListindex.children.iterator()

                //check if the collection has any to do items or not
                while (itemsIterator.hasNext()) {
                    //get current item
                    val currentItem = itemsIterator.next()
                    val NoteItem = NoteFB.create()

                    //get current data in a map
                    val map = currentItem.getValue() as HashMap<String, Any>
                    //key will return Firebase ID
                    NoteItem.objectId = currentItem.key
                    NoteItem.fbnttitle = map["fbnttitle"] as String?
                    NoteItem.fbntdata = map["fbntdata"] as String?
                    NoteItem.timestamp = map["timestamp"] as String?
                    Log.i("@codekul","adddatatoList :\n objectkey :${currentItem.key} \n ${map["fbnttitle"] as String?}")
                    noteItemList!!.add(NoteItem);
                }
            }
            //alert adapter that has changed
            adapter?.notifyDataSetChanged()
            mSwipeRefreshLayout?.isRefreshing = false

        }
        override fun onCancelled(databaseError: DatabaseError) {

            // Getting Item failed, log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {


        return true //added by me
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        return true //added by me
    }

    override fun onItemTouched(fbnttitle: String? , fbntdata: String?, timestamp: String? ,objectId: String?) {
            Log.i("@yog","card_onClick")
            val intent = Intent(this,CreateActivity::class.java)
            val bndl = Bundle()
            bndl.putString("objectId","$objectId")
            bndl.putString("fbnttitle","$fbnttitle")
            bndl.putString("fbntdata","$fbntdata")
            bndl.putString("timestamp","$timestamp")

        Log.i("@codekul"," from OnITemTouch \n Object Id :$objectId ,\n Title: $fbnttitle ,\n Data : $fbntdata ,\n Timestamp : $timestamp")
            intent.putExtras(bndl)
            startActivityForResult(intent, 123)

    }



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

//        /**
//         * Showing Swipe Refresh animation on activity create
//         * As animation won't start on onCreate, post runnable is used
//         */
//        mSwipeRefreshLayout?.post {
//            mSwipeRefreshLayout?.isRefreshing = true
//        }

        //initialize
        mDatabase = FirebaseDatabase.getInstance().reference
        noteItemList = mutableListOf<NoteFB>()
        adapter = Adapter(this,noteItemList!!)
        val recyclerView = findViewById<RecyclerView>(R.id.cardview_note)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = adapter
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)


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
                        val recyclerView = findViewById<RecyclerView>(R.id.cardview_note)
                        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                        recyclerView.adapter = adapter
                        adapter?.notifyDataSetChanged()
                        item.setIcon(view_two)
                    } else if(item.icon == view_two){
//                    if(item.icon  == R.drawable.ic_view_two_card_layout as Drawable){
                        val recyclerView = findViewById<RecyclerView>(R.id.cardview_note)
                        recyclerView.layoutManager = GridLayoutManager(this,2)
                        recyclerView.adapter = adapter
                        adapter?.notifyDataSetChanged()
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


    fun updateList(){
adapter?.notifyDataSetChanged()
    }


}

