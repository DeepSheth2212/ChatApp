package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sent.view.*

class MsgAdapter(val context : Context , val msgList : ArrayList<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1){
            //inflate sent layout
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent , false)
            return MsgAdapter.SendViewHolder(view)
        }else{
            //inflate receive layout
            val view : View = LayoutInflater.from(context).inflate(R.layout.recieve,parent , false)
            return MsgAdapter.ReceiveViewHolder(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMsg = msgList[position]

        if(holder.javaClass == SendViewHolder::class.java){
            //do the stuff for sendviewholder
            val viewHolder = holder as  SendViewHolder
            holder.sentMsg.text = currentMsg.msg
        } else{
            //do stuff for receiveViewHolder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMsg.text = currentMsg.msg
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMsg = msgList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.senderId)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMsg = itemView.findViewById<TextView>(R.id.txt_sent)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val  receiveMsg = itemView.findViewById<TextView>(R.id.txt_receive)
    }

}