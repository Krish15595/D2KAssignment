package com.appvantis.d2kassignment.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.appvantis.d2kassignment.*
import com.appvantis.d2kassignment.adapter.PantSliderAdapter
import com.appvantis.d2kassignment.adapter.ShirtSliderAdapter
import com.appvantis.d2kassignment.enitity.FavouriteEntity
import com.appvantis.d2kassignment.tranformer.CubeInRotationTransformation
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_main.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private val ShirtImages = arrayListOf<ShirtSliderItem>()
    private val PantImages = arrayListOf<PantSliderItem>()
    private val shirtImageList = arrayListOf<String>()
    private val pantImageList = arrayListOf<String>()
    private lateinit var db: AppDB
    private val inflater: LayoutInflater? = null
    private val context: Context? = null
    private var imageSelection = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        toolbar.setTitle("D2k Assignment")
        db = Room.databaseBuilder(applicationContext, AppDB::class.java, "D2k_DB").build()
        EasyImage.configuration(this@MainActivity)
            .setImagesFolderName("D2k")
            .setCopyTakenPhotosToPublicGalleryAppFolder(true)
            .setCopyPickedImagesToPublicGalleryAppFolder(true)
            .setAllowMultiplePickInGallery(true);
        btn_add_new_shirt.setOnClickListener {
            imageSelection = "shirt"
            checkPermissions()
        }
        btn_add_new_pant.setOnClickListener {
            imageSelection = "pant"
            checkPermissions()
        }
        btn_fav.setOnClickListener {
            insertFav()
        }
        shirtVP.adapter = ShirtSliderAdapter(
            this@MainActivity,
            ShirtImages,
            shirtVP
        )
        shirtVP.setPageTransformer(CubeInRotationTransformation())
        ShirtImages.add(
            ShirtSliderItem(
                R.drawable.tshirt1.toString()
            )
        )
        ShirtImages.add(
            ShirtSliderItem(
                R.drawable.tshirt2.toString()
            )
        )
        ShirtImages.add(
            ShirtSliderItem(
                R.drawable.tshirt3.toString()
            )
        )
      ShirtImages.add(
            ShirtSliderItem(
                R.drawable.shirt4.toString()
            )
        )
      ShirtImages.add(
            ShirtSliderItem(
                R.drawable.shirt5.toString()
            )
        )
      ShirtImages.add(
            ShirtSliderItem(
                R.drawable.shirt6.toString()
            )
        )
      ShirtImages.add(
            ShirtSliderItem(
                R.drawable.shirt7.toString()
            )
        )
      ShirtImages.add(
            ShirtSliderItem(
                R.drawable.shirt8.toString()
            )
        )



        PantImages.add(
            PantSliderItem(
                R.drawable.pant1.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant2.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant3.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant4.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant5.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant6.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant7.toString()
            )
        )
        PantImages.add(
            PantSliderItem(
                R.drawable.pant8.toString()
            )
        )
        pantVP.setPageTransformer(CubeInRotationTransformation())
        pantVP.setAdapter(
            PantSliderAdapter(
                this@MainActivity,
                PantImages,
                pantVP
            )
        )
        shuffle.setOnClickListener {
            shuffleShirt(shirtVP.currentItem)
            shufflePant(pantVP.getCurrentItem())

        }

    }

    private fun insertFav() {
        Thread {
            val id = db.favDao().checkifExist(
                PantImages[pantVP.currentItem].image,
                ShirtImages[shirtVP.currentItem].image
            )
            Log.i("exist id", id.toString())
            if (id <1 ) {

                val fav = FavouriteEntity()
                fav.fav_pant = PantImages.get(pantVP.currentItem).image
                fav.fav_shirt = ShirtImages.get(shirtVP.currentItem).image
                    val id = db.favDao().savFav(fav)
                if(id>0){
                    db.favDao().readFav().forEach {

                    }
                    showToast("Combination added to favourite list","success")
                }
                else
                {
                    showToast("Unable to add in fav list","error")
                }
            }
            else
            {
                showToast("This combination is already added is fav list","error")
            }
        }.start()
    }

    fun showToast(toast: String?,type:String) {
        runOnUiThread {
            if(type=="error") {
                TastyToast.makeText(
                    this@MainActivity,
                    toast,
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                )
            }
            else
            {
                TastyToast.makeText(
                    this@MainActivity,
                    toast,
                    TastyToast.LENGTH_SHORT,
                    TastyToast.SUCCESS
                )
            }
        }
    }

    private fun shufflePant(pantCurrentItem: Int) {
        if (PantImages.size > 1) {
            var pantShuffle: Int
            do {
                pantShuffle = (0..PantImages.size - 1).shuffled().first()
            } while (pantCurrentItem == pantShuffle)
            Log.i("pantposition", pantCurrentItem.toString() + "\n" + pantShuffle)
            pantVP.currentItem = pantShuffle
        }
    }

    private fun shuffleShirt(shirtCurrentItem: Int) {
        if (ShirtImages.size > 1) {
            var shirtShuffle: Int
            do {
                shirtShuffle = (0..ShirtImages.size - 1).shuffled().first()
            } while (shirtCurrentItem == shirtShuffle)
            shirtVP.currentItem = shirtShuffle
        }
    }

    private fun checkPermissions() {
        //check Permission
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val rationale = "Please provide location permission so that you can ..."
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(
            this /*context*/,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() { // do your task.
                    EasyImage.openChooserWithGallery(this@MainActivity, "Please choose type", 0)
                }

                override fun onDenied(
                    context: Context?,
                    deniedPermissions: ArrayList<String?>?
                ) { // permission denied, block the feature.
                    permissionDenied()
                }
            })
    }

    private fun permissionDenied() {
         startActivity( Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : EasyImage.Callbacks {
                override fun onImagesPicked(
                    p0: MutableList<File>,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {
                    onPhotosReturned(p0)
                }

                override fun onImagePickerError(
                    p0: Exception?,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {
                    if (p0 != null) {
                        p0.printStackTrace()
                    }
                }

                override fun onCanceled(p0: EasyImage.ImageSource?, p1: Int) {

                }

            })
    }

    private fun onPhotosReturned(p0: MutableList<File>) {
        val filePath = compressImage(p0[0].path)
        if (imageSelection == "shirt") {
            ShirtImages.add(ShirtSliderItem(filePath))
            shirtVP.currentItem = ShirtImages.size - 1
        } else {
            PantImages.add(PantSliderItem(filePath))
            pantVP.currentItem = PantImages.size - 1
        }
        Log.i("list", p0.toString())
    }

    fun compressImage(filePath: String): String {

        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 1632.0f
        val maxWidth = 1224.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    fun getFilename(): String {
        val file = File(Environment.getExternalStorageDirectory().path, "Pictures/D2K/")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + "D2k_" + UUID.randomUUID().toString() + "_IMG_" + System.currentTimeMillis() + ".jpg"
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //noinspection SimplifiableIfStatement
        if (item.itemId == R.id.action_favorite) {
            startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
