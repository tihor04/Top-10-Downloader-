package com.example.top10downloader

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

import java.net.URL
import kotlin.properties.Delegates

class FeedEntry{
    var name :String =""
    var artist:String=""
    var releaseDate:String=""
    var summery :String=""
    var imageurl:String=""

    //we will use this toString function to see that if everything working fine in the logcat.

//    //override fun toString(): String {
//        return """"
//            name= $name
//            artist= $artist
//            relaeasedate= $releaseDate
//            imageurl= $imageurl
//            """.trimIndent()
//    }
}

class MainActivity : AppCompatActivity() {
    private val TAG="mainActivity"

    private val downloadData by lazy {DownloadData(this,findViewById(R.id.xmlListView))}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreatetag")


        downloadData.execute("https://rss.applemarketingtools.com/api/v2/in/apps/top-free/10/apps.json")
        Log.d(TAG,"On create done")


    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
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