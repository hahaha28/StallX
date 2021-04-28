package `fun`.inaction.stallx.adapters

import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.custom.RouteResultWindow
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RoutePlanAdapter: BaseQuickAdapter<RouteResultWindow.RoutePlan, BaseViewHolder>
    (R.layout.adapter_route_result) {

    var selectIndex:Int = 0
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun convert(holder: BaseViewHolder, item: RouteResultWindow.RoutePlan) {

        holder.setText(R.id.name,item.name)
        holder.setText(R.id.time,getTime(item.time))
        holder.setText(R.id.distance,"${(item.distance/1000).toInt()}公里")
        holder.setText(R.id.lightNum,"${item.lights}")

        if(holder.adapterPosition == selectIndex){
            holder.setTextColorRes(R.id.name,R.color.themeColor)
            holder.setTextColorRes(R.id.time,R.color.themeColor)
            holder.setTextColorRes(R.id.distance,R.color.themeColor)
            holder.setTextColorRes(R.id.lightNum,R.color.themeColor)
            holder.itemView.isSelected = true
        }else{
            holder.setTextColorRes(R.id.name,R.color.colorTextLight)
            holder.setTextColorRes(R.id.time,R.color.colorText)
            holder.setTextColorRes(R.id.distance,R.color.colorTextLight)
            holder.setTextColorRes(R.id.lightNum,R.color.colorTextLight)
            holder.itemView.isSelected = false
        }


    }

    private fun getTime(time:Double):String{
        var minute:Int = (time/60).toInt()
        if(minute > 60){
            var hour = minute / 60
            minute -= hour * 60
            return "${hour}小时${minute}分钟"
        }
        return  "${minute}分钟"
    }

}