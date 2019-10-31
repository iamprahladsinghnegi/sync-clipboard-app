package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "KotlinActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        basicReadWrite()
    }



    fun basicReadWrite() {
        // [START write_message]
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users/vikramk9852/data")

        // [END write_message]

        // [START read_message]
        // Read from the database
        var myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
//                val value = dataSnapshot.value as Map<String, Any>
                var myClip: ClipData = ClipData.newPlainText("note_copy", value)
                myClipboard.setPrimaryClip(myClip)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        // [END read_message]
        myClipboard.addPrimaryClipChangedListener {
            val primaryClipData= myClipboard.primaryClip

            if (primaryClipData == null || primaryClipData.itemCount > 0 && primaryClipData.getItemAt(0).text == null)
                return@addPrimaryClipChangedListener  // ... whatever just don't go to next line

            val clip = primaryClipData.getItemAt(0).text.toString()
            myRef.setValue(clip)
            Log.w(TAG, "clipboard changed to $clip")
        }
    }
}
