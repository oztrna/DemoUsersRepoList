package com.oztrna.demoproject.models

import com.orm.SugarRecord

class GitModel : SugarRecord {

        var repoId: Int? = null
        var name: String? = null
        var fullName: String? = null
        var private: Boolean? = null
        var owner: OwnerModel? = null
        var size: Int? = null
        var openIssuesCount: Int? = null
        var createdAt: String? = null
        var updatedAt: String? = null
        var isFav: Boolean? = null

        fun GitModel() {

        }

        constructor(
                repoId: Int?,
                name: String?,
                fullName: String?,
                private: Boolean?,
                owner: OwnerModel?,
                size: Int?,
                openIssuesCount: Int?,
                createdAt: String?,
                updatedAt: String?,
                isFav: Boolean?
        ) {
                this.repoId = repoId
                this.name = name
                this.fullName = fullName
                this.private = private
                this.owner = owner
                this.size = size
                this.openIssuesCount = openIssuesCount
                this.createdAt = createdAt
                this.updatedAt = updatedAt
                this.isFav = isFav
        }

        override fun getId(): Long {
                return id!!
        }

        override fun setId(id: Long) {
                super.setId(id)
        }

        constructor() {}

}
