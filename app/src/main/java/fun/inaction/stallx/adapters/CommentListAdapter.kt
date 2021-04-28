package `fun`.inaction.stallx.adapters

import `fun`.inaction.network.bean.Comment
import `fun`.inaction.stallx.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class CommentListAdapter:BaseQuickAdapter<Comment,BaseViewHolder>(R.layout.adapter_comment_list) {

    override fun convert(holder: BaseViewHolder, item: Comment) {
        holder.setText(R.id.commentUserName,item.userName)
        holder.setText(R.id.commentTime,SimpleDateFormat("yyyy-MM-dd").format(Date(item.timestamp)))
        holder.setText(R.id.comment,item.comment)
    }

}