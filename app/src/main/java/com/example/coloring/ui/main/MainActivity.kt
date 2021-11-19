package com.example.coloring.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.databinding.DataBindingUtil
import com.example.coloring.R
import com.example.coloring.databinding.ActivityMainBinding
import android.view.View.OnTouchListener
import android.graphics.Bitmap
import android.graphics.Canvas
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.ivFirst.setOnTouchListener { view, motionEvent ->
            val locations = IntArray(2)
            view.getLocationOnScreen(locations)
            Log.d("----------", "onCreate: view: ${view.getLocationOnScreen(locations)}")
            Log.d("----------", "onCreate: loc: [0] = ${locations[0]}")
            Log.d("----------", "onCreate: loc: [1] = ${locations[1]}")
            Log.d("----------", "onCreate: me:   $motionEvent")
            //view.getLocationInWindow(locations);
            false
        }
    }

    override fun onResume() {
        super.onResume()
        binding.ivFirst.startAnimation(10, 10, generateImage(10, 10))

    }

    private fun generateImage(sizeX: Int, sizeY: Int): Array<Array<Boolean>>{
        var array = arrayOf<Array<Boolean>>()
        for (x in 0 until sizeX){
            var arr = arrayOf<Boolean>()
            for(y in 0 until sizeY){
                arr += Math.random() < 0.5
            }
            array += arr

        }

//        for (arr in array) {
//            for (value in arr) {
//                print("$value ")
//            }
//            println()
//        }
        return array
    }


}