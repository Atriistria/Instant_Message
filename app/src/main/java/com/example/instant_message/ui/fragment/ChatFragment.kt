package com.example.instant_message.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instant_message.AppDatabase
import com.example.instant_message.R
import com.example.instant_message.data.adapter.ChatViewAdapter
import com.example.instant_message.data.factory.ChatViewModelFactory
import com.example.instant_message.data.reponsitory.ChatRepository
import com.example.instant_message.data.viewModel.ChatViewModel
import com.example.instant_message.databinding.FragmentChatBinding
import com.example.instant_message.domain.entity.ChatItem
import com.example.instant_message.domain.entity.MessageRequest
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.util.WebsocketEvent.SEND_FRIEND_MESSAGE
import com.example.instant_message.ui.activity.LoginActivity


//TODO()
class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? =null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var database: AppDatabase
    private lateinit var chatViewAdapter: ChatViewAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewmodel: ChatViewModel by viewModels {
        database = AppDatabase.getInstance(requireContext())
        val repository = ChatRepository(database)
        ChatViewModelFactory(repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sessionManager = SessionManager.getInstance(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatViewAdapter = ChatViewAdapter()
        recyclerView.adapter = chatViewAdapter

        viewmodel.chatItems.observe(viewLifecycleOwner){ newItems ->
            chatViewAdapter.submitList(newItems)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        arguments?.getString("chatName")?.let { chatName ->
//            binding.toolbarTitle.text = chatName
//        }
        setupClickListeners()
    }

    private fun setupClickListeners() {

        binding.send.setOnClickListener {
            val messageContent = binding.input.text.toString()
            if (messageContent.isNotBlank()){
                val messageRequest = MessageRequest(
                    event = SEND_FRIEND_MESSAGE,
                    content = messageContent,
                    sender = sessionManager.getUsername()!!,
                    recipient = "atride"
                )
                Log.d("ChatFragment", "sendMessage: $messageRequest")
                viewmodel.sendMessage(messageRequest)

                val chatItem = ChatItem(
                    id = (viewmodel.chatItems.value?.size ?: 0).toLong(),
                    imageResId = R.drawable.ic_contacts_pressed,
                    message = messageContent,
                    sender = "self"
                )
                viewmodel.addChatItem(chatItem)
                Log.d("ChatFragment", "addChatItem: ${chatItem.message}")
                binding.input.text.clear()

                recyclerView.post{
                    recyclerView.scrollToPosition(chatViewAdapter.itemCount - 1)
                }
            }else{
                //TODO
            }
        }


//        binding.file.setOnClickListener {
//
//        }
//
//
//        binding.actionButton.setOnClickListener{
//
//        }
        binding.toolbarChat.title = "Chat"
        binding.toolbarChat.setNavigationOnClickListener {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            sessionManager.clearSession()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}