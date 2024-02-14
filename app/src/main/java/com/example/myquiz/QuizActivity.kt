package com.example.myquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import com.example.myquiz.databinding.ActivityQuizBinding
import com.example.myquiz.modle.Question
import com.example.myquiz.modle.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.String

class QuizActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }
    private lateinit var questionList: ArrayList<Question>
    var counter = 0//for correct question
    var endQuiz = true
    var currentChance = 0L
    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //shimmer effect
        binding.shimmerEffect.startShimmer()

        //these button show on the score screen and it redirected to the HomeActivity
        binding.backArrow2.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
        binding.backArrow1.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }



        //here we updating user coins in all screen
        Firebase.database.reference.child("playerCoin")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var currentCoins = snapshot.getValue() as Long
                        binding.CoinWithdrawal.text = currentCoins.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })//end of addValueEventListener

        //here we updating name of user
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


        //here we increment chances of spin whenever user win
        Firebase.database.reference.child("PlayChance")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //updating currentChance
                        currentChance = snapshot.value as Long
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        //here we getting the image of category selected by user
        val image = intent.getIntExtra("categoryimg", 0)
        //setting image of category which chosen by user
        binding.categoryimg.setImageResource(image)

        val catText = intent.getStringExtra("questionType")

        //initialize our questionList
        questionList = ArrayList()

        //here we getting data(question and options) from Fire store
        Firebase.firestore.collection("QuizCategory")
            .document(catText.toString())//this matches Category name of our subject
            .collection("Questions")
            .get()
            .addOnSuccessListener { questionData ->
                questionList.clear()//clear the previous questions
                for (data in questionData.documents) {
                    //hide the shimmer after data loaded
                    binding.shimmerEffect.hideShimmer()

                    var question: Question? = data.toObject(Question::class.java)
                    questionList.add(question!!)
                }
                //showing number of question at playing time
                binding.numbQuests.text = questionList.size.toString()

                //if question is presents in Category, then only we can show
                if (questionList.size > 0) {
                    binding.question.text = questionList.get(currentQuestion).question
                    binding.option1.text = questionList.get(currentQuestion).option1
                    binding.option2.text = questionList.get(currentQuestion).option2
                    binding.option3.text = questionList.get(currentQuestion).option3
                    binding.option4.text = questionList.get(currentQuestion).option4
                }
                else { }
            }



        //functionality of coins when we clicking on them

        /*//when user wants to withdrawal their coins by clicking on Text of coins
        binding.CoinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
        //when user wants to withdrawal their coins by clicking on image of coins
        binding.CoinWithdrawal1.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(this@QuizActivity.supportFragmentManager, "TEST")
            bottomSheetDialog.enterTransition
        }
*/


//implementing the functionality when user choose any option
        binding.option1.setOnClickListener {
            //this method get the value of options from screen
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }
    }

    private fun nextQuestionAndScoreUpdate(s: String) {
        //if user answer is correct
        if (s.equals(questionList.get(currentQuestion).answer)) {
            counter++
            //Toast.makeText(this@QuizActivity,""+counter,Toast.LENGTH_SHORT).show()
        }
        currentQuestion++
        //setting the current question number
        var currQuest = currentQuestion
        binding.currentQuest.text = "" + currQuest.plus(1) + " /"

        //calculating the overall performance
        if (currentQuestion >= questionList.size) {
            if (endQuiz) {
                if (counter > questionList.size / 2) {
                    binding.userScore.text = "Your score is: $counter " + "/ ${questionList.size}"
                    binding.winner.visibility = VISIBLE
                    Firebase.database.reference.child("PlayChance")
                        .child(Firebase.auth.currentUser!!.uid)
                        .setValue(currentChance + 1)
                } else {
                    binding.lessUserScore.text = "Your score is: $counter " + "/ ${questionList.size}"
                    binding.sorry.visibility = VISIBLE
                }
            } else { }
        }
        else {
            //otherwise continues upcoming question and their options
            binding.question.text = questionList.get(currentQuestion).question
            binding.option1.text = questionList.get(currentQuestion).option1
            binding.option2.text = questionList.get(currentQuestion).option2
            binding.option3.text = questionList.get(currentQuestion).option3
            binding.option4.text = questionList.get(currentQuestion).option4
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Stop shimmer animation when the activity is destroyed
        binding.shimmerEffect.stopShimmer()
    }

}