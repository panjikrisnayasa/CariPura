package com.panjikrisnayasa.caripura.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.view.MyTempleDetailActivity


class MyTempleListAdapter(private val fragment: Fragment?) :
    RecyclerView.Adapter<MyTempleListAdapter.MyTempleListViewHolder>() {

    private var mTempleList = arrayListOf<Temple>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTempleListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_temple_list, parent, false)
        return MyTempleListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mTempleList.size
    }

    override fun onBindViewHolder(holder: MyTempleListViewHolder, position: Int) {
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
            val intent = Intent(it.context, MyTempleDetailActivity::class.java)
            intent.putExtra(MyTempleDetailActivity.EXTRA_POSITION, position)
            if (fragment != null) {
                intent.putExtra(MyTempleDetailActivity.EXTRA_TEMPLE, temple)
                fragment.startActivityForResult(
                    intent,
                    MyTempleDetailActivity.REQUEST_UPDATE_CONTRIBUTOR
                )
            } else {
                intent.putExtra(MyTempleDetailActivity.EXTRA_TEMPLE_ID, temple.id)
                (it.context as Activity).startActivityForResult(
                    intent,
                    MyTempleDetailActivity.REQUEST_UPDATE_ADMIN
                )
            }
        }
    }

    fun setData(templeList: ArrayList<Temple>) {
        mTempleList.clear()
        mTempleList.addAll(templeList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mTempleList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mTempleList.size)
    }

    inner class MyTempleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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