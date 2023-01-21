package com.example.mohdma.mobiledevicemanagement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class ViewDocs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_docs)

        val doc_intent = getIntent()
        val employee_id = doc_intent.getStringExtra("employee_id").toString()
        val employee_pwd = doc_intent.getStringExtra("employee_pwd").toString()

        //Get emmployee docs
        val db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        val doc_names = arrayListOf<kotlin.String>()
        val doc_descs = arrayListOf<kotlin.String>()
        val listview = findViewById<ListView>(R.id.listView)
        db.collection("docs_db")
                .whereEqualTo("doc_writer_id", employee_id)
                .get()
                .addOnSuccessListener { documents ->
                    if(documents.size() > 0) {
                        for (document in documents) {
                            doc_names.add(document.getString("doc_name").toString())
                            doc_descs.add(document.getString("doc_content").toString())
                        }
                        listview.adapter = CustomListAdapter(this,doc_names,doc_descs)
                    }else{
                        Toast.makeText(applicationContext, "No documents have been created yet or internet error", Toast.LENGTH_LONG).show()
                    }
                }
    }
    private class CustomListAdapter(context: Context, doc_names: ArrayList<kotlin.String>, doc_descs: ArrayList<kotlin.String> ): BaseAdapter(){
        private val mContext: Context
        private var doc_names = arrayListOf<kotlin.String>()
        private var doc_descs = arrayListOf<kotlin.String>()

        init{
            mContext = context
            this.doc_names = doc_names
            this.doc_descs = doc_descs
        }
        override fun getCount(): Int {
            return doc_names.size
        }

        override fun getItem(position: Int): Any {
            return position.toLong()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row_val = layoutInflater.inflate(R.layout.row_main_docs,parent,false)

            val doc_name = row_val.findViewById<TextView>(R.id.doc_name).setText(doc_names.get(position))
            val doc_desc = row_val.findViewById<TextView>(R.id.doc_content).setText(doc_descs.get(position))
            return row_val
        }
    }
}
