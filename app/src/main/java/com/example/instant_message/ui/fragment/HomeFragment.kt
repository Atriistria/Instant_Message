package com.example.instant_message.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.instant_message.AppDatabase
import com.example.instant_message.R
import com.example.instant_message.data.adapter.ChatListAdapter
import com.example.instant_message.data.factory.ChatViewModelFactory
import com.example.instant_message.data.reponsitory.ChatRepository
import com.example.instant_message.data.viewModel.ChatViewModel
import com.example.instant_message.databinding.FragmentHomeBinding
import com.example.instant_message.ui.activity.ChatActivity

class HomeFragment : Fragment() {

    private lateinit var chatListAdapter: ChatListAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerView: RecyclerView
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel : ChatViewModel by viewModels {
       val database = AppDatabase.getInstance(requireContext())
       val repository = ChatRepository(database)
       ChatViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.homeRv
        viewPager = requireActivity().findViewById(R.id.main_view_pager)

        //滑动时禁用ViewPager滑动
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        viewPager.isUserInputEnabled = false

                    }
                    else -> {
                        viewPager.isUserInputEnabled = true
                    }
                }
            }
        })

        chatListAdapter = ChatListAdapter {
            val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                putExtra("chatName", it.chatName)
            }
            startActivity(intent)
        }

        recyclerView.adapter = chatListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewmodel.chatListItems.observe(viewLifecycleOwner) {
            chatListAdapter.updateChatList(it)
        }
    }
}