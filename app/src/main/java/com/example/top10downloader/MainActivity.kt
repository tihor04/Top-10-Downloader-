package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.ListView

import java.net.URL
import kotlin.properties.Delegates

class FeedEntry{
    var name :String =""
    var artist:String=""
    var releaseDate:String=""
    var summery :String=""
    var imageurl:String=""


}

class MainActivity : AppCompatActivity() {
    private val TAG="mainActivity"


    private var downloadData: DownloadData? = null

    private var feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10

    private var feedCachedUrl = "INVALIDATED"
    private val STATE_URL = "feedUrl"
    private val STATE_LIMIT = "feedLimit"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreatetag")

        if(savedInstanceState!=null)
        {
            feedUrl= savedInstanceState.getString(STATE_URL).toString()
            feedLimit=savedInstanceState.getInt(STATE_LIMIT)

        }


        downloadUrl(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate: done")
    }

    private fun downloadUrl(feedUrl:String){
        if(feedUrl!=feedCachedUrl){
            Log.d(TAG,"DownloadURL started")
            downloadData=DownloadData(this,findViewById(R.id.xmlListView))
            downloadData?.execute(feedUrl)
             feedCachedUrl=feedUrl
            Log.d(TAG,"downloadURL done")
        }
        else{
            Log.d(TAG,"DownloadURL: no change in url")
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu,menu)

        if(feedLimit==10)
        {
            menu?.findItem(R.id.mnu10)?.isChecked=true
        }
        else
            menu?.findItem(R.id.mnu25)?.isChecked=true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){
            R.id.mnufree->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnupaid->
                    feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnusongs->
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnu10,R.id.mnu25->{
                if(!item.isChecked)
                {
                    item.isChecked=true
                    feedLimit=35-feedLimit
                    Log.d(TAG,"onOptionsItemSelect: ${item.title} setting feedlimit changed to $feedLimit")

                }
                else
                {
                    Log.d(TAG,"onOptionsItemSelect: ${item.title} setting feedlimit unchainged")
                }
            }
            R.id.mnuRefreash-> feedCachedUrl="INVALIDATED"
            else->
                return super.onOptionsItemSelected(item)
        }
        downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_URL,feedUrl)
        outState.putInt(STATE_LIMIT,feedLimit)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

    //implementing the async task for our app

    companion object {
        private class DownloadData(context: Context, listview:ListView) :  AsyncTask<String, Void, String>(){
            private val TAG="DownloadData"


            // we initialis these in this way because we make have some data leaks from these operations

            var propcontext:Context by Delegates.notNull()
            var proplistview:ListView by Delegates.notNull()

            init{
                propcontext=context
                proplistview=listview
            }

            override fun doInBackground(vararg url: String?): String {

                Log.d(TAG,"Do in background starts with ${url[0]}")
                val rssfeed =downloadXML(url[0])
                if(rssfeed.isEmpty())
                {
                    Log.e(TAG,"doInBackground: error downloading")
                }

                return rssfeed

            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                val parseapplication=parseApplication()
                parseapplication.parse(result)

                var arrayadpater =FeedAdapter(propcontext,R.layout.list_record,parseapplication.applications)
                proplistview.adapter=arrayadpater
            }
        }


        private fun downloadXML(urlPath:String?) :String{
          return URL(urlPath).readText()
        }
    }

}