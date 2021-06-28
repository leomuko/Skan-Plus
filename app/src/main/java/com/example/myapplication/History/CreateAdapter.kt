package com.example.myapplication.History

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BarcodeResultActivity
import com.example.myapplication.CreateBarcodeActivity
import com.example.myapplication.R
import com.example.myapplication.data.ScannerDataModel
import kotlinx.android.synthetic.main.activity_create_barcode.view.*
import kotlinx.android.synthetic.main.item_history.view.*


class CreateAdapter(context: Context) : RecyclerView.Adapter<CreateAdapter.CreateViewHolder>() {

    private var theList = emptyList<ScannerDataModel>()
    private val mContext = context


    class CreateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateViewHolder {
        return CreateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent,false))
    }

    override fun onBindViewHolder(holder: CreateViewHolder, position: Int) {
        val currentItem = theList[position]
        holder.itemView.item_result.text = currentItem.result
        holder.itemView.item_type.text = currentItem.type
        holder.itemView.item_ImageView.background = ContextCompat.getDrawable(mContext, R.drawable.ic_qr_code)

        holder.itemView.setOnClickListener {
            goToCreateBarcodeActivity(currentItem)
        }
    }

    private fun goToCreateBarcodeActivity(currentItem: ScannerDataModel) {
        val intent = Intent(mContext, CreateBarcodeActivity::class.java)
        intent.putExtra("DATA", currentItem)
        mContext.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return  theList.size
    }

    fun setData(d : List<ScannerDataModel>){
        this.theList = d

        notifyDataSetChanged()
    }
}