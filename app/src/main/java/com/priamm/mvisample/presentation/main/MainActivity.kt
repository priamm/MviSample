package com.priamm.mvisample.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.priamm.mvisample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
    }
}
