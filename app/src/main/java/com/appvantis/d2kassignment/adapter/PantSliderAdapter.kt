package com.appvantis.d2kassignment.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.appvantis.d2kassignment.PantSliderItem
import com.appvantis.d2kassignment.R
import com.sdsmdg.tastytoast.TastyToast
import java.io.File
import java.lang.Exception

class PantSliderAdapter(
    private val mContext: Context,
    private val sliderItems: ArrayList<PantSliderItem>,
    private val pantVP: ViewPager2
) : RecyclerView.Adapter<PantSliderAdapter.pantSliderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pantSliderViewHolder {
        return pantSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: pantSliderViewHolder,
        position: Int
    ) {
        holder.setImage(sliderItems.get(position))
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class pantSliderViewHolder(itemView: View) :
        ViewHolder(itemView) {
        private val pant: AppCompatImageView
        fun setImage(sliderItem: PantSliderItem) {
            try {
                pant.setImageResource(sliderItem.image.toInt())
            }
            catch (e: Exception)
            {
                val shirtImgFile= File(sliderItem.image)
                if(shirtImgFile.exists())
                {
                    val bitmap= BitmapFactory.decodeFile(shirtImgFile.absolutePath)
                    pant.setImageBitmap(bitmap)
                }
                else
                {
                    TastyToast.makeText(mContext,"File has been deleted",
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR)
                }
            }
        }

        init {
            pant = itemView.findViewById(R.id.item)
        }
    }

}