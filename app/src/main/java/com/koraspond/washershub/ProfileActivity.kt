package com.koraspond.washershub

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.koraspond.washershub.Models.updateProfilePic.UpdateProfilePic
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel
import com.koraspond.washershub.Utils.StaticClass.Companion.IMAGEURL
import com.koraspond.washershub.Utils.UserInfoPreference
import com.koraspond.washershub.databinding.ActivityProfileBinding
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    var selectedImageUrl: String = ""

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access media.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        // Initialize user info
        binding.cEt.setText( UserInfoPreference(this).getStr("contact"))
        binding.eEt.text = UserInfoPreference(this).getStr("email")
        Glide.with(this)
            .load(IMAGEURL + UserInfoPreference(this).getStr("image"))
            .placeholder(R.drawable.baseline_person_outline_24)
            .into(binding.profileImage)

        // Upload profile picture
        binding.uploadProfile.setOnClickListener {
            requestMediaPermission()
        }

        // Edit contact number
        binding.editIcon.setOnClickListener {
            binding.cEt.isEnabled = true // Enable the EditText
            binding.cEt.isFocusableInTouchMode = true // Ensure it can be focused in touch mode
            binding.cEt.isClickable = true // Ensure it is clickable
            binding.cEt.isFocusable = true // Ensure it is focusable
            binding.cEt.requestFocus() // Request focus to show the cursor

            // Show the soft keyboard
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.cEt, InputMethodManager.SHOW_IMPLICIT)
        }
        // Save updated contact number
        binding.saveButton.setOnClickListener {
            val updatedContact = binding.cEt.text.toString()
            if (updatedContact.isNotEmpty()) {
                updateContactNumber(updatedContact)
            } else {
                showMessage("Please enter a valid contact number")
            }
        }
    }

    private fun updateContactNumber(contact: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Updating contact...")
        progressDialog.show()

        val token = "token " + UserInfoPreference(this).getStr("token")


        Api.client.updateContact(token, contact).enqueue(object : Callback<UpdateProfilePic> {
            override fun onResponse(call: Call<UpdateProfilePic>, response: Response<UpdateProfilePic>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    UserInfoPreference(this@ProfileActivity).setStr("contact", contact)
                    showMessage("Contact updated successfully")

                    binding.cEt.isEnabled = false
                } else {
                    showMessage("Failed to update contact")
                }
            }

            override fun onFailure(call: Call<UpdateProfilePic>, t: Throwable) {
                progressDialog.dismiss()
                showMessage(t.message ?: "Failed to update contact")
            }
        })
    }

    private fun requestMediaPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this@ProfileActivity,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }

            else -> {
                val permission4 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else
                    Manifest.permission.READ_EXTERNAL_STORAGE

                requestPermissionLauncher.launch(permission4)
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        getContent.launch(galleryIntent)
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImageUri: Uri? = data.data
                    selectedImageUrl = selectedImageUri.toString()

                    val cursor =
                        selectedImageUri?.let { contentResolver.query(it, null, null, null, null) }
                    cursor?.use {
                        val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                        if (it.moveToFirst()) {
                            val imageFile: File? = getFileFromUri(selectedImageUri)
                            if (imageFile != null) {
                                if (imageFile.length() <= 2 * 1024 * 1024) {
                                    uploadProfilePicture(imageFile)
                                } else {
                                    showMessage("Image size exceeds 2 MB. Please choose a smaller image.")
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun uploadProfilePicture(imageFile: File) {
        val loadingDialog = ProgressDialog(this)
        loadingDialog.setMessage("Uploading profile picture...")
        loadingDialog.show()

        val requestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            imageFile
        )
        val selectFile = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestBody
        )
        val token = "token " + UserInfoPreference(this).getStr("token")
        val nameBodyt = RequestBody.create("text/plain".toMediaTypeOrNull(), imageFile.name)

        Api.client.updateProfilePic(
            token,
            selectFile,
            nameBodyt
        ).enqueue(object : Callback<UpdateProfilePic> {
            override fun onResponse(
                call: Call<UpdateProfilePic>,
                response: Response<UpdateProfilePic>
            ) {
                loadingDialog.dismiss()
                if (response.isSuccessful) {
                    val updatedImageUrl = response.body()?.data?.image?.image ?: ""
                    updateImagePath(selectedImageUrl, updatedImageUrl)
                } else {
                    showMessage("Unable to update image. Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdateProfilePic>, t: Throwable) {
                loadingDialog.dismiss()
                showMessage(t.message ?: "Failed to update profile picture")
            }
        })
    }

    private fun updateImagePath(selectedImageUrl: String, image: String) {
        val progressDialog = ProgressDialog(this@ProfileActivity)
        progressDialog.setMessage("Updating profile image...")
        progressDialog.show()

        Api.client.updateProfilePicTwo(
            "token " + UserInfoPreference(this@ProfileActivity).getStr("token").toString(),
            image
        ).enqueue(object : Callback<UpdateProfilePic> {
            override fun onResponse(
                call: Call<UpdateProfilePic>,
                response: Response<UpdateProfilePic>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful && response.body() != null) {
                    Glide.with(this@ProfileActivity)
                        .load(selectedImageUrl)
                        .into(binding.profileImage)

                    UserInfoPreference(this@ProfileActivity).setStr("image", image)
                    showMessage("Profile image updated successfully")
                }
            }

            override fun onFailure(call: Call<UpdateProfilePic>, t: Throwable) {
                progressDialog.dismiss()
                showMessage(t.message ?: "Failed to update profile image")
            }
        })
    }

    private fun getFileFromUri(uri: Uri?): File? {
        uri ?: return null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver?.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(filePathColumn[0])
            val imagePath = it.getString(columnIndex)
            return File(imagePath)
        }
        return null
    }
}
