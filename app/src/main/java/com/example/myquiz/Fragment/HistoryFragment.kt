package com.example.myquiz.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myquiz.adaptor.HistoryAdaptor
import com.example.myquiz.databinding.FragmentHistoryBinding
import com.example.myquiz.modle.HistoryModelClass
import com.example.myquiz.modle.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.Collections

class HistoryFragment : Fragment() {
    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }
    lateinit var adaptor: HistoryAdaptor
    private var ListHistory = ArrayList<HistoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       //Here we add actual data that we get from our database
        Firebase.database.reference.child("playerCoinHistory")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListHistory.clear()//clear previous data, and when new data comes it accept as a new
                    var ListHistory1 = ArrayList<HistoryModelClass>()
                    for (datasnapshot in snapshot.children) {
                        var data = datasnapshot.getValue(HistoryModelClass::class.java)
                        ListHistory1.add(data!!)
                    }
                    Collections.reverse(ListHistory1)//showing latest transaction above in our screen
                    ListHistory.addAll(ListHistory1)
                    adaptor.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //coins button functionality stopped now
            /*binding.CoinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
                                                        }
            binding.CoinWithdrawal1.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition }*/

        //setting layout for history
        binding.HistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adaptor = HistoryAdaptor(ListHistory)
        binding.HistoryRecyclerView.adapter = adaptor//setting the adapter
        binding.HistoryRecyclerView.setHasFixedSize(true)

        //showing name of user on the screen
        Firebase.database.reference.child("Users")
            .child(Firebase.auth.currentUser?.uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<User>()
                        binding.Name.text = user?.name
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        ////showing coins on the screen
        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var currentCoin = snapshot.getValue() as Long
                        binding.CoinWithdrawal.text = currentCoin.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })


        return binding.root//return view
    }

    //no need
    companion object {
    }

}