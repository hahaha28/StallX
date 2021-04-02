package `fun`.inaction.custom.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PointView(context: Context, attrs: AttributeSet?) : View(context, attrs) {


    /**
     * 圆点的颜色
     */
    var color:Int = Color.BLACK
        set(value) {
            field = value
            paint.color = color
            invalidate()
        }

    private val paint = Paint()

    init {
        // 获取xml的属性值
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs,R.styleable.PointView)

            color = typedArray.getColor(R.styleable.PointView_color,Color.BLACK)

            typedArray.recycle()
        }
        // 设置画笔
        paint.color = color
        paint.isAntiAlias = true

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.drawCircle(width/2f,height/2f,width/2f,paint)
    }
}