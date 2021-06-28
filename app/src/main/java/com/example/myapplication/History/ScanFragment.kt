package com.example.myapplication.History

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.ScanViewModel
import com.example.myapplication.data.ScannerDataModel
import kotlinx.android.synthetic.main.fragment_scan.view.*


class ScanFragment : Fragment() {


   private lateinit var mScanViewModel : ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_scan, container, false)

        //RecyclerView
        val adapter = ScanAdapter(requireContext())
        val recyclerView = view.scan_recycler_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //ScanViewModel
        mScanViewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        mScanViewModel.readAllData.observe(requireActivity(), Observer {
            val theList = ArrayList<ScannerDataModel>()
            for (i in it){
                if(i.scan){
                    theList.add(i)
                }
            }
            adapter.setData(theList.toList())
        })

        return view
    }


}