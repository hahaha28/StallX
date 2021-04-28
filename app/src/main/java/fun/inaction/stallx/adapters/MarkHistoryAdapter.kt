package `fun`.inaction.stallx.adapters

import `fun`.inaction.network.bean.Mark
import `fun`.inaction.stallx.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class MarkHistoryAdapter:
    BaseQuickAdapter<Mark, BaseViewHolder>(R.layout.adapter_mark_history) {


    override fun convert(holder: BaseViewHolder, item: Mark) {
        holder.setText(R.id.name,item.name)
        holder.setText(R.id.time,item.time.toString())

    }
}