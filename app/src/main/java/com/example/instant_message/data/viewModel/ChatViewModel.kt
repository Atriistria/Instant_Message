package com.example.instant_message.data.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instant_message.R
import com.example.instant_message.data.reponsitory.ChatRepository
import com.example.instant_message.domain.entity.ChatItem
import com.example.instant_message.domain.entity.ChatListItem
import com.example.instant_message.domain.entity.MessageEntity
import com.example.instant_message.domain.entity.MessageRequest
import com.example.instant_message.domain.entity.MessageResponse
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _content = MutableLiveData<String>()
    private val content: LiveData<String> get() = _content
    private val _image = MutableLiveData<Bitmap>()
    val image : LiveData<Bitmap> get() = _image

    private val _chatItems = MutableLiveData<List<ChatItem>>()
    val chatItems: LiveData<List<ChatItem>> get() = _chatItems

    private val _chatListItems = MutableLiveData<List<ChatListItem>>()
    val chatListItems: LiveData<List<ChatListItem>> get() = _chatListItems

    private val userChatHistory = mutableMapOf<String, List<ChatItem>>()

    init {
        _chatItems.value = mutableListOf()
        _chatListItems.value = mutableListOf()
        EventBus.getDefault().register(this)
        loadChatList()
    }

    fun sendMessage(message: MessageRequest){
        viewModelScope.launch {
            repository.sendMessage(message)
            Log.d("ChatViewModel", "sendMessage: $message")
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageResponse){
        viewModelScope.launch {
            handleMessageEvent(event)
        }
    }

    private suspend fun handleMessageEvent(event: MessageResponse){
        val message = event.recipient?.let {
            MessageEntity(
                id = event.timestamp,
                sender = event.sender,
                recipient = it,
                type = event.type,
                content = event.content,
                timestamp = event.timestamp
            )
        }
        message?.let {
            repository.saveMessage(it)
        }

        Log.d("ChatViewModel", "onMessageEvent: ${event.content}")
        val currentItems = _chatItems.value?.toMutableList() ?: mutableListOf()
        if (event.content.isNotEmpty()) {
            val sender = if (event.sender == "atri") "self" else "other"
            currentItems.add(ChatItem(0, R.drawable.ic_me, event.content, sender))
            _chatItems.postValue(currentItems)

            val currentListItems = _chatListItems.value?.toMutableList() ?: mutableListOf()
            val existingItemIndex = currentListItems.indexOfFirst { it.chatName == event.sender }
            if(existingItemIndex != -1){
                currentListItems[existingItemIndex] = ChatListItem(
                    chatId = 0,
                    chatName = event.sender,
                    lastMessage = event.content,
                    lastMessageTime = event.timestamp,
                    isGroupChat = event.type == "group",
                    avatarUrl = R.drawable.ic_me,
                    isClient = event.type == "system",
                    senderName = event.sender,
                )
            }else{
                val chatListItem = ChatListItem(
                    chatId = 0,
                    chatName = event.sender,
                    lastMessage = event.content,
                    lastMessageTime = event.timestamp,
                    isGroupChat = event.type == "group",
                    isClient = event.type == "system",
                    avatarUrl = R.drawable.ic_me,
                    senderName = event.sender,
                )
               currentListItems.add(chatListItem)
            }
           _chatListItems.postValue(currentListItems)

        }
    }

    fun addChatItem(chatItem: ChatItem){
        val currentItems = _chatItems.value?.toMutableList()?: mutableListOf()
        currentItems.add(chatItem)
        Log.d("ChatViewModel", "addChatItem: ${chatItem.message}")
        _chatItems.value = currentItems
    }

    fun loadChatHistory(username: String) {
        if(userChatHistory.containsKey(username)){
            _chatItems.value = userChatHistory[username]
            return
        }
        viewModelScope.launch {
            val message = repository.getChatHistoryBySender(username)
            val chatItemsList = message.sortedBy { it.timestamp }.map{
                val sender = if (it.sender == "self") "self" else "other"
                ChatItem(
                    id = it.id,
                    message = it.content,
                    sender = sender
                )
            }
            userChatHistory[username] = chatItemsList
            _chatItems.value = chatItemsList
        }
    }
    private fun loadChatList(){
        viewModelScope.launch {
            val message = repository.getChatHistory()
            val chatListItemsList = message.groupBy { it.sender }.map { (sender, messages) ->
                val lastMessage = messages.maxByOrNull { it.timestamp }!!
                ChatListItem(
                    chatId = 0,
                    chatName = sender,
                    lastMessage = lastMessage.content,
                    lastMessageTime = lastMessage.timestamp,
                    isGroupChat = lastMessage.type == "group",
                    senderName = sender,
                    isClient = lastMessage.type == "system",
                    avatarUrl = R.drawable.ic_me
                )
            }
            _chatListItems.value = chatListItemsList
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}