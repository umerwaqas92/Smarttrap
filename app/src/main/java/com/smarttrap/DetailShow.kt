package com.smarttrap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smarttrap.Utills.Utilss
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_show.*
import com.anychart.enums.TooltipPositionMode
import android.R.attr.animation
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.anychart.graphics.vector.Stroke
import com.anychart.chart.common.dataentry.DataEntry
import androidx.core.content.ContextCompat.getSystemService
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.MarkerType

import com.anychart.enums.Anchor
import kotlin.math.abs
import kotlin.random.Random


class DetailShow : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_show)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        FirebaseDatabase.getInstance().getReference("data").child(intent.getStringExtra("id"))
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    Picasso.get().load(p0.child("url").getValue().toString()).into(img_show)
                    textView2.append("Device Id :"+p0.child("device_id").getValue().toString())
                    textView2.append("\nTime Taken :"+Utilss.getDate(p0.child("time").getValue().toString()))
                    textView2.append("\nlocation :latitude"+p0.child("location").child("latitude").getValue().toString())
                    textView2.append(",longitude"+p0.child("location").child("longitude").getValue().toString())
                    textView2.append("\ncountry_name"+p0.child("location").child("country_name").getValue().toString())

                    if(mMap!=null){

                        val sydney = LatLng(p0.child("location").child("latitude").getValue().toString().toDouble(), p0.child("location").child("longitude").getValue().toString().toDouble())
                        mMap.addMarker(MarkerOptions().position(sydney).title("Device "+p0.child("device_id").getValue().toString()))
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                sydney, 12.0f
                            )
                        )


                    }

                }
            })


        val anyChartView:AnyChartView =any_chart_view
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


        var total=0
        var lastrandom=1

        for (i in 1..13){
           var newrandom= Random.nextInt(1,20)


            total+=newrandom
            seriesData.add(CustomDataEntry(i.toString(),newrandom, 2.3, 2.8))
        }


        textView2.setText("Total number  "+total+"\n")





        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")


        val series1 = cartesian.line(series1Mapping)
        series1.name(intent.getStringExtra("name"))
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
//        series1.name("Black Cutworm")
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
//        series1.name("Codling Moth")
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

    override fun onMapReady(g: GoogleMap?) {
        mMap = g!!


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
