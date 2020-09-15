package com.sample.firebaseexample.fragments


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sample.firebaseexample.ui.PICK_IMAGE_REQUEST
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.models.Notes
import kotlinx.android.synthetic.main.upload_fragment.*
import kotlinx.android.synthetic.main.upload_fragment.view.*
import java.io.ByteArrayOutputStream
import java.util.*


const val CAMERA_REQ_CODE = 1003
const val GALLERY_REQ_CODE = 1004
class UploadFragment:Fragment(), View.OnClickListener {

    private lateinit var rootView:View

    private val db = FirebaseFirestore.getInstance()
    private lateinit var imageUri: Uri

    private val noteRef: DocumentReference = db.collection("NoteBook").document("${FirebaseAuth.getInstance().currentUser?.uid}")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.upload_fragment,container,false)
        rootView.btn_load.setOnClickListener(this)
        rootView.btn_save.setOnClickListener(this)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressed()
    }

    override fun onClick(view: View?) {
        if (view == btn_save){
            saveNote()
        } else {
            openDialog()
        }
    }

    private fun openDialog() {
        val items = arrayOf("camera","gallery","cancel")
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Select image")
        alertDialog.setItems(items
        ) { dialogInterface, i ->
            when {
                items[i] == "camera" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,CAMERA_REQ_CODE)
                }
                items[i] == "gallery" -> {
                    val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    intent.type = "image/*"
                    startActivityForResult(intent, GALLERY_REQ_CODE)
                }
                items[i] == "cancel" -> {
                    dialogInterface?.dismiss()
                }
            }
        }
    }

    private fun saveNote() {
        val name = rootView.et_name.text.toString()
        val surname = rootView.et_surname.text.toString()
        val birthday = rootView.et_birthday.text.toString().toLong()

        val data = Notes(
            name,
            surname,
            birthday
        )
//        val note = HashMap<String, Any>()
//        note.put(KEY_NAME,name)
//        note.put(KEY_SURNAME,surname)

        noteRef
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Note saved", Toast.LENGTH_SHORT).show()

                Log.d("saveNote", "Note saved")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Wrong!", Toast.LENGTH_SHORT).show()
                Log.d("saveNote", it.toString())
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null && requestCode == PICK_IMAGE_REQUEST) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
//            if (picturePath != null){
//                loadFromStorage(picturePath)
//                Log.d("Picture Path", picturePath)
//            }
        }

    }

    private fun uploadImageAndSaveUri(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("profilPics/${FirebaseAuth.getInstance().currentUser?.uid}/${UUID.randomUUID()}.jpeg")
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val image = baos.toByteArray()

        val upload = ref.putBytes(image)

        upload.addOnCompleteListener {
            if (upload.isSuccessful){
                ref.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it

                        Toast.makeText(context,"$imageUri",Toast.LENGTH_SHORT).show()

                        rootView.iv_image_from_storage.setImageBitmap(imageBitmap)
                    }
                }
            } else {
                upload.exception?.let {
                    Log.e("upload","${it.message}")
                }
            }
        }
    }

//    private fun getImagePath(contentUri: Uri?): String? {
//        var result: String? = null
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        if (contentUri != null){
//            val cursor = context?.applicationContext?.getContentResolver()?.query(contentUri, proj, null, null, null)
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    val column_index = cursor.getColumnIndexOrThrow(proj[0])
//                    result = cursor.getString(column_index)
//                }
//                cursor.close()
//            }
//        }
//        if (result == null) {
//            result = "Not found"
//        }
//        return result
//
//    }
//    private fun loadFromStorage(picturePath: String?) {
//        try {
//
//            val file = File(picturePath,"")
//            val b = BitmapFactory.decodeStream(FileInputStream(file))
//        } catch (e:Exception){
//            Log.e("loadFromStorage",e.message!!)
//        }
//
//
//    }

    private fun getGalarey(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity?.packageManager!!).also {
                startActivityForResult(intent,
                    PICK_IMAGE_REQUEST
                )
            }
        }
    }



    private fun onBackPressed(){
        activity?.finish()
    }

}