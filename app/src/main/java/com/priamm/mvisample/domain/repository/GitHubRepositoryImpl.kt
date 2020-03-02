package com.priamm.mvisample.domain.repository

import com.priamm.mvisample.data.network.GitHubDataSource
import com.priamm.mvisample.domain.entity.RepoEntity
import com.priamm.mvisample.extensions.asRepoEntityList

class GitHubRepositoryImpl(private val gitHubDataSource: GitHubDataSource) : GitHubRepository {

    override suspend fun searchRepos(query: String): List<RepoEntity> =
        gitHubDataSource.searchRepos(query).asRepoEntityList
}