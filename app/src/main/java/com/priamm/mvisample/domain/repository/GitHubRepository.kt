package com.priamm.mvisample.domain.repository

import com.priamm.mvisample.domain.entity.RepoEntity

interface GitHubRepository {
    suspend fun searchRepos(query: String): List<RepoEntity>
}