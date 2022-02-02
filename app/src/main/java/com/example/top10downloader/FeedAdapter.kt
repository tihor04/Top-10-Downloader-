package com.example.top10downloader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedAdapter (context: Context,private  var resource:Int,private val applications:List<FeedEntry>):
    ArrayAdapter<FeedEntry>(context,resource,applications){
    private val TAG="FeedAdapter"


    private val inflater=LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d(TAG,"getView called")
       val view= inflater.inflate(resource,parent,false)

        val tvname:TextView=view.findViewById(R.id.tv_name)
        val tvartist:TextView=view.findViewById(R.id.tv_artist)
        val tvinfo :TextView=view.findViewById(R.id.tv_info)

        val currentapplication =applications[position]

        tvname.text=currentapplication.name
        tvartist.text=currentapplication.artist
        tvinfo.text=currentapplication.summery

        return view
    }

    override fun getCount(): Int {
        Log.d(TAG,"getCount called")
        return applications.size
    }
}