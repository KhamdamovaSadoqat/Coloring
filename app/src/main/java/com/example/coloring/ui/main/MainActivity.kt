package com.example.coloring.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.coloring.R
import com.example.coloring.databinding.ActivityMainBinding
import android.graphics.Point
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.coloring.databinding.DialogSizeBinding
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var resultDialog: AlertDialog? = null
    private lateinit var bindingDialog: DialogSizeBinding
    private var widthFirst: Int = 0
    private var heightFirst: Int = 0
    private var widthInitial: Int = 0
    private var heightInitial: Int = 0
    private var bitmapSize: Int = 20
    private var x: Int =0
    private var y: Int =0



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingDialog = DialogSizeBinding.inflate(LayoutInflater.from(this))

        binding.giFirst.setOnTouchListener { view, motionEvent ->
            val locations = IntArray(2)
            view.getLocationOnScreen(locations)
            Log.d("----------", "onCreate: view: ${view.getLocationOnScreen(locations)}")
            Log.d("----------", "onCreate: loc: [0] = ${locations[0]}")
            Log.d("----------", "onCreate: loc: [1] = ${locations[1]}")
            Log.d("----------", "onCreate: me:   $motionEvent")

            x = motionEvent.x.toInt() % 40
            y = motionEvent.y.toInt() % 40

            Log.d("----------", "onCreate: x: $x  y: $y")

            false
        }

    }

    override fun onResume() {
        super.onResume()

        binding.btnGenerate.setOnClickListener {
            val x1 = binding.giFirst.measuredWidth / bitmapSize
            val y1 = binding.giFirst.measuredHeight / bitmapSize
            val x2 = binding.giSecond.measuredWidth / bitmapSize
            val y2 = binding.giSecond.measuredHeight / bitmapSize
//            Log.d("----------", "onResume: x1: $x1")
//            Log.d("----------", "onResume: y1: $y1")
//            Log.d("----------", "onResume: x2: $x2")
//            Log.d("----------", "onResume: y2: $y2")
//            Log.d("----------", "onResume: ww: ${binding.giFirst.measuredWidth}")
//            Log.d("----------", "onResume: hh: ${binding.giFirst.measuredHeight}")
            binding.giFirst.startAnimation(generateImage(x1, y1), bitmapSize.toFloat())
            binding.giSecond.startAnimation(generateImage(x2, y2), bitmapSize.toFloat())
        }
        widthFirst = binding.giFirst.measuredWidth
        heightFirst = binding.giFirst.measuredHeight
        binding.btnSize.setOnClickListener {
            dismissDialog()
            if (bindingDialog.root.parent != null) {
                (bindingDialog.root.parent as ViewGroup).removeView(bindingDialog.root)
            }
            showDialog()
        }
    }

    private fun generateImage(sizeX: Int, sizeY: Int): Array<Array<Boolean>> {
        var array = arrayOf<Array<Boolean>>()
        for (x in 0 until sizeX) {
            var arr = arrayOf<Boolean>()
            for (y in 0 until sizeY) arr += Math.random() < 0.5
            array += arr
        }
        return array
    }

    private fun dismissDialog() {
        if (resultDialog != null) {
            resultDialog!!.dismiss()

        }
    }

    private fun showDialog() {
        val dp = resources.displayMetrics.density
        bindingDialog.btnOk.setOnClickListener {
            if (!bindingDialog.etSizeX.text.isNullOrEmpty() && !bindingDialog.etSizeY.text.isNullOrEmpty()) {
                if(widthInitial==0 && heightInitial==0){
                    widthInitial = binding.giFirst.measuredWidth
                    heightInitial = binding.giFirst.measuredHeight
                }

                if (bindingDialog.etSizeX.text.toString()
                        .toInt() >= widthInitial
                ) bindingDialog.etSizeX.error =
                    "Please choose smaller number under $widthInitial"
                else{
                    widthFirst = bindingDialog.etSizeX.text.toString().toInt()
                    if (bindingDialog.etSizeY.text.toString()
                            .toInt() >= heightInitial
                    ) {
                        bindingDialog.etSizeY.error =
                            "Please choose smaller number under $heightInitial"
                    } else{
                        heightFirst = bindingDialog.etSizeY.text.toString().toInt()
                        if(widthFirst <= widthInitial && heightFirst <= heightInitial){
                            val layout = LinearLayout.LayoutParams(widthFirst, heightFirst)
                            layout.setMargins((16*dp).toInt(), (8*dp).toInt(), (16*dp).toInt(), 8)
                            binding.giFirst.layoutParams = layout
                            binding.giSecond.layoutParams = layout
                            dismissDialog()
                        }
                    }
                }
            }
        }
        bindingDialog.btnCancel.setOnClickListener { dismissDialog()}
        resultDialog = AlertDialog.Builder(this)
            .setView(bindingDialog.root)
            .setCancelable(true)
            .show()
    }

}