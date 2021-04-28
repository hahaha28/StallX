package `fun`.inaction.stallx.collection

import `fun`.inaction.custom.view.ProgressView
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Collection
import `fun`.inaction.network.call
import `fun`.inaction.network.service.ParkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.CollectionAdapter
import `fun`.inaction.stallx.collection.viewmodel.CollectionListViewModel
import `fun`.inaction.stallx.databinding.FragmentCollectionListBinding
import `fun`.inaction.stallx.utils.LocationHelper
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dhl.binding.viewbind

class CollectionListFragment:Fragment(R.layout.fragment_collection_list) {

    private val adapter = CollectionAdapter()
    private val viewModel by viewModels<CollectionListViewModel>()
    private val binding by viewbind<FragmentCollectionListBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置recyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.setEmptyView(ProgressView(requireContext()))

        // 监听列表数据
        viewModel.dataList.observe(viewLifecycleOwner){
            adapter.setNewInstance(it)
        }

        // 取消收藏点击事件
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.collectBtn -> {
                    val co = adapter.data[position] as Collection
                    // 取消收藏
                    viewModel.cancelCollected(co._id)
                    adapter.removeAt(position)
                }
            }
        }

        // 列表子项点击事件
        adapter.setOnItemClickListener { adapter, view, position ->
            val c = adapter.data[position] as Collection
            LocationHelper.curLocation?.let { location ->
                ServiceCreator.create<ParkService>().getPark(c.parkID).call {
                    findNavController().navigate(CollectionListFragmentDirections.actionCollectionListFragmentToParkDetailFragment(
                        it.parkData,location.longitude.toString(),location.latitude.toString()
                    ))
                }
            }

        }

    }
}