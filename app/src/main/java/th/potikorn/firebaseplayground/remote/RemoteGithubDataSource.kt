package th.potikorn.firebaseplayground.remote

import javax.inject.Inject

class RemoteGithubDataSource @Inject constructor(private val remoteGithubService: GithubService) {

    fun requestGithubRepositories(username: String) =
        remoteGithubService.getRepositories(username)
}