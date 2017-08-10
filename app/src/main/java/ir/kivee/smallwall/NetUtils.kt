package ir.kivee.smallwall

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.IOException
import java.net.URL

/**
 * Created by payam on 8/7/17.
 */
class NetUtils constructor(con: Context) {

    private val apiUrl = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-Us"
    private var context = con

    private fun readUrl(url: String): String {
        val data = URL(url).readText()
        return data
    }

    private fun deserializeData(data: String): String {
        val jsonObj = JSONObject(data)
        val imageArray = jsonObj.getJSONArray("images")
        var imageUrl = imageArray
                .getJSONObject(0).getString("url")
        imageUrl = "http://www.bing.com" + imageUrl.replace("1920x1080", "1080x1920")
        return imageUrl
    }

    fun mainStack() {
        doAsync {
            var result = readUrl(apiUrl)
            var imageUrl = deserializeData(result)
            var downloadedImage = downloadImage(imageUrl)
            setWallpaper(downloadedImage)
            if (context is Activity) {
                uiThread {
                    context.toast("Done!")
                }
            }
        }
    }

    private fun downloadImage(imgUrl: String): Bitmap {
        var resImage = Picasso.with(context)
                .load(imgUrl)
                .get()
        return resImage
    }

    private fun setWallpaper(img: Bitmap) {
        var wallManager = WallpaperManager.getInstance(context)
        val fullWidth = Resources.getSystem().displayMetrics.widthPixels
        val fullHeight = Resources.getSystem().displayMetrics.heightPixels
        val imgResized = Bitmap.createScaledBitmap(img, fullWidth, fullHeight,true)
        try {
            wallManager.setBitmap(imgResized)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}