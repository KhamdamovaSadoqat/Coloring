package com.example.coloring.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.coloring.R
import com.example.coloring.databinding.ActivityMainBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private var xPressedFirst: Int = 0
    private var yPressedFirst: Int = 0

    //pressed coordinates of x and y in Second custom view
    private var xPressedSecond: Int = 0
    private var yPressedSecond: Int = 0

    //size of custom view -- counts of bitmaps
    private var x: Int = 0
    private var y: Int = 0
    private lateinit var arrayOfFirst: Array<Array<Int>>
    private lateinit var arrayOfSecond: Array<Array<Int>>
    private lateinit var arrayNeedToCheck: ArrayList<Coordinates>
    private lateinit var arrayContains: ArrayList<Coordinates>

    //color list
    lateinit var colorList: ArrayList<String>
    var firstReplacementColor: Int = 0
    var secondReplacementColor: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        bindingDialog = DialogSizeBinding.inflate(LayoutInflater.from(this))
        arrayContains = arrayListOf()
        arrayNeedToCheck = arrayListOf()
        spinnerColor()
        setUp()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setUp(){
        binding.giFirst.setOnTouchListener { _, motionEvent ->
            Log.d("----------", "onCreate: me:   $motionEvent")

            xPressedFirst = motionEvent.x.toInt() / bitmapSize
            yPressedFirst = motionEvent.y.toInt() / bitmapSize

            Log.d("----------", "onCreate: pressed x: $xPressedFirst y: $yPressedFirst")
            Log.d("----------", "onCreate: x: ${xPressedFirst * bitmapSize} y: ${yPressedFirst * bitmapSize}")
            Log.d("----------", "onCreate: x: ${motionEvent.x}  y: ${motionEvent.y}")

            if (arrayOfFirst[xPressedFirst][yPressedFirst] != firstReplacementColor) {
                arrayContains.add(Coordinates(xPressedFirst, yPressedFirst))
                arrayNeedToCheck.add(Coordinates(xPressedFirst, yPressedFirst))
                arrayOfFirst = addToArray(
                    arrayOfFirst,
                    arrayNeedToCheck,
                    arrayContains,
                    arrayOfFirst[xPressedFirst][yPressedFirst],
                    firstReplacementColor
                )
                binding.giFirst.startAnimation(arrayOfFirst, bitmapSize.toFloat())
            }
            false
        }

        binding.giSecond.setOnTouchListener { _, motionEvent ->
            xPressedSecond = motionEvent.x.toInt() / bitmapSize
            yPressedSecond= motionEvent.y.toInt() / bitmapSize

            if (arrayOfSecond[xPressedSecond][yPressedSecond] != secondReplacementColor) {
                arrayContains.add(Coordinates(xPressedSecond, yPressedSecond))
                arrayNeedToCheck.add(Coordinates(xPressedSecond, yPressedSecond))
                arrayOfSecond = addToArray(
                    arrayOfSecond,
                    arrayNeedToCheck,
                    arrayContains,
                    arrayOfSecond[xPressedSecond][yPressedSecond],
                    secondReplacementColor
                )
                binding.giSecond.startAnimation(arrayOfSecond, bitmapSize.toFloat())
            }
            false
        }

        binding.btnSize.setOnClickListener {
            dismissDialog()
            if (bindingDialog.root.parent != null) {
                (bindingDialog.root.parent as ViewGroup).removeView(bindingDialog.root)
            }
            showDialog()
        }

        binding.btnGenerate.setOnClickListener {
            x = binding.giFirst.measuredWidth / bitmapSize
            y = binding.giFirst.measuredHeight / bitmapSize
            val dp = resources.displayMetrics.density
            val layout = LinearLayout.LayoutParams(bitmapSize*x, bitmapSize*y)
            layout.setMargins(
                (16 * dp).toInt(),
                (8 * dp).toInt(),
                (16 * dp).toInt(),
                8
            )
            binding.giFirst.layoutParams = layout
            binding.giSecond.layoutParams = layout
            Log.d("----------", "onCreate: generated x: $x y: $y")
            arrayOfFirst = generateImage(x, y)
            arrayOfSecond = copy(arrayOfFirst)
            binding.giFirst.startAnimation(arrayOfFirst, bitmapSize.toFloat())
            binding.giSecond.startAnimation(arrayOfSecond, bitmapSize.toFloat())
        }
    }

    private fun copy(list: Array<Array<Int>>): Array<Array<Int>>{
        var array = arrayOf<Array<Int>>()
        for (element in list) {
            var arr = arrayOf<Int>()
            for (yin in list[0].indices){
                arr += element[yin]
            }
            array += arr
        }
        return array
    }

    fun print(list: Array<Array<Int>>) {
        list.forEach { x ->
            x.forEach { y ->
                print(y)
                print(" ")
            }
            println()
        }
    }

    private fun addToArray(
        arrayOfColors: Array<Array<Int>>,
        arrayNeedToCheck: ArrayList<Coordinates>,
        arrayContains: ArrayList<Coordinates>,
        initialColor: Int,
        replacementColor: Int
    ): Array<Array<Int>> {
        while (arrayNeedToCheck.isNotEmpty()) {
            val x: Int = arrayNeedToCheck[0].x
            val y: Int = arrayNeedToCheck[0].y
            Log.d("----------", "addToArray: x: $x  y: $y")
            Log.d("----------", "addToArray: color: $initialColor")
            arrayOfColors[x][y] = replacementColor


            //top
            if (y - 1 > -1)
                if (initialColor == arrayOfColors[x][y - 1] && !arrayContains.contains(
                        Coordinates(
                            x,
                            y - 1
                        )
                    )
                ) {
                    arrayNeedToCheck.add(Coordinates(x, y - 1))
                    arrayContains.add(Coordinates(x, y - 1))
                    arrayOfColors[x][y - 1] = replacementColor
                    Log.d("----------", "addToArray:    top: [$x,${y - 1}]")

                }
            //bottom
            if (y + 1 < this.y)
                if (initialColor == arrayOfColors[x][y + 1] && !arrayContains.contains(
                        Coordinates(
                            x,
                            y + 1
                        )
                    )
                ) {
                    arrayNeedToCheck.add(Coordinates(x, y + 1))
                    arrayContains.add(Coordinates(x, y + 1))
                    arrayOfColors[x][y + 1] = replacementColor
                    Log.d("----------", "addToArray: bottom: [$x,${y + 1}]")
                }
            //left
            if (x - 1 > -1)
                if (initialColor == arrayOfColors[x - 1][y] && !arrayContains.contains(
                        Coordinates(
                            x - 1,
                            y
                        )
                    )
                ) {
                    arrayNeedToCheck.add(Coordinates(x - 1, y))
                    arrayContains.add(Coordinates(x - 1, y))
                    arrayOfColors[x - 1][y] = replacementColor
                    Log.d("----------", "addToArray:   left: [${x - 1},$y]")
                }
            //right
            if (x + 1 < this.x)
                if (initialColor == arrayOfColors[x + 1][y] && !arrayContains.contains(
                        Coordinates(
                            x + 1,
                            y
                        )
                    )
                ) {
                    arrayNeedToCheck.add(Coordinates(x + 1, y))
                    arrayContains.add(Coordinates(x + 1, y))
                    arrayOfColors[x + 1][y] = replacementColor
                    Log.d("----------", "addToArray:  right: [${x + 1},$y]")
                }
            arrayNeedToCheck.remove(Coordinates(x, y))
            Log.d("----------", "addToArray: array: $arrayNeedToCheck [$x,$y]")
        }
        arrayContains.clear()
        return arrayOfColors
    }

    private fun generateImage(sizeX: Int, sizeY: Int): Array<Array<Int>> {
        var array = arrayOf<Array<Int>>()
        for (x in 0 until sizeX) {
            var arr = arrayOf<Int>()
            for (y in 0 until sizeY) arr += (0..1).random()

            array += arr
        }
        return array
    }

    private fun spinnerColor() {
        colorList = arrayListOf()
        colorList.add(this.getString(R.string.black))
        colorList.add(this.getString(R.string.white))
        colorList.add(this.getString(R.string.blue))
        colorList.add(this.getString(R.string.red))
        colorList.add(this.getString(R.string.yellow))
        colorList.add(this.getString(R.string.green))
        colorList.add(this.getString(R.string.purple))

        val arrayAdapterFirst: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorList)
        arrayAdapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAlgorithm1.adapter = arrayAdapterFirst
        binding.spAlgorithm1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                firstReplacementColor = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val arrayAdapterSecond: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colorList)
        arrayAdapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAlgorithm2.adapter = arrayAdapterSecond
        binding.spAlgorithm2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                secondReplacementColor = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
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