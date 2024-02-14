package com.example.myquiz.modle

 class HistoryModelClass{
     var timaAndDate:String = ""
     var coin:String = ""
     var isWithdrawal:Boolean=false
     constructor()

     constructor(timaAndDate: String, coin: String, isWithdrawal: Boolean) {
         this.timaAndDate = timaAndDate
         this.coin = coin
         this.isWithdrawal = isWithdrawal
     }

 }
