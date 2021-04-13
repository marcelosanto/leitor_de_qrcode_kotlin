package com.marcelo.qrcodescanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.marcelo.qrcodescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var cardView1:CardView? = null
    var cardView2:CardView? = null
    var btnScan:Button? = null
    var btnEntercode:Button? = null
    var editCode:EditText? = null
    var tvText:TextView? = null

    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardView1 = binding.cardView
        cardView2 = binding.cardViewEdit
        btnScan = binding.btnScan
        btnEntercode = binding.btnEnter
        editCode = binding.editTextCode
        tvText = binding.txtText

        tvText!!.setText("Scan QR Code")
        cardView2!!.visibility = View.VISIBLE

        btnScan!!.setOnClickListener {
            cardView2!!.visibility = View.VISIBLE
            cardView1!!.visibility = View.GONE
            tvText!!.setText("Scan QR Code")
        }

        btnEntercode!!.setOnClickListener {
            cardView2!!.visibility = View.GONE
            cardView1!!.visibility = View.VISIBLE
            tvText!!.setText("Enter QR Code Here")
        }
    }
}