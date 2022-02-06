package com.example.top10downloader

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ViewHolder(v:View){
    val tvname:TextView=v.findViewById(R.id.tv_name)
    val tvartist:TextView=v.findViewById(R.id.tv_artist)
    val tvinfo :TextView=v.findViewById(R.id.tv_info)
}


class FeedAdapter (context: Context,private  var resource:Int,private val applications:List<FeedEntry>):
    ArrayAdapter<FeedEntry>(context,resource,applications){



    private val inflater=LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        val view :View

        val viewholder:ViewHolder

        if(convertView== null){

            view=inflater.inflate(resource,parent,false)
            viewholder=ViewHolder(view)
            view.tag =viewholder

        }
        else{

            view=convertView
            viewholder=view.tag as ViewHolder
        }


        val currentapplication =applications[position]

        viewholder.tvname.text=currentapplication.name
        viewholder.tvartist.text=currentapplication.artist
        viewholder.tvinfo.text=currentapplication.summery

        return view
    }

    override fun getCount(): Int {

        return applications.size
    }
}