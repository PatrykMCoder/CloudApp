package com.pmprogramms.cloudapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pmprogramms.cloudapp.R
import com.pmprogramms.cloudapp.fragment.FilesFragmentDirections
import com.pmprogramms.cloudapp.helpers.FileType
import com.pmprogramms.cloudapp.model.File

class FileRecyclerViewAdapter : RecyclerView.Adapter<FileViewHolder>() {
    private var data = ArrayList<File>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val currentItem = data[position]
        holder.titleTextView.text = currentItem.title

        when (currentItem.type) {
            FileType.IMAGE -> {
                holder.imageType.setImageResource(R.drawable.ic_baseline_image_24)
            }
            FileType.VIDEO -> {
                holder.imageType.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
            FileType.PDF -> {
                holder.imageType.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
            }
        }

        holder.cardContainer.setOnClickListener {
            val action =
                FilesFragmentDirections.actionFilesFragmentToFileDetailsFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: ArrayList<File>) {
        this.data = data
        notifyDataSetChanged()
    }
}

class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTextView: TextView = itemView.findViewById(R.id.title_file_text_view)
    val imageType: ImageView = itemView.findViewById(R.id.type_file)
    val cardContainer: CardView = itemView.findViewById(R.id.card_container)
}
