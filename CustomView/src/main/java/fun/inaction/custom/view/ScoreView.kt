package `fun`.inaction.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sackcentury.shinebuttonlib.ShineButton

class ScoreView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var btn1 :ShineButton
    private var btn2 :ShineButton
    private var btn3 :ShineButton
    private var btn4 :ShineButton
    private var btn5 :ShineButton


    init {
        LayoutInflater.from(context).inflate(R.layout.score_view,this,true)
        btn1 = findViewById<ShineButton>(R.id.score1)
        btn2 = findViewById<ShineButton>(R.id.score2)
        btn3 = findViewById<ShineButton>(R.id.score3)
        btn4 = findViewById<ShineButton>(R.id.score4)
        btn5 = findViewById<ShineButton>(R.id.score5)

        btn2.setOnCheckStateChangeListener { view, checked ->
            btn1.isChecked = checked
        }
        btn3.setOnCheckStateChangeListener { view, checked ->
            btn1.isChecked = checked
            btn2.isChecked = checked
        }
        btn4.setOnCheckStateChangeListener { view, checked ->
            btn1.isChecked = checked
            btn2.isChecked = checked
            btn3.isChecked = checked
        }
        btn5.setOnCheckStateChangeListener { view, checked ->
            btn1.isChecked = checked
            btn2.isChecked = checked
            btn3.isChecked = checked
            btn4.isChecked = checked
        }

    }

    fun getScore():Int{
        var score = 0
        if(btn1.isChecked)
            score ++
        if(btn2.isChecked)
            score++
        if(btn3.isChecked)
            score++
        if(btn4.isChecked)
            score++
        if(btn5.isChecked)
            score++
        return score
    }

}