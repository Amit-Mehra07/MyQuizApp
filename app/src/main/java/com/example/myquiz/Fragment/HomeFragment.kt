package com.example.myquiz.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myquiz.R
import com.example.myquiz.adaptor.categoryadaptor
import com.example.myquiz.databinding.FragmentHomeBinding
import com.example.myquiz.modle.User
import com.example.myquiz.modle.categoryModelClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private var categoryList = ArrayList<categoryModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryList.add(categoryModelClass(R.drawable.html, "HTML"))
        categoryList.add(categoryModelClass(R.drawable.css, "CSS"))
        categoryList.add(categoryModelClass(R.drawable.javascript, "Java Script"))
        categoryList.add(categoryModelClass(R.drawable.python, "Python"))
        categoryList.add(categoryModelClass(R.drawable.clang, "C"))
        categoryList.add(categoryModelClass(R.drawable.c, "C++"))
        categoryList.add(categoryModelClass(R.drawable.j, "Java"))
        categoryList.add(categoryModelClass(R.drawable.kotlin, "Kotlin"))
        categoryList.add(categoryModelClass(R.drawable.android, "Android"))
        categoryList.add(categoryModelClass(R.drawable.software, "Software"))
        categoryList.add(categoryModelClass(R.drawable.golang, "Go"))
        categoryList.add(categoryModelClass(R.drawable.computernetwork, "Network"))
        categoryList.add(categoryModelClass(R.drawable.sql, "SQL"))
        categoryList.add(categoryModelClass(R.drawable.dart, "Dart"))
        categoryList.add(categoryModelClass(R.drawable.flutterlang, "Flutter"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //will implement in further updates
        /* binding.CoinWithdrawal.setOnClickListener {
             val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
             bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
             bottomSheetDialog.enterTransition
         }
         binding.CoinWithdrawal1.setOnClickListener {
             val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
             bottomSheetDialog.show(requireActivity().supportFragmentManager, "TEST")
             bottomSheetDialog.enterTransition
         }*/


        //setting coins on the screen
        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var currentCoins = snapshot.getValue() as Long
                        binding.CoinWithdrawal1.text = currentCoins.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        //setting the user name on the screen
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
                return binding.root//returning view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = categoryadaptor(categoryList, requireActivity())
        binding.categoryRecyclerView.adapter = adapter//setting the adapter
        binding.categoryRecyclerView.setHasFixedSize(true)

    }

    companion object {
    }
}