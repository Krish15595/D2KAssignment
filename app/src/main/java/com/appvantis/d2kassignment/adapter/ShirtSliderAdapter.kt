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
import com.appvantis.d2kassignment.R
import com.appvantis.d2kassignment.ShirtSliderItem
import com.appvantis.d2kassignment.adapter.ShirtSliderAdapter.ShirtSliderViewHolder
import com.sdsmdg.tastytoast.TastyToast
import java.io.File
import java.lang.Exception

class ShirtSliderAdapter(
    private val mContext: Context,
    private val sliderItems: ArrayList<ShirtSliderItem>,
    private val shirtVP: ViewPager2
) : RecyclerView.Adapter<ShirtSliderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShirtSliderViewHolder {
        return ShirtSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ShirtSliderViewHolder,
        position: Int
    ) {
        holder.setImage(sliderItems.get(position))
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class ShirtSliderViewHolder(itemView: View) :
        ViewHolder(itemView) {
        private val shirt: AppCompatImageView
        fun setImage(sliderItem: ShirtSliderItem) {
            try {
                shirt.setImageResource(sliderItem.image.toInt())
            }
            catch (e:Exception)
            {
              val shirtImgFile=File(sliderItem.image)
                if(shirtImgFile.exists())
                {
                    val bitmap=BitmapFactory.decodeFile(shirtImgFile.absolutePath)
                    shirt.setImageBitmap(bitmap)
                }
                else
                {
                    TastyToast.makeText(mContext,"File has been deleted",TastyToast.LENGTH_SHORT,TastyToast.ERROR)
                }
            }
        }

        init {
            shirt = itemView.findViewById(R.id.item)
        }
    }

}