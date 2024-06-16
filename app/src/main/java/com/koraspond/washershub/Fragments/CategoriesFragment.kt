package com.koraspond.washershub.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koraspond.washershub.Adapters.CategoriesFilterAdapter
import com.koraspond.washershub.R


class CategoriesFragment : Fragment() {
lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.cat_rcv)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CategoriesFilterAdapter(requireContext())
    }


}