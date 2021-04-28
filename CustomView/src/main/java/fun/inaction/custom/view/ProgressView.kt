package `fun`.inaction.custom.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout

class ProgressView(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.progress_view,this,true)
    }

}