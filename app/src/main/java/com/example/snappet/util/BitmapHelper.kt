package com.example.snappet.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapHelper {
    fun vectorToBitmap(
        context: Context,
        @DrawableRes id: Int,
        @ColorInt color: Int,
        width: Int,
        height: Int
    ): BitmapDescriptor{
        val vectorDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
        if(vectorDrawable == null){
            return BitmapDescriptorFactory.defaultMarker()
        }

       /* val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )*/

        val scaledBitmap = Bitmap.createScaledBitmap(
            Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            ),
            width,
            height,
            false
        )

        //val canvas = Canvas(bitmap)
        val canvas = Canvas(scaledBitmap)
        vectorDrawable.setBounds(0,0,canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)

        //return BitmapDescriptorFactory.fromBitmap(bitmap)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)

    }
}