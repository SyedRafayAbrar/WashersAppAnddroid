package com.koraspond.washershub.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.koraspond.washershub.Activities.HomeActivity
import com.koraspond.washershub.R

class LoginFragment : Fragment() {
lateinit var loginBtn:MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBtn = view.findViewById(R.id.login)

        loginBtn.setOnClickListener {
            val intent = Intent(requireContext(),HomeActivity::class.java)
            requireContext().startActivity(intent)
        }
    }

}