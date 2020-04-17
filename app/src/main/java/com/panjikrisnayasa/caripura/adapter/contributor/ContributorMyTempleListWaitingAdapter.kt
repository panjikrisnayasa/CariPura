package com.panjikrisnayasa.caripura.adapter.contributor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.Temple
import com.panjikrisnayasa.caripura.view.contributor.MyTempleDetailWaitingActivity

class ContributorMyTempleListWaitingAdapter(private var mTempleList: ArrayList<Temple>) :
    RecyclerView.Adapter<ContributorMyTempleListWaitingAdapter.ContributorMyTempleListWaitingHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContributorMyTempleListWaitingHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_temple_list, parent, false)
        return ContributorMyTempleListWaitingHolder(view)
    }

    override fun getItemCount(): Int {
        return mTempleList.size
    }

    override fun onBindViewHolder(holder: ContributorMyTempleListWaitingHolder, position: Int) {
        val temple = mTempleList[position]

        holder.mImage.clipToOutline = true
        Glide.with(holder.itemView.context).load(temple.photo).into(holder.mImage)
        holder.mTextName.text = temple.name
        holder.mTextVillageOffice.text = temple.villageOffice
        holder.mTextDistance.text = temple.distance
        holder.mTextFullMoonPrayerStart.text = temple.fullMoonPrayerStart
        holder.mTextFullMoonPrayerEnd.text = temple.fullMoonPrayerEnd
        holder.mTextDeadMoonPrayerStart.text = temple.deadMoonPrayerStart
        holder.mTextDeadMoonPrayerEnd.text = temple.deadMoonPrayerEnd

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MyTempleDetailWaitingActivity::class.java)
            it.context.startActivity(intent)
        }
    }

    inner class ContributorMyTempleListWaitingHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mImage: ImageView = itemView.findViewById(R.id.image_item_temple_list)
        var mTextName: TextView = itemView.findViewById(R.id.text_item_temple_list_temple_name)
        var mTextVillageOffice: TextView =
            itemView.findViewById(R.id.text_item_temple_list_temple_village_office)
        var mTextDistance: TextView =
            itemView.findViewById(R.id.text_item_temple_list_temple_distance)
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