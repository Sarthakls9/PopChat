package com.example.popchat

import android.animation.ValueAnimator
import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.airbnb.lottie.LottieAnimationView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class csvAdapter(val context: Activity, var dataList: List<csvData>, private val mediaPlayer: MediaPlayer):
    RecyclerView.Adapter<csvAdapter.MyViewHolder>() {
    var playingPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): csvAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.new_song_element, parent, false)
        return csvAdapter.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: csvAdapter.MyViewHolder, position: Int) {
        val currentData = dataList[position]

        holder.title.text = currentData.data__title
        Picasso.get().load(currentData.data_album_cover).into(holder.image)

        if (playingPosition == position) {
            // If the current position is the one being played, adjust the play button animation
            holder.play.progress = if (mediaPlayer.isPlaying) 0.5f else 1f
        } else {
            // If not playing, reset the play button animation
            holder.play.progress = 0f
        }

        holder.play.setOnClickListener {
            if (playingPosition == position) {
                // Stop the playback and reset the animation if the same item is clicked again
                mediaPlayer.pause()
                playingPosition = -1
                notifyDataSetChanged()
            } else {
                // Stop the playback and reset the animation of the previously playing item
                if (playingPosition != -1) {
                    notifyItemChanged(playingPosition)
                }

                // Play the selected item
                mediaPlayer.reset()
                mediaPlayer.setDataSource(context, currentData.data__preview.toUri())
                mediaPlayer.prepare()
                mediaPlayer.start()

                val animator = ValueAnimator.ofFloat(0f, 0.5f)
                animator.addUpdateListener { animator ->
                    holder.play.progress = animator.animatedValue as Float
                }
                animator.duration = 500
                animator.start()

                playingPosition = position
                notifyDataSetChanged()
            }
        }
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.musicImage)
        val title: TextView = itemView.findViewById(R.id.musicTitle)
        val play: LottieAnimationView = itemView.findViewById(R.id.playBtn)
    }

}