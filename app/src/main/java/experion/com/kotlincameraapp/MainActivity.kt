package experion.com.kotlincameraapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import experion.com.cameralibrary.Camera
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private val REQUEST_CAPTURE_IMAGE = 1888
    lateinit var camera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_open_camera.setOnClickListener {

            /*Create an object of Camera class and pass the directory
             in which we want to save the image file*/

            camera = Camera(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))

            /*call the method openCamera of Camera class to open and take a pic*/
            /*We should pass context of the activity , request code, external
             storage permission check status */

            camera.openCamera(
                this,
                REQUEST_CAPTURE_IMAGE,
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
        button_go_back.setOnClickListener {
            image_view_camera_image.setImageBitmap(null)
            image_view_camera_image.visibility = View.GONE
            button_go_back.visibility = View.GONE
            button_open_camera.visibility = View.VISIBLE

        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        /*To get the Uri of the image taken ,we should call onActivityResult method of
         Camera class as follow*/
        var imagePath: Uri? = camera.onActivityResult(requestCode == REQUEST_CAPTURE_IMAGE, requestCode, data, this)

        image_view_camera_image.setImageURI(imagePath)
        image_view_camera_image.visibility = View.VISIBLE
        button_open_camera.visibility = View.GONE
        button_go_back.visibility = View.VISIBLE

    }
}
