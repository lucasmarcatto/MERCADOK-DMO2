package br.com.lucasmarcatto.microrslucasmarcartto.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64Converter {
    companion object {
        fun drawableToString(drawable: Drawable): String {
            val pictureDrawable = drawable as BitmapDrawable
            // redimensiona para 150x150
            val bitmap = Bitmap.createScaledBitmap(pictureDrawable.bitmap, 150, 150, true)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val imageString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
            return imageString
        }

        fun stringToBitmap(imageString: String): Bitmap {
            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            return decodedImage
        }
    }
}