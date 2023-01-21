package com.example.mohdma.mobiledevicemanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EmailPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_page)

        val write_email = findViewById<Button>(R.id.write_email)
        write_email.setOnClickListener(){
            val email_intent = getIntent()
            val intent = Intent(applicationContext,WriteEmail::class.java)
            intent.putExtra("employee_id", email_intent.getStringExtra("employee_id").toString())
            intent.putExtra("employee_pwd", email_intent.getStringExtra("employee_pwd").toString())
            startActivity(intent)
        }

        val view_emails = findViewById<Button>(R.id.view_emails)
        view_emails.setOnClickListener(){
            val view_emails = getIntent()
            val intent = Intent(applicationContext,ViewEmails::class.java)
            intent.putExtra("employee_id", view_emails.getStringExtra("employee_id").toString())
            intent.putExtra("employee_pwd", view_emails.getStringExtra("employee_pwd").toString())
            startActivity(intent)
        }
    }
}
