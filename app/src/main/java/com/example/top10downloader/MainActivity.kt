package com.example.top10downloader

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import java.net.URL

class FeedEntry{
    var name :String =""
    var artist:String=""
    var releaseDate:String=""
    var summery :String=""
    var imageurl:String=""

    //we will use this toString function to see that if everything working fine in the logcat.

    override fun toString(): String {
        return """"
            name= $name
            artist= $artist
            relaeasedate= $releaseDate
            imageurl= $imageurl
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG="mainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"onCreatetag")

        val downloaddata=DownloadData()
        downloaddata.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG,"On create done")


    }

    //implementing the async task for our app

    companion object {
        private class DownloadData() :  AsyncTask<String, Void, String>(){
            private val TAG="DownloadData"

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
            }
        }


        private fun downloadXML(urlPath:String?) :String{
          return URL(urlPath).readText()
        }
    }

}