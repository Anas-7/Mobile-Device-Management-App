package com.example.mohdma.mobiledevicemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class WriteEmail : AppCompatActivity() {
    data class Email(
            val sender: String? = null,
            val verified: String? = null,
            val sender_email: String? = null,
            val receiver_email: String? = null,
            val message: String? = null,
            val resolved: String? = null
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_email)
        val email_intent = getIntent()
        val employee_id = email_intent.getStringExtra("employee_id").toString()
        val employee_pwd = email_intent.getStringExtra("employee_pwd").toString()

        val submit_button = findViewById<Button>(R.id.submit)
        var count: Int = 0
        submit_button.setOnClickListener(){
            val email_id = findViewById<EditText>(R.id.receiver_id).text.toString()
            val msg_content = findViewById<EditText>(R.id.email_content).text.toString()
            if(email_id.trim().equals("") || email_id == null){
                Toast.makeText(applicationContext,"Please enter the recipient id", Toast.LENGTH_SHORT).show()
            }else if(msg_content.trim().equals("") || msg_content == null){
                Toast.makeText(applicationContext,"Please enter content of message", Toast.LENGTH_SHORT).show()
            }else{
                //First check whether it is a valid email
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

                //Check whether the email is in the whitelist or not
                db.collection("whitelist_db")
                        .whereArrayContains("valid_emails",email_id)
                        .get()
                        .addOnSuccessListener{document ->
                            try {
                                if(document.size() > 0) {
                                    db.collection("employee_db")
                                            .whereEqualTo("employee_id",employee_id)
                                            .get()
                                            .addOnSuccessListener { document->
                                                val valid_email = Email(employee_id, "YES",document.first().getString("employee_email").toString(), email_id, msg_content,"NA")
                                                db.collection("email_db").document().set(valid_email)
                                                Toast.makeText(applicationContext,"Email has been sent!",Toast.LENGTH_LONG).show()
                                                findViewById<EditText>(R.id.receiver_id).setText("")
                                                findViewById<EditText>(R.id.email_content).setText("")
                                            }
                                }else{
                                    //Give a warning
                                    if(count == 0){
                                        Toast.makeText(applicationContext,"This email is not whitelisted. Please check",Toast.LENGTH_LONG).show()
                                        count = count + 1
                                    }else{
                                        Toast.makeText(applicationContext,"Unverified email hence you have been reported",Toast.LENGTH_LONG).show()
                                        db.collection("employee_db")
                                                .whereEqualTo("employee_id",employee_id)
                                                .get()
                                                .addOnSuccessListener { document->
                                                    val invalid_email = Email(employee_id, "NO",document.first().getString("employee_email").toString(), email_id, msg_content,"NO")
                                                    db.collection("email_db").document()
                                                            .set(invalid_email)
                                                            .addOnSuccessListener { Log.e("TAG","DocumentSnapshot successfully written!") }
                                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                                }

                                    }
                                }
                            }catch (e: Exception){
                                Log.d("ERROR", "Error in Email")
                            }
                        }
            }
        }
    }
}
