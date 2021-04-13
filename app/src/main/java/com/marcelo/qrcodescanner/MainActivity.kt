package com.marcelo.qrcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.marcelo.qrcodescanner.databinding.ActivityMainBinding
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    var cardView1:CardView? = null
    var cardView2:CardView? = null
    var btnScan:Button? = null
    var btnEntercode:Button? = null
    var btnEnterEditCode:Button? = null
    var editCode:EditText? = null
    var tvText:TextView? = null
    var hide:Animation? = null
    var reveal:Animation? = null

    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardView1 = binding.cardView
        cardView2 = binding.cardViewEdit
        btnScan = binding.btnScan
        btnEntercode = binding.btnEnter
        btnEnterEditCode = binding.btnEditEnterCode
        editCode = binding.editTextCode
        tvText = binding.txtText


        hide = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        reveal = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)


        tvText!!.setText("Scan QR Code")
        cardView2!!.visibility = View.VISIBLE

        btnScan!!.setOnClickListener {
            tvText!!.startAnimation(reveal)
            cardView2!!.startAnimation(reveal)
            cardView1!!.startAnimation(hide)

            cardView2!!.visibility = View.VISIBLE
            cardView1!!.visibility = View.GONE
            tvText!!.setText("Scan QR Code")


        }

        cardView2!!.setOnClickListener {
            cameraTask()
        }

        btnEnterEditCode!!.setOnClickListener {
        if(editCode!!.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Por favor, digite o codigo", Toast.LENGTH_SHORT).show()
        } else {
            var value = editCode!!.text.toString()
            Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
        }
        }

        btnEntercode!!.setOnClickListener {
            tvText!!.startAnimation(reveal)
            cardView1!!.startAnimation(reveal)
            cardView2!!.startAnimation(hide)

            cardView2!!.visibility = View.GONE
            cardView1!!.visibility = View.VISIBLE
            tvText!!.setText("Enter QR Code Here")
        }
    }

    private  fun hasCameraAccess(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)
    }

    private fun cameraTask(){
        if(hasCameraAccess()) {
            var qrScanner = IntentIntegrator(this)
            qrScanner.setPrompt("scan a QR code")
            qrScanner.setCameraId(0)
            qrScanner.setOrientationLocked(true)
            qrScanner.setBeepEnabled(true)
            qrScanner.captureActivity = CaptureActivity::class.java
            qrScanner.initiateScan()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Esse app precisa do acesso a camera, para tira fotos",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){
            if(result.contents == null) {
                Toast.makeText(this, "Resultado não encontrado", Toast.LENGTH_SHORT).show()
                editCode!!.setText("")
            } else {
                try {
                    cardView1!!.startAnimation(reveal)
                    cardView2!!.startAnimation(hide)
                    cardView1!!.visibility = View.VISIBLE
                    cardView2!!.visibility = View.GONE
                    editCode!!.setText(result.contents.toString())
                } catch (exception:JSONException){
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    editCode!!.setText("")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Toast.makeText(this, "Permissão garantida", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
       if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }
}