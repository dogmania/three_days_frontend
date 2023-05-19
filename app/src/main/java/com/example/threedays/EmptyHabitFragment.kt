package com.example.threedays

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.threedays.databinding.FragmentEmptyHabitBinding

class EmptyHabitFragment : Fragment() {

    private lateinit var binding : FragmentEmptyHabitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmptyHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
        setWelcomeText(nickname)
    }//뷰가 생성되었을 때 setWelcomeText 함수를 호출하고 MainActivity로부터 전달받은 nickname 데이터를 함수의 parameter로 전달함

    private fun setWelcomeText(nickname : String) {
        val welcomeText = view?.findViewById<TextView>(R.id.welcome_text)
        welcomeText?.text = nickname + getString(R.string.welcome)
    }
}