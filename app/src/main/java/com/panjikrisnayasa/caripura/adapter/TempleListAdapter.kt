package com.panjikrisnayasa.caripura.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.view.TempleDetailActivity
import java.util.*

class TempleListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<TempleListAdapter.TempleListViewHolder>() {

    private var mTempleList = arrayListOf<Temple>()
    private var mTempTempleList = arrayListOf<Temple>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempleListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_temple_list, parent, false)
        return TempleListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mTempleList.size
    }

    override fun onBindViewHolder(holder: TempleListViewHolder, position: Int) {
        val temple = mTempleList[position]

        holder.mImage.clipToOutline = true
        Glide.with(holder.itemView.context).load(temple.photo).into(holder.mImage)
        holder.mTextName.text = temple.name
        holder.mTextVillageOffice.text = temple.villageOffice
        holder.mTextFullMoonPrayerStart.text = temple.fullMoonPrayerStart
        holder.mTextFullMoonPrayerEnd.text = temple.fullMoonPrayerEnd
        holder.mTextDeadMoonPrayerStart.text = temple.deadMoonPrayerStart
        holder.mTextDeadMoonPrayerEnd.text = temple.deadMoonPrayerEnd

        holder.itemView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    mContext,
                    mContext.getString(R.string.toast_message_location_permission),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                moveToDetail(it, temple)
            }
        }
    }

    fun setData(templeList: ArrayList<Temple>) {
        mTempleList.clear()
        mTempleList.addAll(templeList)
        mTempTempleList.addAll(templeList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        mTempleList.clear()
        if (query.isEmpty()) {
            mTempleList.addAll(mTempTempleList)
        } else {
            for (temple in mTempTempleList) {
                if (temple.name.trim().toLowerCase(Locale.getDefault())
                        .contains(query) || temple.villageOffice.trim()
                        .toLowerCase(Locale.getDefault()).contains(query)
                ) {
                    mTempleList.add(temple)
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun moveToDetail(view: View, temple: Temple) {
        val viewContext = view.context
        val intent = Intent(viewContext, TempleDetailActivity::class.java)
        intent.putExtra(TempleDetailActivity.EXTRA_TEMPLE, temple)
        viewContext.startActivity(intent)
    }

    inner class TempleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImage: ImageView = itemView.findViewById(R.id.image_item_temple_list)
        var mTextName: TextView = itemView.findViewById(R.id.text_item_temple_list_temple_name)
        var mTextVillageOffice: TextView =
            itemView.findViewById(R.id.text_item_temple_list_temple_village_office)
        var mTextFullMoonPrayerStart: TextView =
            itemView.findViewById(R.id.text_item_temple_list_full_moon_prayer_start)
        var mTextFullMoonPrayerEnd: TextView =
            itemView.findViewById(R.id.text_item_temple_list_full_moon_prayer_end)
        var mTextDeadMoonPrayerStart: TextView =
            itemView.findViewById(R.id.text_item_temple_list_dead_moon_prayer_start)
        var mTextDeadMoonPrayerEnd: TextView =
            itemView.findViewById(R.id.text_item_temple_list_dead_moon_prayer_end)
    }
}