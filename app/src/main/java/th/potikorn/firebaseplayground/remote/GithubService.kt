package th.potikorn.firebaseplayground.remote

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import th.potikorn.firebaseplayground.dao.GithubDetailDao

interface GithubService {

    @GET("users/{username}/repos")
    fun getRepositories(
        @Path("username") username: String
    ): Single<Response<MutableList<GithubDetailDao>>>
}