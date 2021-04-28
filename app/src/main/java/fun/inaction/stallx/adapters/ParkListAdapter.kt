package `fun`.inaction.stallx.adapters

import `fun`.inaction.network.bean.Park
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.utils.MapUtil
import `fun`.inaction.stallx.utils.hide
import android.content.res.ColorStateList
import android.graphics.Color
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.chip.Chip

class ParkListAdapter(val targetLongitude: Double, val targetLatitude: Double) :
    BaseQuickAdapter<Park, BaseViewHolder>(R.layout.adapter_park_list) {

    override fun convert(holder: BaseViewHolder, item: Park) {

        holder.setText(R.id.parkName, item.name)
        val chargeChip = holder.getView<Chip>(R.id.chargeChip)
        when (item.isCharged) {
            0 -> chargeChip.hide()
            1 -> {
                chargeChip.text = "免费"
                chargeChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#00C853"))
            }
            2 -> {
                chargeChip.text = "收费"
                chargeChip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FF5252"))
            }
        }
        val parkTypeChip = holder.getView<Chip>(R.id.parkTypeChip)
        when (item.type) {
            1 -> parkTypeChip.text = "地下"
            2 -> parkTypeChip.text = "地上"
            3 -> parkTypeChip.text = "路边"
            4 -> parkTypeChip.text = "未划线"
        }

        var distance = DistanceUtil.getDistance(LatLng(targetLatitude,targetLongitude),LatLng(item.latitude,item.longitude))
        distance /= 1000
        val disStr = String.format("%.2f",distance)

        holder.setText(R.id.distanceDesc,"距离目的地${disStr}km")


    }

}