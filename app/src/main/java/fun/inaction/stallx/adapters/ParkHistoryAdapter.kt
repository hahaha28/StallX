package `fun`.inaction.stallx.adapters

import `fun`.inaction.network.bean.History
import `fun`.inaction.stallx.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class ParkHistoryAdapter:BaseQuickAdapter<History,BaseViewHolder>(R.layout.adapter_park_history) {

    init {
        addChildClickViewIds(R.id.commentBtn)
    }

    override fun convert(holder: BaseViewHolder, item: History) {
        holder.setText(R.id.parkName,item.parkName)
        holder.setText(R.id.time,SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(item.timestamp)))
    }

}