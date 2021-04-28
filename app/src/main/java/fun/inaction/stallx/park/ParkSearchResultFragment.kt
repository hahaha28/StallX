package `fun`.inaction.stallx.park

import `fun`.inaction.custom.view.ProgressView
import `fun`.inaction.network.bean.Park
import `fun`.inaction.stallx.R
import `fun`.inaction.stallx.adapters.ParkListAdapter
import `fun`.inaction.stallx.databinding.FragmentParkAddBinding
import `fun`.inaction.stallx.databinding.FragmentParkSearchResultBinding
import `fun`.inaction.stallx.park.viewmodel.ParkSearchResultViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dhl.binding.viewbind

class ParkSearchResultFragment: Fragment(R.layout.fragment_park_search_result) {

    private val binding by viewbind<FragmentParkSearchResultBinding>()

    private val viewModel:ParkSearchResultViewModel by viewModels()

    private val args :ParkSearchResultFragmentArgs by navArgs()

    private lateinit var adapter:ParkListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchNearParks(args.longitude.toDouble(),args.latitude.toDouble(),args.city)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ParkListAdapter(args.longitude.toDouble(),args.latitude.toDouble())

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.setEmptyView(ProgressView(requireContext()))

        viewModel.parkList.observe(viewLifecycleOwner){
            adapter.setNewInstance(it)
        }

        // item 点击事件
        adapter.setOnItemClickListener { adapter, _, position ->
            val park = adapter.data[position] as Park
            findNavController().navigate(ParkSearchResultFragmentDirections.actionParkSearchResultFragmentToParkDetailFragment(
                park,args.longitude,args.latitude
            ))
        }

    }
}