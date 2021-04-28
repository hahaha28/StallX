package `fun`.inaction.stallx.mark

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Mark
import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.service.MarkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.MarkHistoryAdapter
import `fun`.inaction.stallx.databinding.FragmentAddMarkBinding
import `fun`.inaction.stallx.databinding.FragmentMarkListBinding
import `fun`.inaction.stallx.mark.viewmodel.MarkListViewModel
import `fun`.inaction.stallx.utils.LocationHelper
import `fun`.inaction.stallx.utils.NavUtil
import `fun`.inaction.stallx.utils.logi
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mapapi.model.LatLng
import com.hi.dhl.binding.viewbind
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MarkListFragment:Fragment(R.layout.fragment_mark_list) {

    private val TAG = "MarkListFragment"

    private val viewModel by viewModels<MarkListViewModel>()

    private val binding by viewbind<FragmentMarkListBinding>()
    private val adapter = MarkHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        LiveEventBus.get("NewMark",Mark::class.java)
            .observeForever {
                viewModel.addMark(it)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        // 设置RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getMarkHistory()
        }
        // recyclerView 点击事件
        adapter.setOnItemClickListener { adapter, view, position ->
            LocationHelper.curLocation?.let {
                WaitDialog.show(requireActivity() as AppCompatActivity,"")
                val targetMark = adapter.data[position] as Mark
                logi(TAG,"lo,la(${it.longitude},${it.latitude})|to lo,la(${targetMark.longitude},${targetMark.latitude})")
                NavUtil.startWalkNav(requireActivity(),
                    LatLng(it.latitude,it.longitude),
                    LatLng(targetMark.latitude,targetMark.longitude),
                    {
                        WaitDialog.dismiss()
                    },
                    {
                        WaitDialog.dismiss()
                        TipDialog.show(requireActivity() as AppCompatActivity,it,TipDialog.TYPE.ERROR)
                            .setTipTime(2000)
                    }
                )
            }
        }
        // 监听列表数据
        viewModel.markHistory.observe(viewLifecycleOwner){
            adapter.setNewInstance(it)
            binding.refreshLayout.finishRefresh()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_add -> {
                findNavController().navigate(R.id.action_markListFragment_to_addMarkFragment)
            }
        }
        return true
    }
}