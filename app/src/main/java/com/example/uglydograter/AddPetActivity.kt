package com.example.uglydograter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.uglydograter.api.RetrofitClient
import com.example.uglydograter.models.AddPetRequest
import com.example.uglydograter.models.AddPetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddPetActivity : AppCompatActivity() {

    private lateinit var etPetName: EditText
    private lateinit var etPetDescription: EditText
    private lateinit var etPetBirthday: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var btnAddPet: Button
    private lateinit var imageViewPet: ImageView
    private var imageUri: Uri? = null

    // I used AI to help me with this as I was completely lost
    fun encodeImageToBase64(context: Context, imageUri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int

        // Read the image file into a byte array
        while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        // Convert byte array to Base64 string
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private val imagePickerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data
                imageViewPet.setImageURI(imageUri)
            }
        }

    // I used AI to help me with this as I was completely lost
    private fun getFileFromContentUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver

            // Get MIME type and extension dynamically
            val mimeType = contentResolver.getType(uri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "tmp"

            // Create a temporary file with the correct extension
            val tempFile = File.createTempFile("temp_image", ".$extension", context.cacheDir)

            // Copy content to the temporary file
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        etPetName = findViewById(R.id.etPetName)
        etPetDescription = findViewById(R.id.etPetDescription)
        etPetBirthday = findViewById(R.id.etPetBirthday)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnAddPet = findViewById(R.id.btnAddPet)
        imageViewPet = findViewById(R.id.imageViewPet)

        btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        btnAddPet.setOnClickListener {
            addPet()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }


    private fun addPet() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        if (token == null) {
            Toast.makeText(this, "Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }


        val name = etPetName.text.toString().trim()
        val description = etPetDescription.text.toString().trim()
        val birthday = etPetBirthday.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || imageUri == null || birthday.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }
        val imageBase64 = encodeImageToBase64(this, imageUri!!)

        val request = AddPetRequest(
            token = token,
            image = imageBase64,
            name = name,
            description = description,
            birthday = birthday
        )




        RetrofitClient.apiService.addPet(request)
            .enqueue(object : Callback<AddPetResponse> {
                override fun onResponse(
                    call: Call<AddPetResponse>,
                    response: Response<AddPetResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == 1) {
                        Toast.makeText(
                            this@AddPetActivity,
                            "Pet added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        val errorMessage = response.body().toString() ?: "Unknown error"
                        val statusCode = response.code()
                        Toast.makeText(
                            this@AddPetActivity,
                            "Failed to add pet: $errorMessage (Status Code: $statusCode)",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AddPetResponse>, t: Throwable) {
                    Toast.makeText(this@AddPetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

}


