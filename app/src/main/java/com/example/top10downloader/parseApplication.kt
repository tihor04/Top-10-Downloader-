package com.example.top10downloader

import android.service.autofill.TextValueSanitizer
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class parseApplication {
    private val TAG="pareseApplication"
  val applications =ArrayList<FeedEntry>()


    fun parse(XMLdata:String) :Boolean {
        Log.d(TAG,"parse called for $XMLdata")
        var status=true
        var inEntry=false
        var textValue=""

        try{
            val factory=XmlPullParserFactory.newInstance()
            factory.isNamespaceAware=true
            val xpp=factory.newPullParser()
            xpp.setInput(XMLdata.reader())
            var eventType=xpp.eventType
            var currentRecord=FeedEntry()

            while(eventType!=XmlPullParser.END_DOCUMENT){
                var tagName= xpp.name?.toLowerCase()
                when(eventType) {

                    XmlPullParser.START_TAG ->{
                        //Log.d(TAG, "Parse : end tag for $tagName")
                    if (tagName == "entry") {
                        inEntry = true
                    }
                }

                    XmlPullParser.TEXT->textValue=xpp.text

                    XmlPullParser.END_TAG->{
                      //  Log.d(TAG,"parse:Ending tag for $tagName")
                        if(inEntry){
                            when(tagName){
                                "entry"->{
                                    applications.add(currentRecord)
                                    inEntry=false
                                    currentRecord=FeedEntry()//create a new obj

                                }
                                "name"->currentRecord.name=textValue
                                "artist"->currentRecord.artist=textValue
                                "releaseDate"->currentRecord.releaseDate=textValue
                                "summery"->currentRecord.summery=textValue
                                "image"->currentRecord.imageurl=textValue

                            }
                        }
                    }




                }
                eventType=xpp.next()
            }
//            for(app in applications)
//            {
//               // Log.d(TAG,"******************")
//                Log.d(TAG,app.toString())
//            }

        }catch (e:Exception)
        {
            e.printStackTrace()
            status=false
        }
        return status

    }

}