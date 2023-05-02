package com.example.datafetchfromphp.views


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datafetchfromphp.R

class LeakActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak)

      //  ReactiveActivity.Companion.context=this

    }


}