package com.example.coloring.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.coloring.R
import com.example.coloring.databinding.ActivityMainBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.example.coloring.databinding.DialogSizeBinding
import com.example.coloring.ui.model.Coordinates
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var resultDialog: AlertDialog? = null
    private lateinit var bindingDialog: DialogSizeBinding

    //custom view width and height
    private var widthFirst: Int = 0
    private var heightFirst: Int = 0

    //initial width and height of custom view
    private var widthInitial: Int = 0
    private var heightInitial: Int = 0

    //bitmap size in that custom view
    private var bitmapSize: Int = 20

    //pressed coordinates of x and y in First custom view
    private var x: Int = 0
    private var y: Int = 0

    //size of custom view -- counts of bitmpas
    private var x1: Int = 0
    private var y1: Int = 0
    private lateinit var arrayOfFirst: Array<Array<Boolean>>
    private lateinit var arrayOfSecond: Array<Array<Boolean>>
    private lateinit var array: ArrayList<Coordinates>
    private lateinit var arrayContains: ArrayList<Coordinates>


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingDialog = DialogSizeBinding.inflate(LayoutInflater.from(this))
        arrayContains = arrayListOf()
        array = arrayListOf()

        binding.btnGenerate.setOnClickListener {
            x1 = binding.giFirst.measuredWidth / bitmapSize
            y1 = binding.giFirst.measuredHeight / bitmapSize
//            Log.d("----------", "onResume: x1: $x1")
//            Log.d("----------", "onResume: y1: $y1")
//            Log.d("----------", "onResume: x2: $x2")
//            Log.d("----------", "onResume: y2: $y2")
//            Log.d("----------", "onResume: ww: ${binding.giFirst.measuredWidth}")
//            Log.d("----------", "onResume: hh: ${binding.giFirst.measuredHeight}")
            arrayOfFirst = generateImage(x1, y1)
            arrayOfSecond = generateImage(x1, y1)
            binding.giFirst.startAnimation(arrayOfFirst, bitmapSize.toFloat())
            binding.giSecond.startAnimation(arrayOfSecond, bitmapSize.toFloat())
        }
        binding.btnSize.setOnClickListener {
            dismissDialog()
            if (bindingDialog.root.parent != null) {
                (bindingDialog.root.parent as ViewGroup).removeView(bindingDialog.root)
            }
            showDialog()
        }

        binding.giFirst.setOnTouchListener { _, motionEvent ->
            Log.d("----------", "onCreate: me:   $motionEvent")

            x = motionEvent.x.toInt() / bitmapSize
            y = motionEvent.y.toInt() / bitmapSize
            arrayContains.add(Coordinates(x, y))
            array.add(Coordinates(x, y))
            Log.d("----------", "onCreate: x: $x  y: $y")
            addToArray(x, y, arrayOfFirst, array, arrayContains)
            false
        }

    }

    private fun addToArray(
        x: Int,
        y: Int,
        arrayOfBoolens: Array<Array<Boolean>>,
        array: ArrayList<Coordinates>,
        arrayContains: ArrayList<Coordinates>
    ) {

        while (array.isNotEmpty()) {
            val initial: Boolean = arrayOfBoolens[array[0].x][array[0].y]//white or black
            //top
            if (y - 1 > -1)
                if (initial == arrayOfBoolens[x][y - 1] && !arrayContains.contains(Coordinates(x, y-1))) {
                    array.add(Coordinates(x, y - 1))
                    arrayContains.add(Coordinates(x, y - 1))
                    Log.d("----------", "addToArray:    top: [$x,${y-1}]")
                }
            //bottom
            if (y + 1 < y1)
                if (initial == arrayOfBoolens[x][y + 1] && !arrayContains.contains(Coordinates(x, y+1))){
                    array.add(Coordinates(x, y + 1))
                    arrayContains.add(Coordinates(x, y + 1))
                    Log.d("----------", "addToArray: bottom: [$x,${y+1}]")
                }
            //left
            if (x - 1 > -1)
                if (initial == arrayOfBoolens[x - 1][y] && !arrayContains.contains(Coordinates(x-1, y))){
                    array.add(Coordinates(x - 1, y))
                    arrayContains.add(Coordinates(x - 1, y))
                    Log.d("----------", "addToArray:   left: [${x-1},$y]")
                }
            //right
            if (x + 1 < x1)
                if (initial == arrayOfBoolens[x + 1][y] && !arrayContains.contains(Coordinates(x+1, y))){
                    array.add(Coordinates(x + 1, y))
                    arrayContains.add(Coordinates(x + 1, y))
                    Log.d("----------", "addToArray:  right: [${x+1},$y]")
                }

            array.remove(Coordinates(x, y))
            Log.d("----------", "addToArray: array: $array [$x,$y]")
        }
//        arrayContains.clear()
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
                if (widthInitial == 0 && heightInitial == 0) {
                    widthInitial = binding.giFirst.measuredWidth
                    heightInitial = binding.giFirst.measuredHeight
                }

                if (bindingDialog.etSizeX.text.toString()
                        .toInt() >= widthInitial
                ) bindingDialog.etSizeX.error =
                    "Please choose smaller number under $widthInitial"
                else {
                    widthFirst = bindingDialog.etSizeX.text.toString().toInt()
                    if (bindingDialog.etSizeY.text.toString()
                            .toInt() >= heightInitial
                    ) {
                        bindingDialog.etSizeY.error =
                            "Please choose smaller number under $heightInitial"
                    } else {
                        heightFirst = bindingDialog.etSizeY.text.toString().toInt()
                        if (widthFirst <= widthInitial && heightFirst <= heightInitial) {
                            val layout = LinearLayout.LayoutParams(widthFirst, heightFirst)
                            layout.setMargins(
                                (16 * dp).toInt(),
                                (8 * dp).toInt(),
                                (16 * dp).toInt(),
                                8
                            )
                            binding.giFirst.layoutParams = layout
                            binding.giSecond.layoutParams = layout
                            dismissDialog()
                        }
                    }
                }
            }
        }
        bindingDialog.btnCancel.setOnClickListener { dismissDialog() }
        resultDialog = AlertDialog.Builder(this)
            .setView(bindingDialog.root)
            .setCancelable(true)
            .show()
    }

}