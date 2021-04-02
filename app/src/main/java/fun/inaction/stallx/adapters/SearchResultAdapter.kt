package `fun`.inaction.stallx.adapters

import `fun`.inaction.stallx.R
import com.baidu.mapapi.search.sug.SuggestionResult
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.StringBuilder
import java.net.Inet4Address


class SearchResultAdapter : BaseQuickAdapter<SuggestionResult.SuggestionInfo, BaseViewHolder>(
    R.layout.adapter_search_result
) {

    override fun convert(holder: BaseViewHolder, item: SuggestionResult.SuggestionInfo) {

        // 设置标题
        holder.setText(R.id.normalTitle,item.key)
        // 设置地址
        holder.setText(R.id.address,item.address.substringAfter("-"))
        // 设置tag
        holder.setText(R.id.tag,item.tag)

    }


}