package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var msgAdapter: MsgAdapter
    private lateinit var sent_button : ImageView
    private lateinit var msg_box : EditText
    private lateinit var msgList : ArrayList<Msg>

    private var receiverRoom : String? = null
    private var senderRoom : String? = null

    private lateinit var mDbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom  = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        supportActionBar?.title = name

        msgList = ArrayList()
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        sent_button = findViewById(R.id.sent_img)
        msg_box = findViewById(R.id.msg_box)
        msgAdapter = MsgAdapter(this,msgList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = msgAdapter

        //logic for data to add in recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Msg::class.java)
                    msgList.add(message!!)
                }

                msgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        //adding msg to database
        sent_button.setOnClickListener{
            val message = msg_box.text.toString()
            val messageObj = Msg(message,senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObj).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObj)
            }
            msg_box.setText("")
        }

    }
}