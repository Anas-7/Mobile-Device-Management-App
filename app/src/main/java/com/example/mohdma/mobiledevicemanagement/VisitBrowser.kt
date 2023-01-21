package com.example.mohdma.mobiledevicemanagement

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import io.perfmark.Tag
import java.net.URL


class VisitBrowser : AppCompatActivity() {
    data class Whitelist(
            val valid_urls: List<String>? = null,
            val valid_emails: List<String>? = null
    )

    data class Browser(
            val employee_id: String? = null,
            val url_visited: String? = null,
            val resolved: String? = null
    )

    /* Returns true if url is valid */
    fun isValid(url: String?): Boolean {
        /* Try creating a valid URL */
        return try {
            URL(url).toURI()
            true
        } // If there was an Exception
        // while creating URL object
        catch (e: Exception) {
            false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_browser)

        val home_intent = getIntent()
        val employee_id = home_intent.getStringExtra("employee_id").toString()
        val employee_pwd = home_intent.getStringExtra("employee_pwd").toString()

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener(){

            val temp_url = findViewById<EditText>(R.id.browser_url).text.toString()
            val url_entered: String?
            if(!temp_url.contains("https://"))
                url_entered = "https://" + temp_url
            else
                url_entered = temp_url
            if(isValid(url_entered)){
                val db = FirebaseFirestore.getInstance()
                db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

                //Check whether the url is in the whitelist or not
                db.collection("whitelist_db")
                        .whereArrayContains("valid_urls",url_entered)
                        .get()
                        .addOnSuccessListener{document ->
                            try {
                                if(document.size() > 0) {
                                    val webView = findViewById<WebView>(R.id.webView)
                                    //Enabling javascript.
                                    webView.getSettings().setJavaScriptEnabled(true)
                                    //Making the page open in the app itself.
                                    webView.setWebViewClient(WebViewClient())
                                    //If we want to load a specific URL.
                                    webView.loadUrl(url_entered)
                                }else{
                                    //Insert the error in the Browser database
                                    Toast.makeText(applicationContext,"Accessing this url is not allowed.",Toast.LENGTH_LONG).show()
                                    val sus_url = Browser(employee_id,url_entered,"NO")
                                    db.collection("browser_db").document()
                                            .set(sus_url)
                                            .addOnSuccessListener { Log.e("TAG","DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                }
                            }catch (e: Exception){
                                Log.d("ERROR", "Error in webview")
                            }
                        }
        }else{
            Toast.makeText(applicationContext,"Please enter a valid url",Toast.LENGTH_LONG).show()
        }


        }

    }
}
