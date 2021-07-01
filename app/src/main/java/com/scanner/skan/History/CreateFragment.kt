package com.scanner.skan.History

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scanner.skan.R
import com.scanner.skan.data.ScanViewModel
import com.scanner.skan.data.ScannerDataModel
import kotlinx.android.synthetic.main.fragment_create.view.*

class CreateFragment : Fragment() {

    private lateinit var mScanViewModel : ScanViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        //RecyclerView
        val adapter = CreateAdapter(requireContext())
        val recyclerView = view.create_recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //ScanViewModel
        mScanViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        mScanViewModel.readAllData.observe(requireActivity(), Observer {
            val theList = ArrayList<ScannerDataModel>()
            for (i in it){
                if(!i.scan){
                    theList.add(i)
                }
            }
            adapter.setData(theList.toList())
        })

        return view
    }


}