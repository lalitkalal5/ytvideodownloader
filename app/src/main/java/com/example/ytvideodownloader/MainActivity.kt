package com.example.ytvideodownloader

import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ytvideodownloader.ui.theme.YtvideodownloaderTheme
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YtvideodownloaderTheme {
                Greeting()
            }
        }
    }

    @Composable
    fun Greeting() {
        var textState by remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth().fillMaxHeight()
                .background(Color.Red),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Hello folks",
                    modifier = Modifier
                        .padding(10.dp),
                    Color.Black,
//                fontSize = 25.dp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
            contentAlignment = Alignment.Center )
            {
            Text(
                text = "Please enter your YouTube video link",
                modifier = Modifier.padding(10.dp),
                Color.Black,
                fontWeight = FontWeight.Bold
            )}
            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .padding(10.dp).fillMaxWidth().fillMaxHeight(.1f)
                    .border(
                        border = BorderStroke(2.dp, color = Color.Red),
                        shape = RoundedCornerShape(10.dp)
                    ),
                decorationBox = { innerTextField ->
                    if (textState.text.isEmpty()) {
                        Text(
                            text = "Link",
//                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(10.dp)
                                .background(Color.DarkGray),
                            Color.White,

                            )
                    }
                    innerTextField()
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { Downloadsfun(textState.text) }) {
                    Text(
                        text = "Download",
                        modifier = Modifier
                            .background(Color.DarkGray),
                        Color.White
                    )
                }
            }
        }
    }
    private fun Downloadsfun(url: String) {
        val link = Videorequest(url = url)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://90f7-152-58-35-65.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(apiinterface::class.java)
        service.downloadVideo(link).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        saveVideoToFile(body)
                        Log.d("MainActivity", "Video downloaded successfully")
                        Toast.makeText(
                            this@MainActivity,
                            "Download completed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("MainActivity", "Error downloading video", t)
            }
        })
    }

    private fun saveVideoToFile(body: ResponseBody) {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), "video.mp4")

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            Log.d("MainActivity", "Video saved to: ${file.absolutePath}")
            Toast.makeText(this,"${file.absolutePath}",Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }}



//@Preview(showBackground = true)
//    @Composable
//    fun GreetingPreview() {
//        YtvideodownloaderTheme {
//            Greeting()
//        }
//    }
////}
