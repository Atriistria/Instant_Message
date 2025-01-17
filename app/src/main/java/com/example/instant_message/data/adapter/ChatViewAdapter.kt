package com.example.instant_message.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instant_message.R
import com.example.instant_message.domain.entity.ChatItem

class ChatViewAdapter :
    ListAdapter<ChatItem, ChatViewAdapter.ChatViewHolder>(ChatItemDiffCallback()) {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatText: TextView = itemView.findViewById(R.id.tv_chat)
        val chatImage: ImageView = itemView.findViewById(R.id.img_chat)
        val chatTextSelf: TextView = itemView.findViewById(R.id.tv_chat_self)
        val chatImageSelf: ImageView = itemView.findViewById(R.id.img_chat_self)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatItem = getItem(position)
        holder.chatText.text = chatItem.message
        holder.chatTextSelf.text = chatItem.message
        chatItem.imageResId?.let {
            if (chatItem.sender == "self") {
                holder.chatImageSelf.setImageResource(it)
            } else {
                holder.chatImage.setImageResource(it)
            }
        }

        if (chatItem.sender == "self") {
            Log.d("ChatViewAdapter", "onBindViewHolder: ${chatItem.sender}")
            holder.chatText.visibility = View.GONE
            holder.chatImage.visibility = View.GONE
            holder.chatTextSelf.visibility = View.VISIBLE
            holder.chatImageSelf.visibility = View.VISIBLE
            holder.chatTextSelf.setBackgroundResource(R.drawable.message_bubble_right)
        } else {
            holder.chatText.visibility = View.VISIBLE
            holder.chatImage.visibility = View.VISIBLE
            holder.chatTextSelf.visibility = View.GONE
            holder.chatImageSelf.visibility = View.GONE
            holder.chatText.setBackgroundResource(R.drawable.message_bubble_left)
        }
    }

    class ChatItemDiffCallback : DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem == newItem
        }
    }
}
