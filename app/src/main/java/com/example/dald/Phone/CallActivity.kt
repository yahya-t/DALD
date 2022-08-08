package com.example.dald.Phone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.dald.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CallActivity : AppCompatActivity() {

    lateinit var intentContactName: String
    lateinit var intentContactNumber: String

    lateinit var tvContactNameFromIntent: TextView
    lateinit var fabCall: FloatingActionButton
    private val REQUEST_CALL = 1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        // get intent values
        intentContactName = intent.getStringExtra("intentContactName").toString()
        intentContactNumber = intent.getStringExtra("intentContactNumber").toString()

        // assign intentContactName to the TextView
        tvContactNameFromIntent = findViewById(R.id.tv_ContactNameFromIntent)
        tvContactNameFromIntent.text = intentContactName

        fabCall = findViewById(R.id.fab_Call)

        fabCall.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                makePhoneCall()
            }

        })
    }

    fun makePhoneCall() {
        var number = intentContactNumber

        if (number.trim().isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
            } else {
                var dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Sorry, number is invalid....", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun returnToContacts(view: View) {
        val intent = Intent(this, PhoneMainActivity::class.java)
        finish()
        startActivity(intent)
    }
}