package com.example.myquiz.adaptor

import android.os.Build
import android.os.health.TimerStat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.databinding.HistoryitemBinding
import com.example.myquiz.modle.HistoryModelClass
import com.google.type.DateTime
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class HistoryAdaptor(var ListHistory:ArrayList<HistoryModelClass>)
    : RecyclerView.Adapter<HistoryAdaptor.HistoryCoinViewHolder>() {
    class HistoryCoinViewHolder(var binding: HistoryitemBinding)
          :RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HistoryCoinViewHolder {
     return HistoryCoinViewHolder(HistoryitemBinding
         .inflate(LayoutInflater.from(parent.context),parent,false)) }

    override fun getItemCount() = ListHistory.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryCoinViewHolder, position: Int) {

        //convert date and time in actual time, note timestamp is in Long
        var timeStamp = Timestamp(ListHistory.get(position).timaAndDate.toLong())
        holder.binding.Time.text= Date(timeStamp.time).toString()


        //for credit or withdrawal coins
        holder.binding.coinStatus.text = if(ListHistory.get(position).isWithdrawal)
        {"- Withdrawal"}
        else{"+ Credit"}

        //showing coins
        holder.binding.Coin.text=ListHistory[position].coin
    }
}