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
import com.panjikrisnayasa.caripura.view.MyTempleDetailWaitingActivity

class MyTempleListWaitingAdapter :
    RecyclerView.Adapter<MyTempleListWaitingAdapter.MyTempleListWaitingViewHolder>() {

    private var mTempleList = arrayListOf<Temple>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyTempleListWaitingViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_temple_list_with_label, parent, false)
        return MyTempleListWaitingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mTempleList.size
    }

    override fun onBindViewHolder(holder: MyTempleListWaitingViewHolder, position: Int) {
        val temple = mTempleList[position]

        holder.mImage.clipToOutline = true
        Glide.with(holder.itemView.context).load(temple.photo).into(holder.mImage)
        holder.mTextName.text = temple.name
        holder.mTextVillageOffice.text = temple.villageOffice
        holder.mTextFullMoonPrayerStart.text = temple.fullMoonPrayerStart
        holder.mTextFullMoonPrayerEnd.text = temple.fullMoonPrayerEnd
        holder.mTextDeadMoonPrayerStart.text = temple.deadMoonPrayerStart
        holder.mTextDeadMoonPrayerEnd.text = temple.deadMoonPrayerEnd
        when (temple.requestType) {
            "add" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.item_label_request_add_temple)
                holder.mTextLabel.setBackgroundResource(R.color.colorGreen)
            }
            "edit" -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.item_label_request_edit_temple)
                holder.mTextLabel.setBackgroundResource(R.color.colorOrange)
            }
            else -> {
                holder.mTextLabel.text =
                    holder.itemView.context.resources?.getString(R.string.item_label_request_delete_temple)
                holder.mTextLabel.setBackgroundResource(R.color.colorRed)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MyTempleDetailWaitingActivity::class.java)
            intent.putExtra(MyTempleDetailWaitingActivity.EXTRA_TEMPLE, temple)
            it.context.startActivity(intent)
        }
    }

    fun setData(templeList: ArrayList<Temple>) {
        mTempleList.clear()
        mTempleList.addAll(templeList)
        notifyDataSetChanged()
    }

    inner class MyTempleListWaitingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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