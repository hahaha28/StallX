package `fun`.inaction.stallx.nav

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory

class CarRouteResultFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        BaiduNaviManagerFactory.getRouteResultManager().onCreate(activity)
        BaiduNaviManagerFactory.getRouteResultSettingManager().setRouteMargin(
            100, 100, 100, 500
        )
        BaiduNaviManagerFactory.getRouteResultManager().setRouteClickedListener { index ->

        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        BaiduNaviManagerFactory.getMapManager().onResume()
        BaiduNaviManagerFactory.getRouteResultManager().onResume()
    }

    override fun onPause() {
        super.onPause()
        BaiduNaviManagerFactory.getMapManager().onPause()
        BaiduNaviManagerFactory.getRouteResultManager().onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        BaiduNaviManagerFactory.getRouteResultManager().onDestroy()
    }

}