package com.example.myquiz.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myquiz.R
import com.example.myquiz.databinding.FragmentProfileBinding
import com.example.myquiz.modle.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    var isExpand = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //when user click arrow button
        binding.imageButton.setOnClickListener {
            if (isExpand) {
                binding.expadableconstraintlayout.visibility = View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            } else {
                binding.expadableconstraintlayout.visibility = View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)
        }
            isExpand = !isExpand//true = not true(false)
        }

       //setting user details on the screen
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser?.uid!!)
            .addListenerForSingleValueEvent(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var user = snapshot.getValue<User>()
                        binding.profileName.text =  user?.name
                        binding.Name.text = user?.name
                        binding.Email.text = user?.email
                        binding.Password.text = user?.password
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })

        // Inflate the layout for this fragment
        return binding.root
    }

//not required now
    companion object {
    }
}