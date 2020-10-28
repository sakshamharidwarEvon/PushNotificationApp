package com.example.pushnotificationfirebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var editTextName: EditText? = null
    var buttonAdd: Button? = null
    var spinnerGenres: Spinner? = null
    var buttonDelete: Button? = null
    var databaseArtists: DatabaseReference? = null
    var listViewArtists: ListView? = null
    var artistList: MutableList<Artist>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseArtists = FirebaseDatabase.getInstance().getReference("Artist")
        editTextName = findViewById<View>(R.id.search_edit_text) as EditText
        buttonAdd = findViewById<View>(R.id.button) as Button
        spinnerGenres = findViewById<View>(R.id.spinner) as Spinner
        listViewArtists = findViewById<View>(R.id.list_view_artist) as ListView
        buttonDelete = findViewById<View>(R.id.buttonClear) as Button
        artistList = ArrayList()
        buttonAdd!!.setOnClickListener { addArtist() }
        buttonDelete!!.setOnClickListener {
            //deleteArtists(Artist.getArtistId());
        }
    }

    private fun addArtist() {
        val name = editTextName!!.text.toString().trim { it <= ' ' }
        val genre = spinnerGenres!!.selectedItem.toString()
        if (!TextUtils.isEmpty(name)) {
            val id: String = databaseArtists?.push()?.getKey()!!
            val artist = Artist(id, name, genre)
            databaseArtists?.child(id)?.setValue(artist)
            Toast.makeText(this, "Artist Added", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show()
        }
    }


    private fun deleteArtists(artistId: String) {
        val drArtist: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Artist").child(artistId)
        drArtist.removeValue()
        Toast.makeText(this, "Database Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        databaseArtists?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {


                val name = editTextName!!.text.toString()

                if (name.isEmpty()) {
                    Toast.makeText(applicationContext, "please fill", Toast.LENGTH_LONG).show()
                } else {
                    notification()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        databaseArtists?.addValueEventListener(object : ValueEventListener() {
            override fun onDataChange(snapshot: DataSnapshot) {
                artistList!!.clear()

                for (artistSnapshot in snapshot.children) {
                    val artist = artistSnapshot.getValue(Artist::class.java)
                    artistList!!.add(artist!!)
                }
                val adapter = ArtistList(this@MainActivity, artistList!!)
                listViewArtists!!.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }}

//    private fun notification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
//            val manager = getSystemService(
//                NotificationManager::class.java
//            )
//            manager.createNotificationChannel(channel)
//        }
//        val resultIntent = Intent(this, MainActivity::class.java)
//        val resultPendingIntent =
//            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val builder = NotificationCompat.Builder(this, "n")
//            .setContentText("App Started")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setAutoCancel(true)
//            .setContentText("App is Started")
//            .setContentIntent(resultPendingIntent)
//        val managerCompat = NotificationManagerCompat.from(this)
//        managerCompat.notify(999, builder.build())
//    }
//
//    private fun addArtist() {
//            val name = editTextName!!.text.toString().trim { it <= ' ' }
//            val genre = spinnerGenres!!.selectedItem.toString()
//            if (!TextUtils.isEmpty(name)) {
//                val id: String = databaseArtists?.push().getKey()!!
//                val artist = Artist(id, name, genre)
//                databaseArtists.child(id).setValue(artist)
//                Toast.makeText(this, "Artist Added", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "You should enter a name", Toast.LENGTH_LONG).show()
//            }
//        }

}

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = (NotificationManager)Android.App.Application.Context.GetSystemService(channel);
            manager.createNotificationChannel(channel)
        }
        val resultIntent = Intent(
            this,
            MainActivity::class.java
        )
        val resultPendingIntent =
            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "n")
            .setContentText("App Started")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setContentText("App is Started")
            .setContentIntent(resultPendingIntent)
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(999, builder.build())
    }
}
