package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.myapplication.databinding.FragmentArchivePreviewBoxBinding
import com.android.myapplication.databinding.FragmentArchiveReviewBinding
import com.android.myapplication.ArchiveReviewFragment.DayViewContainer
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ArchivePreviewBoxFragment : Fragment() {
    private lateinit var binding: FragmentArchivePreviewBoxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentArchivePreviewBoxBinding.inflate(inflater, container, false)

        binding.scrollTool.setOnClickListener {
            requireActivity().runOnUiThread {
                val currentFragment = parentFragmentManager.findFragmentById(R.id.preview_box)
                if (currentFragment !is ArchiveFragment) { // 중복 방지
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.rootlayout, ArchiveFragment()) // 아카이빙 메인 화면으로
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // 수정하기 버튼 클릭 -> 수정 완료! Toast 메시지 적용, short = 2초
        binding.modifyButton.setOnClickListener {
            Toast.makeText(requireContext(), "수정 완료!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}
