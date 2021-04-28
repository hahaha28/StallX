package `fun`.inaction.stallx.nav

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baidu.navisdk.adapter.BNaviCommonParams
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory
import com.baidu.navisdk.adapter.IBNaviListener
import com.baidu.navisdk.adapter.struct.*
import com.baidu.navisdk.ui.routeguide.model.RGLineItem

class CarNavFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = Bundle()
        bundle.putBoolean(BNaviCommonParams.ProGuideKey.ADD_MAP, false)

        val view = BaiduNaviManagerFactory.getRouteGuideManager().onCreate(
            activity,
            BNGuideConfig.Builder()

                .params(bundle)
                .build()
        )
        view.fitsSystemWindows = true
        return view
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        BaiduNaviManagerFactory.getRouteGuideManager().onConfigurationChanged(newConfig)
    }


    override fun onStart() {
        super.onStart()
        BaiduNaviManagerFactory.getRouteGuideManager().onStart()
    }

    override fun onResume() {
        super.onResume()
        BaiduNaviManagerFactory.getRouteGuideManager().onResume()
    }

    override fun onPause() {
        super.onPause()
        BaiduNaviManagerFactory.getRouteGuideManager().onPause()
    }

    override fun onStop() {
        super.onStop()
        BaiduNaviManagerFactory.getRouteGuideManager().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        BaiduNaviManagerFactory.getRouteGuideManager().onDestroy(false)
        BaiduNaviManagerFactory.getRouteGuideManager().setNaviListener(object : IBNaviListener {
            override fun onRoadNameUpdate(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onRemainInfoUpdate(p0: Int, p1: Int) {
                TODO("Not yet implemented")
            }

            override fun onViaListRemainInfoUpdate(p0: Message?) {
                TODO("Not yet implemented")
            }

            override fun onGuideInfoUpdate(p0: BNaviInfo?) {
                TODO("Not yet implemented")
            }

            override fun onHighWayInfoUpdate(p0: IBNaviListener.Action?, p1: BNHighwayInfo?) {
                TODO("Not yet implemented")
            }

            override fun onFastExitWayInfoUpdate(
                p0: IBNaviListener.Action?,
                p1: String?,
                p2: Int,
                p3: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onEnlargeMapUpdate(
                p0: IBNaviListener.Action?,
                p1: View?,
                p2: String?,
                p3: Int,
                p4: String?,
                p5: Bitmap?
            ) {
                TODO("Not yet implemented")
            }

            override fun onDayNightChanged(p0: IBNaviListener.DayNightMode?) {
                TODO("Not yet implemented")
            }

            override fun onRoadConditionInfoUpdate(p0: Double, p1: MutableList<BNRoadCondition>?) {
                TODO("Not yet implemented")
            }

            override fun onMainSideBridgeUpdate(p0: Int) {
                TODO("Not yet implemented")
            }

            override fun onLaneInfoUpdate(
                p0: IBNaviListener.Action?,
                p1: MutableList<RGLineItem>?
            ) {
                TODO("Not yet implemented")
            }

            override fun onSpeedUpdate(p0: String?, p1: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onArriveDestination() {
                TODO("Not yet implemented")
            }

            override fun onArrivedWayPoint(p0: Int) {
                TODO("Not yet implemented")
            }

            override fun onLocationChange(p0: BNaviLocation?) {
                TODO("Not yet implemented")
            }

            override fun onMapStateChange(p0: IBNaviListener.MapStateMode?) {
                TODO("Not yet implemented")
            }

            override fun onStartYawing(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onYawingSuccess() {
                TODO("Not yet implemented")
            }

            override fun onYawingArriveViaPoint(p0: Int) {
                TODO("Not yet implemented")
            }

            override fun onNotificationShow(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onHeavyTraffic() {
                TODO("Not yet implemented")
            }

            override fun onNaviGuideEnd() {
                
            }
        })
    }
}