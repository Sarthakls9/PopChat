package com.example.popchat

import android.animation.ValueAnimator
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class messageAdapter(val context: Context, val messageList: ArrayList<message>): RecyclerView.Adapter<ViewHolder>() {

    val ITEM_RECIEVE = 1
    val ITEM_SENT = 2
    val SONG = 3
    private var isPlaying = false
    private val mediaPlayer = MediaPlayer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
            return RecieveViewHolder(view)
        }else if (viewType == 2){
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.song_in_chat, parent, false)
            return songChat(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if(holder.javaClass == songChat::class.java){
            val ViewHolder = holder as songChat

            holder.ttl.text = currentMessage.title
            Picasso.get().load(currentMessage.coverImg).into(holder.coverImg)
            holder.plyBtn.setOnClickListener {


                if (isPlaying) {
                    // Stop the playback and reset the animation
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0) // Reset playback to the beginning
                    isPlaying = false
                    notifyDataSetChanged()
                } else {
                    // Play the selected item
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(context, currentMessage.preview!!.toUri())
                    mediaPlayer.prepare()
                    mediaPlayer.start()

                    // Add animation or any other UI updates here if needed
                    val animator = ValueAnimator.ofFloat(0f, 0.5f)
                    animator.addUpdateListener { animator ->
                        holder.plyBtn.progress = animator.animatedValue as Float
                    }
                    animator.duration = 500
                    animator.start()

                    isPlaying = true
                    notifyDataSetChanged()
                }


            }
        }
        else if (holder.javaClass == SentViewHolder::class.java){

            val ViewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else{

            val ViewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message

        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]
        if(currentMessage.senderId.equals("song")){
            return SONG
        }
        else if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECIEVE
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.text_sent_message)
    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.text_recieve_message)
    }

    class songChat(itemView: View):RecyclerView.ViewHolder(itemView){
        val coverImg = itemView.findViewById<ImageView>(R.id.musicImageChat)
        val plyBtn = itemView.findViewById<LottieAnimationView>(R.id.playBtnChat)
        val ttl = itemView.findViewById<TextView>(R.id.musicTitlechat)
    }
}