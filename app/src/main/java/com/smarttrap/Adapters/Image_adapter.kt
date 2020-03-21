package com.smarttrap.Adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.smarttrap.DetailShow
import com.smarttrap.R
import com.smarttrap.Utills.Item
import com.smarttrap.Utills.Utilss
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


public class Image_adapter :RecyclerView.Adapter<Image_adapter.ViewHolder>{

    lateinit var list:ArrayList<DataSnapshot>
    lateinit var C:Context;

    constructor(list: ArrayList<DataSnapshot>, C: Context) : super() {
        this.list = list
        this.C = C
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v=LayoutInflater.from(C).inflate(R.layout.item_image,parent,false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Picasso.get().load(list.get(position).child("url").getValue().toString()).centerCrop().resize(500,500).into(holder.imageview)

       // var time=list.get(position).child("time").getValue().toString().split(".")[0].toLong()
        holder.txt.setText(Utilss.getDate(list.get(position).child("time").getValue().toString()))
        holder.txttitle.setText(list.get(position).child("name").getValue().toString())


      var counts=  list.get(position).child("counts").children
        var total:Int=0;

        for (doc in counts){

            total+=  Integer.parseInt(doc.child("count").getValue().toString())
        }

        holder.txtcount.setText("total found "+ total)




        holder.imageview.setOnClickListener({

            C.startActivity(Intent(C,DetailShow::class.java).putExtra("id",list.get(position).key).putExtra("name",list.get(position).child("name").getValue().toString()))

        })
    }



    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        var imageview: ImageView =view.imageView
        var txt:TextView=view.textView
        var txttitle:TextView=view.textView1
        var txtcount:TextView=view.textView3

    }
}