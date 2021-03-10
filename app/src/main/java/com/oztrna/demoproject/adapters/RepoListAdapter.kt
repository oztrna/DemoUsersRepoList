package com.oztrna.demoproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oztrna.demoproject.R
import com.oztrna.demoproject.models.GitModel
import kotlinx.android.synthetic.main.recycler_repo_list.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RepoListAdapter(private val data: ArrayList<GitModel>, private val fromFav: Boolean = false) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    interface RowClickListener {
        fun onRowClicked(selectedRepo: GitModel)
    }

    var rowClickListener: RowClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_repo_list, parent, false)
        return RepoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.itemView.repo_list_created_at.text = String.format("Created at: %s", convertDate("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd", data[position].createdAt!!))
        holder.itemView.repo_list_updated_at.text = String.format("Last updated: %s", convertDate("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd", data[position].updatedAt!!))

        if (fromFav) {
            holder.itemView.repo_list_name.text = data[position].fullName
        } else {
            holder.itemView.repo_list_name.text = data[position].name
        }

        if (data[position].isFav!!) {
            holder.itemView.repo_list_star?.visibility = View.VISIBLE
        } else {
            holder.itemView.repo_list_star?.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            rowClickListener?.onRowClicked(data[position])
        }
    }

    private fun convertDate(inputFormat: String, outputFormat: String, inputDate: String): String {
        var parsed: Date?
        var outputDate = ""
        val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())
        try {
            parsed = df_input.parse(inputDate) as Date
            outputDate = df_output.format(parsed)
        } catch (e: ParseException) {
        }
        return outputDate
    }

    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}