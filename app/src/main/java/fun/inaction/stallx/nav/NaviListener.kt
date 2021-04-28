package `fun`.inaction.stallx.nav

import android.graphics.Bitmap
import android.os.Message
import android.view.View
import com.baidu.navisdk.adapter.IBNaviListener
import com.baidu.navisdk.adapter.struct.BNHighwayInfo
import com.baidu.navisdk.adapter.struct.BNRoadCondition
import com.baidu.navisdk.adapter.struct.BNaviInfo
import com.baidu.navisdk.adapter.struct.BNaviLocation
import com.baidu.navisdk.ui.routeguide.model.RGLineItem

open class NaviListener:IBNaviListener {
    override fun onRoadNameUpdate(p0: String?) {
    }

    override fun onRemainInfoUpdate(p0: Int, p1: Int) {
    }

    override fun onViaListRemainInfoUpdate(p0: Message?) {
    }

    override fun onGuideInfoUpdate(p0: BNaviInfo?) {
    }

    override fun onHighWayInfoUpdate(p0: IBNaviListener.Action?, p1: BNHighwayInfo?) {
    }

    override fun onFastExitWayInfoUpdate(
        p0: IBNaviListener.Action?,
        p1: String?,
        p2: Int,
        p3: String?
    ) {
    }

    override fun onEnlargeMapUpdate(
        p0: IBNaviListener.Action?,
        p1: View?,
        p2: String?,
        p3: Int,
        p4: String?,
        p5: Bitmap?
    ) {
    }

    override fun onDayNightChanged(p0: IBNaviListener.DayNightMode?) {
    }

    override fun onRoadConditionInfoUpdate(p0: Double, p1: MutableList<BNRoadCondition>?) {
    }

    override fun onMainSideBridgeUpdate(p0: Int) {
    }

    override fun onLaneInfoUpdate(p0: IBNaviListener.Action?, p1: MutableList<RGLineItem>?) {
    }

    override fun onSpeedUpdate(p0: String?, p1: Boolean) {
    }

    override fun onArriveDestination() {
    }

    override fun onArrivedWayPoint(p0: Int) {
    }

    override fun onLocationChange(p0: BNaviLocation?) {
    }

    override fun onMapStateChange(p0: IBNaviListener.MapStateMode?) {
    }

    override fun onStartYawing(p0: String?) {
    }

    override fun onYawingSuccess() {
    }

    override fun onYawingArriveViaPoint(p0: Int) {
    }

    override fun onNotificationShow(p0: String?) {
    }

    override fun onHeavyTraffic() {
    }

    override fun onNaviGuideEnd() {
    }
}