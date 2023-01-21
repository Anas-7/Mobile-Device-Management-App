package com.example.mohdma.mobiledevicemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class WriteDocs : AppCompatActivity() {
    data class Doc(
        val doc_writer_id: String? = null,
        val doc_name: String? = null,
        val doc_content: String? = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_docs)

        val doc_intent = getIntent()
        val employee_id = doc_intent.getStringExtra("employee_id").toString()
        val employee_pwd = doc_intent.getStringExtra("employee_pwd").toString()

        val submit_button = findViewById<Button>(R.id.submit)
        submit_button.setOnClickListener(){
            val doc_name = findViewById<EditText>(R.id.doc_name).text.toString()
            val doc_content = findViewById<EditText>(R.id.doc_content).text.toString()
            if(doc_name.trim().equals("") || doc_name == null){
                Toast.makeText(applicationContext,"Please enter the name of document", Toast.LENGTH_SHORT).show()
            }else if(doc_content.trim().equals("") || doc_content == null){
                Toast.makeText(applicationContext,"Please enter content of document", Toast.LENGTH_SHORT).show()
            }else{
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                val doc_val = Doc(employee_id,doc_name,doc_content)
                db.collection("docs_db").document()
                        .set(doc_val)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"Document created successfully!",Toast.LENGTH_SHORT).show()
                            findViewById<EditText>(R.id.doc_name).setText("")
                            findViewById<EditText>(R.id.doc_content).setText("")
                        }
                        .addOnFailureListener{
                            Toast.makeText(applicationContext,"Document couldn't be saved. Check your connection.",Toast.LENGTH_LONG).show()
                        }
            }
        }

    }
}
