package com.oztrna.demoproject.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orm.SugarRecord
import com.oztrna.demoproject.R
import com.oztrna.demoproject.adapters.RepoListAdapter
import com.oztrna.demoproject.helpers.Manager
import com.oztrna.demoproject.listeners.OnFragmentInteractionListener
import com.oztrna.demoproject.models.GitModel
import com.oztrna.demoproject.models.OwnerModel
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_listing.*
import kotlinx.android.synthetic.main.toolbar.*
import java.security.acl.Owner

class FavoritePage: MyFragment(), RepoListAdapter.RowClickListener {

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title?.text = "Favorites"
        toolbar_fav?.visibility = View.GONE

        context?.let {
            var data = SugarRecord.listAll(GitModel::class.java)

            toolbar_title?.text = String.format("Favorites(%d)", data.size)

            val adapter = RepoListAdapter(ArrayList(data), true)
            adapter.rowClickListener = this
            favorite_list?.adapter = adapter
            favorite_list?.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
        }

        toolbar_back?.setOnClickListener {
            mListener?.onFragmentInteraction(MyFragmentCommand.OPEN_MAIN_PAGE, null)
        }

    }

    override fun onRowClicked(selectedRepo: GitModel) {
        Manager.selectedGitModel = selectedRepo

        val ownerModel = SugarRecord.find(OwnerModel::class.java, "repo_id = ?", selectedRepo.repoId.toString())[0]
        selectedRepo.owner = ownerModel
        selectedRepo.isFav = true

        val bundle = Bundle()
        bundle.putBoolean("isFromFav", true)

        mListener?.onFragmentInteraction(MyFragmentCommand.OPEN_DETAIL_PAGE, bundle)
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

    override val fragmentType: MyFragmentType
        get() = MyFragmentType.MY_FRAGMENT_FAVORITES
}