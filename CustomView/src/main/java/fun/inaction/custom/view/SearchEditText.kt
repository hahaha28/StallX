package `fun`.inaction.custom.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView

class SearchEditText:FrameLayout{

    private  val inputEditText: EditText
    private val cancelButton: ImageView

    private var textChangedListener: ((String)->Unit)? = null

    constructor(context: Context, attributes: AttributeSet):super(context, attributes){
        val view = LayoutInflater.from(context).inflate(
            R.layout.search_edit_text,
            this,
            true
        )
        inputEditText = view.findViewById(R.id.editTextTextPersonName)
        cancelButton = view.findViewById(R.id.cancelBtn)


        //监听用户输入，有输入则显示cancelButton
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    cancelButton.visibility = INVISIBLE
                } else {
                    cancelButton.visibility = VISIBLE
                }
                textChangedListener?.invoke(getText())
            }

            override fun afterTextChanged(s: Editable) {}
        })


        //点击cancelButton删除用户输入
        cancelButton.setOnClickListener { inputEditText.setText("") }

        //设置回车键搜索
//        inputEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
//                //搜索回调
//                if (onSearchListener != null) {
//                    onSearchListener.onSearch(inputEditText.text.toString())
//                }
//                return@OnEditorActionListener true
//            }
//            false
//        })

    }

    fun getText():String = inputEditText.text.toString()

    /**
     * 设置文字改变监听
     */
    fun setTextChangedListener(listener:(String)->Unit){
        textChangedListener = listener
    }

}