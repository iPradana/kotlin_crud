package com.team67x.kotlin_crud

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    var id : String = ""
    var name : String = ""
    var hasil : String = ""
    var jsondata : String = ""
    var data : String = ""
    var jObject = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = etID.text.toString()
        name = etName.text.toString()

        try {
            etID.setText(intent.getStringExtra("ID"))
            etName.setText(intent.getStringExtra("Name"))
            Toast.makeText(getApplicationContext(),""+intent.getStringExtra("Name"),Toast.LENGTH_SHORT).show()
        }catch (ex : Exception){
            Toast.makeText(getApplicationContext(),""+ex,Toast.LENGTH_SHORT).show()
        }
    }

    public fun refreshdata(){
        try{
            HttpAsyncTaskPosts().execute("http://team67app.000webhostapp.com/tampil.php")
        }catch (ex :Exception ){
            Toast.makeText(getApplicationContext(),"Failed Parsing Data",Toast.LENGTH_SHORT).show()
        }
    }

    public fun btnSimpan (v : View)
    {
        id =  etID.text.toString()
        name = etName.text.toString()
        HttpAsyncTaskPost().execute("http://team67app.000webhostapp.com/tambah.php?id=$id&name=$name")
        refreshdata()
    }

    fun showMessage(title: String, Message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }

    fun POST(url: String, id: String, name : String): String {
        var inputStream: InputStream? = null
        var result = ""
        try {
            val httpclient = DefaultHttpClient()
            val httpGet = HttpGet(url)
            var json = ""
            val jsonObject = JSONObject()
            jsonObject.accumulate("username", id)
            jsonObject.accumulate("name", name)
            json = jsonObject.toString()
            val se = StringEntity(json)
            httpGet.setHeader("Accept", "application/json")
            httpGet.setHeader("Content-type", "application/json")
            val httpResponse = httpclient.execute(httpGet)
            inputStream = httpResponse.getEntity().getContent()
            if (inputStream != null){
                result = convertInputStreamToString(inputStream)
            }else{
                result = "Did not work!"
            }
            hasil = result
        } catch (e: Exception) {
            Log.d("InputStream", e.localizedMessage)
        }
        return result
    }

    fun GET(url: String, id: String, name : String): String {
        var inputStream: InputStream? = null
        var result = ""
        try {
            jsondata = ""
            val httpclient = DefaultHttpClient()
            val httpGet = HttpGet(url)
            var json = ""
            val jsonObject = JSONObject()
            jsonObject.accumulate("username", id)
            jsonObject.accumulate("name", name)
            json = jsonObject.toString()
            val se = StringEntity(json)
            httpGet.setHeader("Accept", "application/json")
            httpGet.setHeader("Content-type", "application/json")
            val httpResponse = httpclient.execute(httpGet)
            inputStream = httpResponse.getEntity().getContent()
            if (inputStream != null){
                result = convertInputStreamToString(inputStream)
            } else{
                result = "Did not work!"
            }
            jsondata += result
        } catch (e: Exception) {
            Log.d("InputStream", e.localizedMessage)
        }
        return jsondata
    }

    @Throws(IOException::class)
    private fun convertInputStreamToString(inputStream: InputStream): String {
        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()
            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            val result = response.toString()
            return result
        }
    }

    private fun parseBoundary() {
        var xResultBoundary : String =""
        jObject = JSONObject(jsondata)
        val menuitemArray = jObject.getJSONArray("user")
    }

    private inner class HttpAsyncTaskPost : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return POST(urls[0], id,name)
        }
        override fun onPostExecute(result: String) {
          // tambahkan toast setelah berhasil di eksekusi
        }
    }

    private inner class HttpAsyncTaskPosts : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return GET(urls[0], id,name)
        }
        override fun onPostExecute(result: String) {
            //tambahkan toast setelah berhasil di eksekusi
        }
    }
}
