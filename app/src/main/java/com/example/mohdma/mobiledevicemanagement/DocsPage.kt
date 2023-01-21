package com.example.mohdma.mobiledevicemanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DocsPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_page)

        val write_doc = findViewById<Button>(R.id.write_doc)
        write_doc.setOnClickListener(){
            val doc_intent = getIntent()
            val intent = Intent(applicationContext,WriteDocs::class.java)
            intent.putExtra("employee_id", doc_intent.getStringExtra("employee_id").toString())
            intent.putExtra("employee_pwd", doc_intent.getStringExtra("employee_pwd").toString())
            startActivity(intent)
        }

        val view_docs = findViewById<Button>(R.id.view_docs)
        view_docs.setOnClickListener(){
            val doc_intent = getIntent()
            val intent = Intent(applicationContext,ViewDocs::class.java)
            intent.putExtra("employee_id", doc_intent.getStringExtra("employee_id").toString())
            intent.putExtra("employee_pwd", doc_intent.getStringExtra("employee_pwd").toString())
            startActivity(intent)
        }
    }
}
