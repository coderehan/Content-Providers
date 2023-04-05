package com.cipl.contentproviders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cipl.contentproviders.databinding.AdapterContactListBinding

class AdapterContactList(private val list:List<String>) : RecyclerView.Adapter<AdapterContactList.MyViewHolder>() {

    inner class MyViewHolder(val binding: AdapterContactListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterContactList.MyViewHolder {
        val binding = AdapterContactListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterContactList.MyViewHolder, position: Int) {
        val binding = holder.binding
        binding.tvName.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}