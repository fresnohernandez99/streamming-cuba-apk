package cu.video.app.streamingcuba.utils.imageLoader

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import cu.video.app.streamingcuba.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class ImageLoader @Inject constructor(@ApplicationContext private val context: Context) {

    fun loadAsset(imgName: String, imageView: ImageView, center: Boolean) {
        val charge =
            GlideApp.with(context)
                .load(Uri.parse("file:///android_asset/imgs/$imgName"))
                .placeholder(R.drawable.placeholder)
        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

    fun loadAssetForTutorial(imgName: String, imageView: ImageView, center: Boolean) {
        val charge =
            GlideApp.with(context)
                .load(Uri.parse("file:///android_asset/imgs/$imgName"))
                .placeholder(R.drawable.placeholder)
                .override(1240, 720)
        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

    fun loadDrawable(imgName: String, imageView: ImageView, center: Boolean) {
        val img = ContextCompat.getDrawable(
            context,
            context.resources.getIdentifier(imgName, "drawable", context.packageName)
        )
        val charge =
            GlideApp.with(context)
                .load(img)
                .placeholder(R.drawable.placeholder)
        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

    fun loadResWithRoundedCorners(@DrawableRes resId: Int, imageView: ImageView, radius: Int) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            RoundedCorners(radius)
        )

        GlideApp.with(context)
            .load(ContextCompat.getDrawable(context, resId))
            .apply(RequestOptions.bitmapTransform(multiTransformation))
            .into(imageView)
    }

    fun loadPathWithRoundedCorners(path: String, imageView: ImageView, radius: Int) {
        val multiTransformation = MultiTransformation(
            CenterCrop(),
            RoundedCorners(radius)
        )

        GlideApp.with(context)
            .load(path)
            .apply(RequestOptions.bitmapTransform(multiTransformation))
            .into(imageView)
    }

    fun loadRes(@DrawableRes resId: Int, imageView: ImageView, center: Boolean) {
        val charge =
            GlideApp.with(context)
                .load(ContextCompat.getDrawable(context, resId))

        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

    fun loadPath(path: String, imageView: ImageView, center: Boolean) {
        val charge =
            GlideApp.with(context)
                .load(path)

        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

    fun loadWithListener(path: String, imageView: ImageView, imageViewTemp: ImageView) {
        GlideApp.with(context)
            .load(path)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

            })
            .into(imageViewTemp)
    }

    fun loadPathWithPlaceholder(
        path: String, imageView: ImageView, @DrawableRes resId: Int, center: Boolean
    ) {
        val charge =
            GlideApp.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(resId)

        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)

    }

    fun loadImageWithPlaceholderWithListener(
        path: String,
        imageView: ImageView,
        @DrawableRes resId: Int,
        center: Boolean
    ) {
        val t1 = Calendar.getInstance().timeInMillis
        val charge =
            GlideApp.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val t2 = Calendar.getInstance().timeInMillis
                        val tf = t2 - t1
                        Log.d("GlideAppTest", "Load times: $tf")
                        if (resource is GifDrawable) {
                            resource.setLoopCount(1)
                        }
                        return false
                    }

                })
                .placeholder(resId)
                .useUnlimitedSourceGeneratorsPool(true)
                .onlyRetrieveFromCache(true)

        if (center) {
            charge
                .centerCrop()
                .into(imageView)
        } else charge.into(imageView)
    }

}