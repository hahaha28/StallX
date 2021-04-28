package `fun`.inaction.stallx.adapters

import `fun`.inaction.network.bean.Collection
import `fun`.inaction.stallx.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sackcentury.shinebuttonlib.ShineButton
import java.text.SimpleDateFormat
import java.util.*

class CollectionAdapter:BaseQuickAdapter<Collection,BaseViewHolder>(R.layout.adapter_collection_list) {

    init {
        addChildClickViewIds(R.id.collectBtn)
    }

    override fun convert(holder: BaseViewHolder, item: Collection) {
        holder.setText(R.id.parkName,item.parkName)
        holder.setText(R.id.time,SimpleDateFormat("yyyy-MM-dd").format(Date(item.timestamp)))
        val shineBtn = holder.getView<ShineButton>(R.id.collectBtn)
        shineBtn.isChecked = true
    }

}