package com.vidhey.viewmodelapicall.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vidhey.viewmodelapicall.ViewModel.MainViewModel
import com.vidhey.viewmodelapicall.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]


         viewModel.data.observe(this, Observer { hellow.text = it[0].CatalogueId })

    }

}
