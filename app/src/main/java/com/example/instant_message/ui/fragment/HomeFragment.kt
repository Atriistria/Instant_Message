package com.example.instant_message.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instant_message.AppDatabase
import com.example.instant_message.R
import com.example.instant_message.data.adapter.ChatListAdapter
import com.example.instant_message.data.factory.ChatViewModelFactory
import com.example.instant_message.data.reponsitory.ChatRepository
import com.example.instant_message.data.viewModel.ChatViewModel
import com.example.instant_message.ui.activity.ChatActivity

class HomeFragment : Fragment() {

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        isGranted -> {
            if (isGranted) {

            }else{

            }
        }
    }
    private lateinit var chatListAdapter: ChatListAdapter
    private val viewmodel : ChatViewModel by viewModels {
       val database = AppDatabase.getInstance(requireContext())
       val repository = ChatRepository(database)
       ChatViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        val recyclerView :RecyclerView= view.findViewById(R.id.recyclerView)
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