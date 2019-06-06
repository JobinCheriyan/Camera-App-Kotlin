package experion.com.cameralibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.widget.Toast
import experion.com.cameralibrary.constants.Constant
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*Camera class have storageDirectory as primary constructor,
 * where we store the image file */
class Camera(storageDirectory: File) {
    private var imageFilePath: String? = null
    private var storageDirectory: File = storageDirectory
    private var constant: Constant = Constant()

    /* Method which executes the camera intent */
    fun openCamera(context: Context, requestCode: Int, externalStoragePermission: Boolean) {
        /*Directory existence check*/
        if (storageDirectory.exists()) {

            /*Permission check for external storage*/
            if (externalStoragePermission) {
                val activityContext = context as Activity
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
                    var photoFile: File? = null

                    try {
                        /*getting the image file created*/
                        photoFile = createImageFile(context, storageDirectory)
                    } catch (ex: IOException) {

                    }

                    if (photoFile != null) {
                        val photoURI =
                            FileProvider.getUriForFile(context, constant.AUTHORITY, photoFile)
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI)
                        activityContext.startActivityForResult(cameraIntent, requestCode)

                    } else {
                        context.toast(constant.PHOTO_FILE_CANT_CREATE)
                    }
                }


            } else {
                context.toast(constant.PROVIDE_CAMERA_PERMISSION)
            }
        } else {
            context.toast(constant.DIRECTORY_DOESNT_EXIST)
        }


    }

    /*Method receiving the camera intent and returning  image uri*/
    fun onActivityResult(requestCodeCheck: Boolean, resultCode: Int, data: Intent?, context: Context): Uri? {
        var imageFilePathUri: Uri? = null
        /*check whether the request code matches or not */
        if (requestCodeCheck) {
            imageFilePathUri = Uri.parse(imageFilePath)
        } else {
            Toast.makeText(context, constant.REQUEST_CODE_DOSENT_MATCH, Toast.LENGTH_LONG).show()
        }

        return imageFilePathUri
    }
/* Method to generate the image file*/
    @Throws(IOException::class)
    private fun createImageFile(context: Context, storageDirectory: File): File {
        val timeStamp = SimpleDateFormat(
            constant.PATTERN,
            Locale.getDefault()
        ).format(Date())
        val imageFileName = constant.FILE_NAME_PREFIX + timeStamp + "_"

        val imageFile = File.createTempFile(
            imageFileName, /* prefix */
            constant.FILE_EXTENSION, /* suffix */
            storageDirectory      /* directory */
        )


        imageFilePath = imageFile.absolutePath


        return imageFile
    }


}



