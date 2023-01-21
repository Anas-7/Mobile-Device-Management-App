package com.example.mohdma.mobiledevicemanagement

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.util.*
import kotlin.collections.ArrayList

class ViewEmails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_emails)

        val email_intent = getIntent()
        val employee_id = email_intent.getStringExtra("employee_id").toString()
        val employee_pwd = email_intent.getStringExtra("employee_pwd").toString()

        //Get emmployee docs
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        val email_types = arrayListOf<kotlin.String>()
        val receiver_emails = arrayListOf<kotlin.String>()
        val email_contents = arrayListOf<kotlin.String>()
        val listview = findViewById<ListView>(R.id.listView)
        db.collection("employee_db")
                .whereEqualTo("employee_id",employee_id)
                .get()
                .addOnSuccessListener { document->
                    if(document.size() > 0){
                        val sender_email = document.first().getString("employee_email").toString()
                        //Instances where receiver is the employee and sender someone else
                        db.collection("email_db")
                                .whereEqualTo("receiver_email",sender_email)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for(document in documents){
                                        email_types.add("Received")
                                        receiver_emails.add("From: " + document.getString("sender_email").toString())
                                        email_contents.add(document.getString("message").toString())
                                    }
                                    //Instances where receiver is someone else and sender is employee
                                    db.collection("email_db")
                                            .whereEqualTo("sender_email",sender_email)
                                            .get()
                                            .addOnSuccessListener { documents->
                                                for(document in documents){
                                                    email_types.add("Sent")
                                                    receiver_emails.add("To: " + document.getString("receiver_email").toString())
                                                    email_contents.add(document.getString("message").toString())
                                                }
                                                Log.d("List Item 0", receiver_emails.get(0))
                                                Collections.shuffle(receiver_emails,Random(3))
                                                Collections.shuffle(email_contents,Random(3))
                                                Collections.shuffle(email_types,Random(3))
                                                listview.adapter = CustomListAdapter(this,receiver_emails,email_contents,email_types)
                                            }
                                }
                    }
                }

    }
    private class CustomListAdapter(context: Context,
                                    receiver_emails: ArrayList<kotlin.String>,
                                    email_contents: ArrayList<kotlin.String>,
                                    email_types: ArrayList<kotlin.String> ): BaseAdapter(){
        private val mContext: Context
        private var email_types = arrayListOf<kotlin.String>()
        private var receiver_emails = arrayListOf<kotlin.String>()
        private var email_contents = arrayListOf<kotlin.String>()

        init{
            mContext = context
            this.email_types = email_types
            this.receiver_emails = receiver_emails
            this.email_contents = email_contents
        }
        override fun getCount(): Int {
            return receiver_emails.size
        }

        override fun getItem(position: Int): Any {
            return position.toLong()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row_val = layoutInflater.inflate(R.layout.row_main_email,parent,false)

            row_val.findViewById<TextView>(R.id.email_type).setText(email_types.get(position))
            row_val.findViewById<TextView>(R.id.email_id).setText(receiver_emails.get(position))
            row_val.findViewById<TextView>(R.id.email_content).setText(email_contents.get(position))
            return row_val
        }
    }
}
