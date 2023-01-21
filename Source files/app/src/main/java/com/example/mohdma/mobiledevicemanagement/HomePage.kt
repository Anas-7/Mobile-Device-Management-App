package com.example.mohdma.mobiledevicemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val email = findViewById<Button>(R.id.email)
        email.setOnClickListener(){
            val main_intent = getIntent()
            val intent = Intent(applicationContext, EmailPage::class.java)
            intent.putExtra("employee_id", main_intent.getStringExtra("employee_id"))
            intent.putExtra("employee_pwd", main_intent.getStringExtra("employee_pwd"))
            startActivity(intent)
        }

        val browser = findViewById<Button>(R.id.browser)
        browser.setOnClickListener(){
            val main_intent = getIntent()
            val intent = Intent(this, VisitBrowser::class.java)
            Log.d("Tag","Emp id is " + main_intent.getStringExtra("employee_id"))
            intent.putExtra("employee_id", main_intent.getStringExtra("employee_id"))
            intent.putExtra("employee_pwd", main_intent.getStringExtra("employee_pwd"))
            startActivity(intent)
        }

        val docs = findViewById<Button>(R.id.docs)
        docs.setOnClickListener(){
            val main_intent = getIntent()
            val intent = Intent(applicationContext, DocsPage::class.java)
            intent.putExtra("employee_id", main_intent.getStringExtra("employee_id"))
            intent.putExtra("employee_pwd", main_intent.getStringExtra("employee_pwd"))
            startActivity(intent)
        }

    }
}
