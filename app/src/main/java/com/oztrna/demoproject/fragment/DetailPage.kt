package com.oztrna.demoproject.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.orm.SugarRecord
import com.oztrna.demoproject.R
import com.oztrna.demoproject.helpers.Manager
import com.oztrna.demoproject.listeners.OnFragmentInteractionListener
import com.oztrna.demoproject.models.GitModel
import com.oztrna.demoproject.models.OwnerModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailPage: MyFragment() {

    private var mListener: OnFragmentInteractionListener? = null
    private var data: GitModel? = Manager.selectedGitModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFromFav = arguments?.getBoolean("isFromFav", false)

        toolbar_title?.text = data?.name
        detail_owner_name?.text = data?.owner?.login
        detail_star_count?.text = String.format("Stars: %d", data?.size)
        detail_open_issues_count?.text = String.format("Open Issues: %d", data?.openIssuesCount)

        Glide.with(this).load(data?.owner?.avatarUrl).into(detail_owner_img);

        toolbar_back?.setOnClickListener {

            if (isFromFav != null && isFromFav) {
                mListener!!.onFragmentInteraction(MyFragmentCommand.OPEN_FAVORITE_PAGE, null)
            } else {
                mListener?.onFragmentInteraction(MyFragmentCommand.OPEN_MAIN_PAGE, null)
            }

        }

        toolbar_fav?.setOnClickListener {
            favRepo()
        }

        if (data != null && data!!.isFav!!) {
            toolbar_fav?.setImageResource(R.drawable.ic_heart_on)
        }
    }

    private fun favRepo() {
        if (data != null) {
            if (data!!.isFav!!) {
                SugarRecord.executeQuery("DELETE FROM GIT_MODEL WHERE repo_id = ${data!!.repoId.toString()}")
                SugarRecord.executeQuery("DELETE FROM OWNER_MODEL WHERE repo_id = ${data!!.repoId.toString()}")
                data?.isFav = false
                toolbar_fav?.setImageResource(R.drawable.ic_heart_off)
            } else {
                data?.save()
                data?.owner?.save()
                data?.isFav = true
                toolbar_fav?.setImageResource(R.drawable.ic_heart_on)
            }
        }
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
        get() = MyFragmentType.MY_FRAGMENT_REPO_DETAIL
}