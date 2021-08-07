package com.scanner.skan.adapters

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scanner.skan.History.HistoryActivity
import com.scanner.skan.R
import com.scanner.skan.Settings.SettingsActivity
import com.scanner.skan.barcode.BarCodeActivity
import com.scanner.skan.barcode.CreateBarcodeActivity
import com.scanner.skan.models.MenuModel

class MenuRecyclerAdapter(_context: Context, _dashBoardList: List<MenuModel>) :
    RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>() {

    val context: Context = _context;
    val dashBoardList: List<MenuModel> = _dashBoardList;

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.item_main_text)
        var menuImg: ImageView = itemView.findViewById(R.id.item_menu_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)

        return MenuViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val theItem: MenuModel = dashBoardList[position];
        holder.tvTitle.text = theItem.title
        Glide.with(context).load(theItem.image).into(holder.menuImg)

        holder.itemView.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.equals(theItem.title, "Scan")){
                    val intent = Intent(context, BarCodeActivity::class.java )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }else if(TextUtils.equals(theItem.title, "Create")){
                    val intent = Intent(context, CreateBarcodeActivity::class.java )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }else if (TextUtils.equals(theItem.title, "History")){
                    val intent = Intent(context, HistoryActivity::class.java )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }else if(TextUtils.equals(theItem.title, "Settings")){
                    val intent = Intent(context, SettingsActivity::class.java )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            }

        })
    }

    override fun getItemCount(): Int {
       return dashBoardList.size
    }
}