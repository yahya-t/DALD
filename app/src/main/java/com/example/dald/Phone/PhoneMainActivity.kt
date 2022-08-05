package com.example.dald.Phone

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dald.MainActivity
import com.example.dald.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.*
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

class PhoneMainActivity : AppCompatActivity() {

    lateinit var rvContacts: RecyclerView
    lateinit var contactsList: ArrayList<ContactModel>
    lateinit var contactAdapter: ContactAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_main)

        // hide system ui
        hideSystemUI()
        supportActionBar?.hide()

        rvContacts = findViewById(R.id.rv_Contacts)
        rvContacts.setHasFixedSize(true)
        rvContacts.layoutManager = LinearLayoutManager(this)

        contactsList = ArrayList()

        contactAdapter = ContactAdapter(this, contactsList)
        rvContacts.adapter = contactAdapter

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if (response?.permissionName.equals(Manifest.permission.READ_CONTACTS)) {
                        getContacts()
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@PhoneMainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?,token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()

    }

    @SuppressLint("Range")
    fun getContacts() {
        var phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        while (phones?.moveToNext() == true) {
            var name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            var numberUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

            var contact = ContactModel(name, number, numberUri)
            contactsList.add(contact)
            contactAdapter.notifyDataSetChanged()
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

    fun returnHomeFromPhone(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}