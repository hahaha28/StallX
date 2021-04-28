package `fun`.inaction.stallx.history

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.GetParkResult
import `fun`.inaction.network.bean.History
import `fun`.inaction.network.call
import `fun`.inaction.network.service.HistoryService
import `fun`.inaction.network.service.ParkService
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.ParkHistoryAdapter
import `fun`.inaction.stallx.databinding.FragmentHistoryBinding
import `fun`.inaction.stallx.utils.LocationHelper
import `fun`.inaction.stallx.utils.appCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dhl.binding.viewbind
import com.kongzue.dialog.v3.WaitDialog

class ParkHistoryFragment:Fragment(R.layout.fragment_history) {

    private val binding by viewbind<FragmentHistoryBinding>()
    private val adapter = ParkHistoryAdapter()

    private val service = ServiceCreator.create<HistoryService>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.setOnItemChildClickListener { adapter, view, position ->
            val data = adapter.data[position] as History
            when(view.id){
                R.id.commentBtn -> {
                    findNavController().navigate(ParkHistoryFragmentDirections.actionParkHistoryFragmentToParkCommentFragment(
                        data.parkID
                    ))
                }
            }
        }

        adapter.setOnItemClickListener { adapter, view, position ->

            WaitDialog.show(appCompatActivity(),"请稍后...")
            val parkHistory = adapter.data[position] as History
            ServiceCreator.create<ParkService>().getPark(parkHistory.parkID).enqueue(object :
                OkCallback<GetParkResult>() {
                override fun onSuccess(result: GetParkResult) {
                    super.onSuccess(result)
                    val park = result.parkData
                    LocationHelper.curLocation?.let {
                        findNavController().navigate(ParkHistoryFragmentDirections.actionParkHistoryFragmentToParkDetailFragment(
                            park,it.longitude.toString(),it.latitude.toString()
                        ))
                    }

                }

                override fun onFinally() {
                    super.onFinally()
                    WaitDialog.dismiss()
                }
            })


        }

        requestHistoryData()

    }

    private fun requestHistoryData(){
        service.getHistory().call {
            adapter.setNewInstance(it.historyData)
        }
    }

}