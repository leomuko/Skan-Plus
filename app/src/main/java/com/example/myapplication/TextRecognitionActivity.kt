package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText


class TextRecognitionActivity : AppCompatActivity() {

    private lateinit var captureImageButton: Button
    private lateinit var detectTextButton: Button
    private lateinit var detectTv: TextView
    private lateinit var captureImageView: ImageView
    val REQUEST_IMAGE_CAPTURE = 1
    private val TAG = "TextRecognitionActivity"
    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        captureImageButton = findViewById(R.id.take_imageButton)
        detectTextButton = findViewById(R.id.display_text_button)
        detectTv = findViewById(R.id.text_display)
        captureImageView = findViewById(R.id.image_view)


        captureImageButton.setOnClickListener {
            //for capturing the image
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Log.d(TAG, "Error: " + e.localizedMessage)
            }
        }

        detectTextButton.setOnClickListener {
            detectTextFromImage();
        }
    }

    private fun detectTextFromImage() {
        val image = FirebaseVisionImage.fromBitmap(imageBitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image).addOnSuccessListener {
            displayDetectedText(it)

        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Error :" + it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDetectedText(result: FirebaseVisionText?) {
        val resultText = result!!.text
        if (result.textBlocks.size == 0) {
            Toast.makeText(this, "No text was detected", Toast.LENGTH_SHORT).show()
        } else {
            for (block in result.textBlocks) {
                val blockText = block.text
                detectTv.text = blockText
                /*val blockConfidence = block.confidence
                val blockLanguages = block.recognizedLanguages
                val blockCornerPoints = block.cornerPoints
                val blockFrame = block.boundingBox
                for (line in block.lines){
                    val lineText = line.text
                    val lineConfidence = line.confidence
                    val lineLanguages = line.recognizedLanguages
                    val lineCornerPoints = line.cornerPoints
                    val  lineFrame = line.boundingBox
                    for (element in line.elements){
                        val elementText = element.text
                        val elementConfidence = element.confidence
                        val elementLanguages = element.recognizedLanguages
                        val elementCornerPoints = element.cornerPoints
                        val elementFrame = element.boundingBox
                    }
                }*/
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            captureImageView.setImageBitmap(imageBitmap)
        }
    }
}