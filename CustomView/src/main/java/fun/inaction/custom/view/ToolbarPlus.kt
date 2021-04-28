package `fun`.inaction.custom.view

import `fun`.inaction.custom.view.utils.dp2px
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar

class ToolbarPlus(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {

    init {
        setNavigationOnClickListener {
            onNavigationOnClickListener(it)
        }
        setBackgroundColor(Color.WHITE)
    }



    companion object{
        var onNavigationOnClickListener : (View)->Unit = {}
    }

}