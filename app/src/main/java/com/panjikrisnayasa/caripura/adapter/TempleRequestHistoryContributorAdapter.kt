package com.panjikrisnayasa.caripura.adapter

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
import com.panjikrisnayasa.caripura.view.TempleRequestHistoryDetailContributorActivity

class TempleRequestHistoryContributorAdapter :
    RecyclerView.Adapter<TempleRequestHistoryContributorAdapter.TempleRequestHistoryContributorViewHolder>() {

    private var mTempleList = arrayListOf<Temple>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TempleRequestHistoryContributorViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_temple_list_with_label, parent, false)
        return TempleRequestHistoryContributorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mTempleList.size
    }

    override fun onBindViewHolder(
        holder: TempleRequestHistoryContributorViewHolder,
        position: Int
    ) {
        val temple = mTempleList[position]

        holder.mImage.clipToOutline = true
        Glide.with(holder.itemView.context).load(temple.photo).into(holder.mImage)
        holder.mTextName.text = temple.name
        holder.mTextVillageOffice.text = temple.villageOffice
        holder.mTextFullMoonPrayerStart.text = temple.fullMoonPrayerStart
        holder.mTextFullMoonPrayerEnd.text = temple.fullMoonPrayerEnd
        holder.mTextDeadMoonPrayerStart.text = temple.deadMoonPrayerStart
        holder.mTextDeadMoonPrayerEnd.text = temple.deadMoonPrayerEnd
        when (temple.requestStatus) {
            "accepted" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.item_label_accepted)
                holder.mTextLabel.setBackgroundResource(R.color.colorGreen)
            }
            "rejected" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.item_label_rejected)
                holder.mTextLabel.setBackgroundResource(R.color.colorRed)
            }
            "deleted_by_admin" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.temple_request_detail_text_label_deleted_by_admin)
                holder.mTextLabel.setBackgroundResource(R.color.colorOrange)
            }
            "edited_by_admin" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.temple_request_detail_text_label_edited_by_admin)
                holder.mTextLabel.setBackgroundResource(R.color.colorOrange)
            }
        }

        holder.itemView.setOnClickListener {
            val intent =
                Intent(it.context, TempleRequestHistoryDetailContributorActivity::class.java)
            intent.putExtra(TempleRequestHistoryDetailContributorActivity.EXTRA_TEMPLE, temple)
            it.context.startActivity(intent)
        }
    }

    fun setData(templeList: ArrayList<Temple>) {
        mTempleList.clear()
        mTempleList.addAll(templeList)
        notifyDataSetChanged()
    }

    inner class TempleRequestHistoryContributorViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mImage: ImageView = itemView.findViewById(R.id.image_item_temple_list_with_label)
        var mTextName: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_temple_name)
        var mTextVillageOffice: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_temple_village_office)
        var mTextFullMoonPrayerStart: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_full_moon_prayer_start)
        var mTextFullMoonPrayerEnd: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_full_moon_prayer_end)
        var mTextDeadMoonPrayerStart: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_dead_moon_prayer_start)
        var mTextDeadMoonPrayerEnd: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_dead_moon_prayer_end)
        var mTextLabel: TextView =
            itemView.findViewById(R.id.text_item_temple_list_with_label_label)
    }
}