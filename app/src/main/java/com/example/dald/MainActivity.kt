package com.example.dald

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.example.dald.Clock.ClockMainActivity
import com.example.dald.Game.GameMainActivity
import com.example.dald.Photos.*
import com.example.dald.Music.MusicMainActivity
import com.example.dald.Notes.NotesMainActivity
import com.example.dald.Phone.PhoneMainActivity
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var dbUser: UserDatabase = UserDatabase(this)

    // array to hold the permission required for the app
    var PERMISSIONS = emptyArray<String>()

    // View variables
    lateinit var tvUserName: TextView
    lateinit var etUserName: TextView
    lateinit var btnConfirmUserName: Button

    // date and time variables
    lateinit var tvMainDate: TextView
    lateinit var dateTime: String
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        // add permissions to the array
        PERMISSIONS += listOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE)

        // check and request permissions
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1)
        }

        // initialise views
        tvUserName = findViewById(R.id.tv_UserName)
        etUserName = findViewById(R.id.et_UserName)
        btnConfirmUserName = findViewById(R.id.btn_ConfirmUserName)

        //assign name from database if exists
        if (dbUser.getUser().userName == null || dbUser.getUser().userName == "") {
            dbUser.deleteUserDataDB()
            dbUser.addUserNameDB("Hello!")
            tvUserName.text = dbUser.getUser().userName
        } else {
            tvUserName.text = "Hi ${dbUser.getUser().userName}"
        }

        tvMainDate = findViewById(R.id.tv_MainDate)

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("EEEE\nd LLLL yyyy")
        dateTime = simpleDateFormat.format(calendar.time).toString()
        tvMainDate.text = dateTime

        // onLongClickListener to change user name
        tvUserName.setOnLongClickListener {
            // adjust visibility of views
            tvUserName.isVisible = false
            etUserName.isVisible = true
            btnConfirmUserName.isVisible = true

            etUserName.text = ""

            // when confirm button is clicked
            btnConfirmUserName.setOnClickListener {
                closeKeyboard()
                dbUser.deleteUserDataDB()
                dbUser.addUserNameDB("${etUserName.text}")

                if (dbUser.getUser().userName == "") {
                    tvUserName.text = "Hello!"
                } else {
                    tvUserName.text = "Hi ${dbUser.getUser().userName}"
                }

                tvUserName.isVisible = true
                etUserName.isVisible = false
                btnConfirmUserName.isVisible = false
            }
            true
        }

    }


    private fun closeKeyboard() {
        var view = this.currentFocus
        if (view != null) {
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun hasPermissions(context: Context, PERMISSIONS: Array<String>): Boolean {
        if (context != null && PERMISSIONS != null) {
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read External Storage Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Read External Storage Permission is denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == 1) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Read Contacts Permission is denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (requestCode == 1) {
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Phone Call Permission is granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Phone Call Permission is denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun galleryCard(view: View) {
        val intent = Intent(this, PhotoMainActivity::class.java)
        startActivity(intent)
    }

    fun musicPlayerCard(view: View) {
        val intent = Intent(this, MusicMainActivity::class.java)
        startActivity(intent)
    }

    fun notesCard(view: View) {
        val intent = Intent(this, NotesMainActivity::class.java)
        startActivity(intent)
    }

    fun clockCard(view: View) {
        val intent = Intent(this, ClockMainActivity::class.java)
        startActivity(intent)
    }

    fun phoneCard(view: View) {
        val intent = Intent(this, PhoneMainActivity::class.java)
        startActivity(intent)
    }

    fun gameCard(view: View) {
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}