package com.example.myquiz.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myquiz.databinding.CategoryitemBinding
import com.example.myquiz.QuizActivity
import com.example.myquiz.R
import com.example.myquiz.modle.categoryModelClass

class categoryadaptor(
    var categoryList: ArrayList<categoryModelClass>,
    var requireActivity: FragmentActivity)
    : RecyclerView.Adapter<categoryadaptor.MycategoryViewHolder>()
{
    class MycategoryViewHolder(var binding: CategoryitemBinding) :
          RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MycategoryViewHolder {
        return MycategoryViewHolder(
            CategoryitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: MycategoryViewHolder, position: Int) {
        val datalist = categoryList[position]
        holder.binding.categoryImage.setImageResource(datalist.catImage)
        holder.binding.category.text = datalist.catText
        //animation for recycler view
        holder.binding.categorybtn
            .startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,
             R.anim.anim_recycler))
        holder.binding.categorybtn.setOnClickListener {
                    val intent = Intent(requireActivity, QuizActivity::class.java)
            //sending image and name of category with Intent for QuizActivity clas
                    intent.putExtra("categoryimg", datalist.catImage)
                    intent.putExtra("questionType", datalist.catText)
                    requireActivity.startActivity(intent)
                }
            }
    }