package com.oztrna.demoproject.models

import com.orm.SugarRecord

class OwnerModel : SugarRecord {

    var ownerId: Int? = null
    var repoId: Int? = null
    var login: String? = null
    var avatarUrl: String? = null
    var url: String? = null
    var htmlUrl: String? = null

    fun OwnerModel() {

    }

    constructor(
        ownerId: Int?,
        repoId: Int?,
        login: String?,
        avatarUrl: String?,
        url: String?,
        htmlUrl: String?
    ) {
        this.ownerId = ownerId
        this.repoId = repoId
        this.login = login
        this.avatarUrl = avatarUrl
        this.url = url
        this.htmlUrl = htmlUrl
    }

    constructor() {}

}

/*
data class OwnerModel(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val url: String,
    val htmlUrl: String
) */