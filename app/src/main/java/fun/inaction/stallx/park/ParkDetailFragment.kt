package `fun`.inaction.stallx.park

import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.call
import `fun`.inaction.network.service.CollectionService
import `fun`.inaction.network.service.HistoryService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.CommentListAdapter
import `fun`.inaction.stallx.databinding.FragmentParkDetailBinding
import `fun`.inaction.stallx.utils.LocationHelper
import `fun`.inaction.stallx.utils.NavUtil
import `fun`.inaction.stallx.utils.load
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.hi.dhl.binding.viewbind

class ParkDetailFragment : Fragment(R.layout.fragment_park_detail) {

    private val args: ParkDetailFragmentArgs by navArgs()

    private val binding by viewbind<FragmentParkDetailBinding>()

    private val adapter = CommentListAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val park = args.park

        // 设置基本信息
        binding.backgroundImageView.load("${ServiceCreator.BASE_URL}${park.imgUrl}")
        binding.parkName.text = park.name
        LocationHelper.curLocation?.let {
            val dis = DistanceUtil.getDistance(
                LatLng(it.latitude, it.longitude),
                LatLng(park.latitude, park.longitude)
            ) / 1000
            binding.distanceDesc.text = "距当前${String.format("%.2f", dis)}km"
        }
        val dis = DistanceUtil.getDistance(
            LatLng(park.latitude, park.longitude),
            LatLng(args.targetLatitude.toDouble(), args.targetLongitude.toDouble())
        ) / 1000
        binding.targetDistanceDesc.text = "距目的地${String.format("%.2f", dis)}km"
        binding.totalStallNum.text = if (park.totalStallNum == 0) {
            "未知"
        } else {
            "${park.totalStallNum}"
        }
        binding.curStallNum.text = if (park.curStallNum == 0) {
            "未知"
        } else {
            "${park.curStallNum}"
        }
        binding.chargeRules.text = if (park.isCharged == 0) {
            "未知"
        } else {
            park.chargingRules
        }

        // 设置评论信息

        binding.commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.commentRecyclerView.adapter = adapter
        adapter.setNewInstance(park.comments)

        // 导航按钮点击事件
        binding.navBtn.setOnClickListener {
            ServiceCreator.create<HistoryService>().addHistory(args.park._id!!,args.park.name).call {  }
            LocationHelper.curLocation?.let {
                NavUtil.startCarNav(
                    requireActivity(),
                    LatLng(it.latitude, it.longitude),
                    LatLng(args.targetLatitude.toDouble(), args.targetLongitude.toDouble())
                )
            }
        }


        // 收藏按钮相关
        setIsCollected()
    }

    // 设置收藏相关
    private fun setIsCollected(){
        val collectionService = ServiceCreator.create<CollectionService>()
        collectionService.isCollected(args.park._id!!).call {
            binding.collectBtn.isChecked = it.isCollected
        }

        binding.collectBtn.setOnCheckStateChangeListener { _, checked ->
            if(checked){
                // 添加收藏
                collectionService.addCollection(args.park._id!!).call {  }
            }else{
                // 取消收藏
                collectionService.deleteCollectionIndirect(args.park._id!!).call {}
            }
        }
    }
}