package com.example.top10downloader

import android.content.ContentValues.TAG
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

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

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG,"On post execute : parameter is $result")
            }
        }


        private fun downloadXML(urlPath:String?) :String{
            val xmlReslut =StringBuilder()

            try {
                val url = URL(urlPath)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                val response = connection.responseCode
                Log.d(TAG, "downloadXML: the response code was: $response")
//            val inputstream= connection.inputStream
//            val inputStreamReader=InputStreamReader(inputstream)
//            val reader=BufferedReader(inputStreamReader)


//                val reader = BufferedReader(InputStreamReader(connection.inputStream))
//
//                val inputBuffer = CharArray(500)
//                var charRead = 0
//                while (charRead >= 0) {
//                    charRead = reader.read(inputBuffer)
//                    if (charRead > 0) {
//                        xmlReslut.append(String(inputBuffer, 0, charRead))
//
//                    }
//                }
//                reader.close()

                //better and simpler way to write the above code in kotlin

                val stream=connection.inputStream
                stream.buffered().reader().use{
                    reader->xmlReslut.append(reader.readText())
                }

                Log.d(TAG, "Recived ${xmlReslut.length} bytes")
                return xmlReslut.toString()
            }catch(e:Exception)
            {
                val errormessage :String
               errormessage= when(e){
                    is MalformedURLException ->"downloadXML: Invalid url ${e.message}"
                    is IOException -> "downloadXML:IO exception reading data ${e.message}"
                    is SecurityException->"downloadXML: need permission ${e.message}"

                   else -> "unknown error ${e.message}"
               }
            }

//            }catch(e:MalformedURLException) {
//                Log.e(TAG,"dowlnaldXML: malformed url : ${e.message}")
//            }
//            catch (e:IOException) {
//                Log.e(TAG, "downloadXML:IO exception: ${e.message}")
//            }
//            catch(e:SecurityException){
//                Log.e(TAG,"downloadXML: security exception : needs internet permision  ${e.message}")
//            }
//            catch(e:Exception) {
//                Log.e(TAG,"unknown exception: ${e.message}")
//            }


            return ""  //if the program goes till here then we get an error
        }
    }

}