package com.appvantis.d2kassignment.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.appvantis.d2kassignment.AppDB
import com.appvantis.d2kassignment.PantSliderItem
import com.appvantis.d2kassignment.R
import com.appvantis.d2kassignment.ShirtSliderItem
import com.appvantis.d2kassignment.adapter.PantSliderAdapter
import com.appvantis.d2kassignment.adapter.ShirtSliderAdapter
import com.appvantis.d2kassignment.tranformer.CubeInRotationTransformation
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_main.toolbar


class FavouriteActivity : AppCompatActivity() {
    private val ShirtImages = arrayListOf<ShirtSliderItem>()
    private val PantImages = arrayListOf<PantSliderItem>()
    private lateinit var db: AppDB
    private lateinit var shirtSliderAdapter: ShirtSliderAdapter
    private lateinit var pantSliderAdapter: PantSliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        toolbar.setTitle("D2k Assignment")
        db = Room.databaseBuilder(applicationContext, AppDB::class.java, "D2k_DB").build()
        shirtSliderAdapter =
            ShirtSliderAdapter(
                this@FavouriteActivity,
                ShirtImages,
                shirtVP
            )
        shirtVP.adapter = shirtSliderAdapter
        shirtVP.setPageTransformer(CubeInRotationTransformation())
        pantSliderAdapter = PantSliderAdapter(
            this@FavouriteActivity,
            PantImages,
            pantVP
        )
        pantVP.adapter = pantSliderAdapter
        btn_shuffle.setOnClickListener {
            shuffleShirt(shirtVP.currentItem)
            shufflePant(pantVP.currentItem)
        }

        Thread {
            getItem()
        }.start()
        pantVP.setPageTransformer(CubeInRotationTransformation())
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

    private fun getItem() {
        val list = db.favDao().readFav()
        if (list.size > 0) {
            ll_parent_view.visibility = View.VISIBLE
            btn_shuffle.visibility = View.VISIBLE
            no_item.visibility = View.GONE
            no_image.visibility=View.GONE
            ShirtImages.clear()
            PantImages.clear()
            (0..list.size - 1).forEach {
                ShirtImages.add(ShirtSliderItem(list[it].fav_shirt))
                PantImages.add(PantSliderItem(list[it].fav_pant))
            }


        } else {
            ll_parent_view.visibility = View.GONE
            btn_shuffle.visibility = View.GONE
            no_item.visibility = View.VISIBLE
            no_image.visibility=View.VISIBLE
            showToast("List is empty")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //noinspection SimplifiableIfStatement
        if (item.itemId == R.id.action_delete) {
            deleteDailog()
            return true
        }

        return super.onOptionsItemSelected(item);
    }

    private fun deleteDailog() {
        val alertDialog: LottieAlertDialog
        alertDialog =
            LottieAlertDialog.Builder(this@FavouriteActivity, DialogTypes.TYPE_CUSTOM, "alert.json")
                .setTitle("Delete")
                .setDescription("Are you sure you want remove from favouite..?")
                .setPositiveText("Ok")
                .setNegativeText("cancel")
                .setPositiveListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                        removeFav()
                    }

                })
                .setNegativeListener(object : ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                    }
                }).build()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun removeFav() {
        if (PantImages.size > 0) {
            Thread {
                val id = db.favDao().checkifExist(
                    PantImages[pantVP.currentItem].image,
                    ShirtImages[shirtVP.currentItem].image
                )
                Log.i("delete id", id.toString())
                if (id > 0) {
                    var delete_id = db.favDao().deleteFav(
                        PantImages[pantVP.currentItem].image,
                        ShirtImages[shirtVP.currentItem].image
                    )
                    if (delete_id > 0) {

                        showToast("Combination removed successfully")
                        runOnUiThread {
                            PantImages.removeAt(pantVP.currentItem)
                            ShirtImages.removeAt(shirtVP.currentItem)
                            shirtSliderAdapter.notifyDataSetChanged()
                            pantSliderAdapter.notifyDataSetChanged()
                            if (ShirtImages.size > 0) {
                                ll_parent_view.visibility = View.VISIBLE
                                btn_shuffle.visibility = View.VISIBLE
                                no_item.visibility = View.GONE
                                no_image.visibility=View.GONE

                            } else {
                                ll_parent_view.visibility = View.GONE
                                btn_shuffle.visibility = View.GONE
                                no_item.visibility = View.VISIBLE
                                no_image.visibility=View.VISIBLE
                            }
                        }

                    }
                } else {
                    showToast("this combination is not in favourite")
                }
            }.start()
        } else {
            TastyToast.makeText(
                this@FavouriteActivity,
                "Combination list is empty",
                TastyToast.LENGTH_SHORT,
                TastyToast.INFO
            )
        }
    }

    fun showToast(toast: String?) {
        runOnUiThread {
            TastyToast.makeText(
                this@FavouriteActivity,
                toast,
                TastyToast.LENGTH_SHORT,
                TastyToast.ERROR
            )
        }
    }
}
