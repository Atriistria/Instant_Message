package com.example.instant_message.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instant_message.R
import com.example.instant_message.domain.entity.ChatListItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ChatListAdapter(private val clickListener: (ChatListItem)-> Unit) : ListAdapter<ChatListItem,ChatListAdapter.ChatListViewHolder> (ChatListItemDiffCallback()){
    class ChatListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chatImage: ImageView = itemView.findViewById(R.id.img_list_chat)
        val chatName: TextView = itemView.findViewById(R.id.tv_list_chat_name)
        val lastMessage: TextView = itemView.findViewById(R.id.tv_list_last_message)
        val lastMessageTime: TextView = itemView.findViewById(R.id.tv_list_last_message_time)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list,parent,false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chatListItem = getItem(position)
        holder.chatName.text = chatListItem.chatName
        holder.lastMessage.text = chatListItem.lastMessage
        holder.lastMessageTime.text = formatTime(chatListItem.lastMessageTime)
        Glide.with(holder.itemView.context)
            .load(chatListItem.avatarUrl)
            .into(holder.chatImage)
        holder.itemView.setOnClickListener {
            clickListener(chatListItem)
        }
    }

    private fun formatTime(timestamp: Long):String{
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        return currentTime.format(Date(timestamp))
    }
    class ChatListItemDiffCallback :DiffUtil.ItemCallback<ChatListItem>(){
        override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return oldItem.chatName == newItem.chatName &&
                    oldItem.lastMessage == newItem.lastMessage &&
                    oldItem.lastMessageTime == newItem.lastMessageTime
        }
    }
    fun updateChatList(newList: List<ChatListItem>) {
        submitList(newList)
    }

}