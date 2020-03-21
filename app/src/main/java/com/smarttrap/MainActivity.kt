package com.smarttrap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smarttrap.Adapters.Image_adapter
import com.smarttrap.Utills.Item
import kotlinx.android.synthetic.main.activity_detail_show.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.any_chart_view
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class MainActivity : AppCompatActivity(){


    lateinit var items:ArrayList<DataSnapshot>
    lateinit var Adapter:Image_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        items= ArrayList()

        Adapter=Image_adapter(items,this)
        var lm:LinearLayoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        recylerView.layoutManager=lm
        recylerView.adapter=Adapter


        load_char_data()

        //startActivity(Intent(this,MapsActivity::class.java))






        val storage = FirebaseStorage.getInstance()
        val listRef = storage.reference.child("image")

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("data")



        myRef.addChildEventListener(object :ChildEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.e("Files__",p0.child("url").getValue().toString())
                items.add(0,p0)
                Adapter.notifyDataSetChanged()

            }
        })





//        listRef.listAll()
//            .addOnSuccessListener { listResult ->
//                listResult.prefixes.forEach { prefix ->
//
//                    // All the prefixes under listRef.
//                    // You may call listAll() recursively on them.
//                }
//
//                listResult.items.forEach { item ->
//                    // All the items under listRef.
//
//                    item.downloadUrl.addOnSuccessListener {unit->
//                        Log.e("Files",unit.toString())
//                        items.add(Item(url = unit.toString(),text = item.name))
//                        Adapter.notifyDataSetChanged()
//                    }
//
//                    //log("Files",item.downloadUrl)
//                }
//            }
//            .addOnFailureListener {
//                // Uh-oh, an error occurred!
//            }
    }



    fun load_char_data(){
        val anyChartView: AnyChartView =any_chart_view
        val cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)


        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)





        val seriesData = ArrayList<DataEntry>()

        seriesData.add(CustomDataEntry("1", 10, 18.0, 21.0))
        seriesData.add(CustomDataEntry("2", 10, 23.3, 20.3))
        seriesData.add(CustomDataEntry("3", 19, 24.7, 19.2))
        seriesData.add(CustomDataEntry("4", 20, 18.0, 21.0))
        seriesData.add(CustomDataEntry("5", 29, 23.3, 20.3))
        seriesData.add(CustomDataEntry("6", 10, 24.7, 19.2))
        seriesData.add(CustomDataEntry("7", 10, 18.0, 21.0))
        seriesData.add(CustomDataEntry("8", 13, 23.3, 20.3))
        seriesData.add(CustomDataEntry("9", 14, 24.7, 19.2))
        seriesData.add(CustomDataEntry("10", 17, 18.0, 21.0))

        seriesData.add(CustomDataEntry("11", 12, 18.0, 21.0))
        seriesData.add(CustomDataEntry("12", 17, 23.3, 20.3))











            FirebaseDatabase.getInstance().getReference().child("data").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    Log.e("mydata", p0.toString())
                }

                override fun onDataChange(p0: DataSnapshot) {

                    for(doc in p0.children){
                        var document=doc
                        val month="02"
                        val year="2020"
                        for (i in 1..30){
                            var day=i
                            var sday:String=day.toString()
                            if(day<10){
                                sday="0"+day
                            }
                            var date= year+"-"+month+"-"+ sday


                            if(  document.child("counts").child(date).exists()){
                                Log.e("mydata", "day "+sday)
                                var c=CustomDataEntry(i.toString(), Integer.parseInt(document.child("counts").child(date).child("count").value.toString()), 2.3, 2.8)
                                seriesData.add(c)

                                Log.e("mydata", document.child("counts").child(date).child("count").value.toString())
                            }else{
                              //  Log.e("mydata", "not esists ")

                            }

                        }
                    }

                }

            })







        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")

        val series1 = cartesian.line(series1Mapping)
        series1.name("Army Cutworm")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)

        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)



//        val series2 = cartesian.line(series1Mapping)
//        series1.name("WISKY")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers()
//            .type(MarkerType.CIRCLE)
//            .size(4.0)
//        series2.tooltip()
//            .position("right")
//            .anchor(Anchor.LEFT_CENTER)
//            .offsetX(5.0)
//            .offsetY(5.0)
//
//        val series3 = cartesian.line(series1Mapping)
//        series1.name("WOODKA")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers()
//            .type(MarkerType.CIRCLE)
//            .size(4.0)
//        series3.tooltip()
//            .position("right")
//            .anchor(Anchor.RIGHT_CENTER)
//            .offsetX(5.0)
//            .offsetY(5.0)





        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        anyChartView.setChart(cartesian)
    }



    private inner class CustomDataEntry internal constructor(
        x: String,
        value: Number,
        value2: Number,
        value3: Number
    ) : ValueDataEntry(x, value) {

        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }

    }

}
