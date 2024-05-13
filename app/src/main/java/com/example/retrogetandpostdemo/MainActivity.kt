package com.example.retrogetandpostdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val textViewResponse = findViewById<TextView>(R.id.textResponse)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val jsonObject = JSONObject().apply {
                put("acquirerId", "ADIBOMA0001")
                put("merchantId", JSONObject.NULL)
                put("merchantName", "madhina")
                put("merchantAddress", "Al madina Al soor")
                put("merchantAddr2", "Rolla")
                put("businessDays", JSONObject().apply {
                    put("mon", true)
                    put("tue", true)
                    put("wed", true)
                    put("thu", true)
                    put("fri", true)
                    put("sat", true)
                    put("sun", false)
                })
            }
            val jsonBody = JSONObject().apply {
                put("userName", username)
                put("password", password)
                put("deviceType", "WEB")
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

            try {
                // Print request body to log
                Log.d("MainActivity", "Request Body: $jsonBody")

                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("http://213.42.225.250:9508/NanoPay/v1/login")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        // Handle failure
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()

                        runOnUiThread {
                            // Check the status code
                            val statusCode = response.code
                            Log.d("MainActivity", "Status Code: $statusCode")

                            when (statusCode) {
                                200 -> {
                                    // Handle status code 200 (OK)
                                    if (responseBody != null) {
                                        // Update UI with response
                                        textViewResponse.text = responseBody

                                        // Parse response body into a JSON object
                                        val jsonResponse = JSONObject(responseBody)

                                        // Extract specific value using its key
                                        val specificValue =
                                            jsonResponse.optString("passFlag", "default_value")
                                        Log.d("MainActivity", "Response Body: $responseBody")
                                        // Print the specific value to log
                                        Log.d("MainActivity", "Specific Value: $specificValue")
                                    }
                                }

                                400 -> {
                                    // Handle status code 400 (Bad Request)
                                    Log.e("MainActivity", "Bad Request: $responseBody")
                                    // Add any specific error handling logic for 400 status code
                                }

                                else -> {
                                    // Handle other status codes
                                    Log.e("MainActivity", "Unexpected status code: $statusCode")
                                    // Add any specific error handling logic for other status codes
                                }
                            }
                        }
                    }


                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

//class MainActivity : AppCompatActivity() {
//    private lateinit var text_view:TextView
//    private lateinit var retService: AlbumService
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        text_view=findViewById(R.id.text_view)
//        retService = RetrofitInstance
//            .getRetrofitInstance()
//            .create(AlbumService::class.java)
//        getRequestWithQueryParameters()
//        //getRequestWithPathParameters()
//       // uploadAlbum()
//
//    }
//
//
//    private fun getRequestWithQueryParameters() {
//        val responseLiveData: LiveData<Response<Albums>> = liveData {
//            val response = retService.getSortedAlbums(3)
//            emit(response)
//        }
//        responseLiveData.observe(this, Observer {
//            val albumsList = it.body()?.listIterator()
//            if (albumsList != null) {
//                while (albumsList.hasNext()) {
//                    val albumsItem = albumsList.next()
//                    val result = " " + "Album Title : ${albumsItem.title}" + "\n" +
//                            " " + "Album id : ${albumsItem.id}" + "\n" +
//                            " " + "User id : ${albumsItem.userId}" + "\n\n\n"
//                    text_view.append(result)
//                }
//            }
//        })
//    }
//
//
//    private fun getRequestWithPathParameters() {
//        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
//            val response = retService.getAlbum(3)
//            emit(response)
//
//        }
//
//        pathResponse.observe(this, Observer {
//            val title = it.body()?.title
//            Toast.makeText(applicationContext, title, Toast.LENGTH_LONG).show()
//        })
//    }
//   // POST a AlbumsItem on success resp a AlbumsItem with id 101
//    private fun uploadAlbum() {
//        val album = AlbumsItem(0, "My title", 3)
//        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
//            val response = retService.uploadAlbum(album)
//            emit(response)
//        }
//        postResponse.observe(this, Observer {
//            val receivedAlbumsItem = it.body()
//            val result = " " + "Album Title : ${receivedAlbumsItem?.title}" + "\n" +
//                    " " + "Album id : ${receivedAlbumsItem?.id}" + "\n" +
//                    " " + "User id : ${receivedAlbumsItem?.userId}" + "\n\n\n"
//            text_view.text = result
//        })
//
//    }
//
//}
