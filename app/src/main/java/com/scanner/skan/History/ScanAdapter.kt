package com.scanner.skan.History

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.scanner.skan.BarcodeResultActivity
import com.scanner.skan.R
import com.scanner.skan.data.ScannerDataModel
import kotlinx.android.synthetic.main.item_history.view.*


class ScanAdapter(context: Context) : RecyclerView.Adapter<ScanAdapter.MyViewHolder>(){

    private var theList = emptyList<ScannerDataModel>()
    private val mContext = context

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent,false))
    }

    override fun onBindViewHolder(holder: ScanAdapter.MyViewHolder, position: Int) {
        val currentItem = theList[position]
        holder.itemView.item_result.text = currentItem.result
        holder.itemView.item_type.text = currentItem.type
        if (currentItem.type == mContext.getString(R.string.website)){
            holder.itemView.item_ImageView.background = ContextCompat.getDrawable(mContext, R.drawable.ic_globe)
        }else if (currentItem.type == mContext.getString(R.string.contact)){
            holder.itemView.item_ImageView.background = ContextCompat.getDrawable(mContext, R.drawable.ic_contact)
        }else if (currentItem.type == mContext.getString(R.string.product)){
            holder.itemView.item_ImageView.background = ContextCompat.getDrawable(mContext, R.drawable.ic_product)
        }else{
            holder.itemView.item_ImageView.background = ContextCompat.getDrawable(mContext, R.drawable.ic_text)
        }
        holder.itemView.setOnClickListener {
            goToResultsActivity(currentItem)
        }
    }

    private fun goToResultsActivity(currentItem: ScannerDataModel) {
        val intent = Intent(mContext, BarcodeResultActivity::class.java)
        intent.putExtra("DATA", currentItem)
        mContext.startActivity(intent)
    }

    fun setData(d : List<ScannerDataModel>){
        this.theList = d

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return theList.size
    }
}