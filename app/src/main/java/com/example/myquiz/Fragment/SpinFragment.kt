package com.example.myquiz.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myquiz.databinding.FragmentSpinBinding
import com.example.myquiz.modle.HistoryModelClass
import com.example.myquiz.modle.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.Random

class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    var currentChance = 0L
    var currentCoin = 0L
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            :View? {
        binding = FragmentSpinBinding.inflate(inflater, container, false)

        //showing name of the user on the screen
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

        //showing total spin chance of the user on the screen
        Firebase.database.reference.child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentChance = snapshot.value as Long
                        binding.spinChance.text = currentChance.toString()//(snapshot.value as Long).toString()
                    }
                    else{
                        var temp = 0
                        binding.spinChance.text = temp.toString()
                        binding.Spin.isEnabled = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })


        //showing coins on the screen
        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        currentCoin = snapshot.getValue() as Long
                        binding.Coins.text = currentCoin.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        return binding.root
    }



    private fun showResult(itemTitle: String, spin:Int) {
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        if(spin%2==0){
            var winCoins = itemTitle.toInt()

            Firebase.database.reference.child("playerCoin")
                .child(Firebase.auth.currentUser!!.uid)
                .setValue(winCoins+currentCoin)

            var historyModelClass = HistoryModelClass(System.currentTimeMillis().toString(),winCoins.toString(),false)

            Firebase.database.reference.child("playerCoinHistory")
                .child(Firebase.auth.currentUser!!.uid)
                .push()//for creating new nodes in database
                .setValue(historyModelClass)

            //updating coins in this screen
            binding.Coins.text = (winCoins+currentCoin).toString()
        }
        currentChance = currentChance - 1
        Firebase.database.reference.child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid)
            .setValue(currentChance)
        binding.Spin.isEnabled = true // Enable Spin the button again
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //implement in future
        /*binding.Coins.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }
        binding.CoinWithdrawal1.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialog.enterTransition
        }*/


        binding.Spin.setOnClickListener {
            binding.Spin.isEnabled = false // Disable the button while the wheel is spinning
            if(currentChance > 0){
           val spin = Random().nextInt(6) // Generate a random value between 0 and 5
           val degrees = 60f * spin // Calculate the rotation degree based on the random value

           timer = object : CountDownTimer(5000, 50) {
           var rotation = 0f

        override fun onTick(millisUntilFinished: Long) {
            rotation += 5f // Rotate the wheel
            if (rotation >= degrees) {
                rotation = degrees
                timer.cancel()
                showResult(itemTitles[spin],spin)
            }
            binding.wheel.rotation = rotation
        }

        override fun onFinish() {}
    }.start()
               }
            else{
               Toast.makeText(activity, "Sorry,You have no chance to spin", Toast.LENGTH_SHORT).show()
                  }
        }
    }
}