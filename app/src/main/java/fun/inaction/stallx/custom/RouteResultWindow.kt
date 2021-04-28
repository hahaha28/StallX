package `fun`.inaction.stallx.custom

import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.RoutePlanAdapter
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.navisdk.adapter.BNaviCommonParams
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory
import razerdp.basepopup.BasePopupWindow

class RouteResultWindow(context: Context?) : BasePopupWindow(context) {

    private lateinit var routeResult:MutableList<RoutePlan>
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter :RoutePlanAdapter

    init {

    }

    override fun onCreateContentView(): View {
        routeResult = mutableListOf()
        adapter = RoutePlanAdapter()

        val routePlanInfo = BaiduNaviManagerFactory.getRouteResultManager().routePlanInfo

        routePlanInfo.routeTabInfos.forEach {
            routeResult.add(RoutePlan(it.pusLabelName,it.passTime,it.length,it.lights))
        }

        val view = createPopupById(R.layout.window_route_result)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context,routeResult.size)

        adapter.setNewInstance(routeResult)
        adapter.setOnItemClickListener { adapter, view, position ->
            this.adapter.selectIndex = position
        }

        setOutSideDismiss(false)
        setOutSideTouchable(true)
        setBackPressEnable(false)
        setBackgroundColor(Color.parseColor("#00000000"))

        return view
    }

    data class RoutePlan(
        var name:String,
        var time:Double,
        var distance:Double,
        var lights:Int
    )

}