package com.example.mohdma.mobiledevicemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase obj
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener(){
            val employee_id = findViewById<EditText>(R.id.employee_id).text.toString()
            val employee_pwd = findViewById<EditText>(R.id.employee_pwd).text.toString()
            db.collection("employee_db")
                    .whereEqualTo("employee_id",employee_id)
                    .whereEqualTo("employee_pwd",employee_pwd)
                    .get()
                    .addOnSuccessListener { document ->
                        try {
                            if(document.size() > 0){
                                if(document.first().getString("authorized").toString().equals("NO")){
                                    val intent = Intent(applicationContext, NoLongerAuthorized::class.java)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(applicationContext,"Login Successful!",Toast.LENGTH_SHORT).show()
                                    val intent = Intent(applicationContext, HomePage::class.java)
                                    intent.putExtra("employee_id", employee_id)
                                    intent.putExtra("employee_pwd", employee_pwd)
                                    startActivity(intent)
                                }
                            }else{
                                Log.d("TAG", "Error getting documents:  something here")
                                Toast.makeText(applicationContext,"Login Failed. Check credentials",Toast.LENGTH_LONG).show()
                            }
                        }catch (e: Exception){
                            Log.d("TAG", "Error getting documents:  something here")
                            Toast.makeText(applicationContext,"Login Failed. Check credentials",Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }
}