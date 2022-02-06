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

    private var downloadData :DownloadData?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreatetag")


        downloadData?.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG,"On create done")
    }

    private fun downloadUrl(feedUrl:String){

        Log.d(TAG,"DownloadURL started")
        downloadData=DownloadData(this,findViewById(R.id.xmlListView))
        downloadData?.execute(feedUrl)

        Log.d(TAG,"downloadURL done")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val feedUrl:String

        when(item.itemId){
            R.id.mnufree->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml"
            R.id.mnupaid->
                    feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml"
            R.id.mnusongs->
                feedUrl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml"
            else->
                return super.onOptionsItemSelected(item)
        }
        downloadUrl(feedUrl)
        return true
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