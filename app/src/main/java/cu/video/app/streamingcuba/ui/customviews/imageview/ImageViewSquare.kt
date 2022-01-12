package cu.video.app.streamingcuba.ui.customviews.imageview

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Pinkal on 13/7/17.
 */

class ImageViewSquare : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // This is the key that will make the height equivalent to its width
    }

    /** How to use it on XML
     *
    <..package...imageview.imagezoom.ImageViewTouch
    android:id="@+id/_pagerPhotoImg"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
     */
}
