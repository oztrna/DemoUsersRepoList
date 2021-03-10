package com.oztrna.demoproject.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.SugarRecord
import com.oztrna.demoproject.R
import com.oztrna.demoproject.adapters.RepoListAdapter
import com.oztrna.demoproject.helpers.Manager
import com.oztrna.demoproject.listeners.OnFragmentInteractionListener
import com.oztrna.demoproject.models.GitModel
import com.oztrna.demoproject.models.OwnerModel
import kotlinx.android.synthetic.main.fragment_listing.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class ListingPage: MyFragment(), RepoListAdapter.RowClickListener {

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_back?.visibility = View.GONE

        btn_submit?.setOnClickListener {
            val userName = edit_user_name?.text.toString()

            if (userName == "") {
                Toast.makeText(context, "Username cannot be empty!", Toast.LENGTH_LONG).show()
            } else {
                listing_empty_view?.visibility = View.GONE
                loading_bar?.visibility = View.VISIBLE
                repo_list?.visibility = View.GONE
                fetchGithubDetail(userName)
            }

            hideKeyboard(activity!!)
        }

        toolbar_fav?.setOnClickListener {
            mListener?.onFragmentInteraction(MyFragmentCommand.OPEN_FAVORITE_PAGE, null)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun fetchGithubDetail(username: String) {
        val request =
            Request.Builder().url("https://api.github.com/users/$username/repos")
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                loading_bar?.visibility = View.GONE
                Log.d("SERVICE", "Connection failed")
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.body() == null) {
                    return
                }
                try {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        parseGitDetail(responseBody.string())
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun parseGitDetail(response: String) {
        try {
            val root = JSONArray(response)
            val data = ArrayList<GitModel>()

            for (i in 0 until root.length()) {
                val item = root.getJSONObject(i)

                val owner = item.getJSONObject("owner")

                val ownerModel = OwnerModel(owner.getInt("id"),
                    item.getInt("id"),
                    owner.getString("login"),
                    owner.getString("avatar_url"),
                    owner.getString("url"),
                    owner.getString("html_url")
                )

                var gitModel = GitModel(
                    item.getInt("id"),
                    item.getString("name"),
                    item.getString("full_name"),
                    item.getBoolean("private"),
                    ownerModel,
                    item.getInt("size"),
                    item.getInt("open_issues_count"),
                    item.getString("created_at"),
                    item.getString("updated_at"),
                    false
                )

                if (SugarRecord.find(GitModel::class.java, "repo_id = ?", gitModel.repoId.toString()).size > 0) {
                    gitModel.isFav = true
                }

                data.add(gitModel)
            }

            Manager.lastGitModel = data

            activity?.let {
                it.runOnUiThread {
                    val adapter = RepoListAdapter(data)
                    adapter.rowClickListener = this
                    repo_list?.adapter = adapter
                    repo_list?.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)

                    repo_list?.visibility = View.VISIBLE
                    loading_bar?.visibility = View.GONE

                    if (data.size == 0) {
                        listing_empty_view?.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    override fun onRowClicked(selectedRepo: GitModel) {
        Manager.selectedGitModel = selectedRepo

        mListener?.onFragmentInteraction(MyFragmentCommand.OPEN_DETAIL_PAGE, null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onResume() {
        super.onResume()

        var data = Manager.lastGitModel
        if (data != null) {

            listing_empty_view?.visibility = View.GONE

            data.forEachIndexed { index, gitModel ->
                if (SugarRecord.find(GitModel::class.java, "repo_id = ?", gitModel.repoId.toString()).size > 0) {
                    gitModel.isFav = true
                } else {
                    gitModel.isFav = false
                }
            }

            context?.let {
                val adapter = RepoListAdapter(data)
                adapter.rowClickListener = this
                repo_list?.adapter = adapter
                repo_list?.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
        }
    }

    override val fragmentType: MyFragmentType
        get() = MyFragmentType.MY_FRAGMENT_MAIN_LIST
}