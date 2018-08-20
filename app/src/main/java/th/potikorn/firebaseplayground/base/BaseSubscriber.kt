package th.potikorn.firebaseplayground.base

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.net.HttpURLConnection

open class BaseSubscriber<T>(private val callback: SubscribeCallback<T>) :
    SingleObserver<Response<T>> {

    interface SubscribeCallback<in T> {
        fun onSuccess(body: T?)
        fun onUnSuccess(message: String?)
        fun onObservableError(message: String?)
        fun onUnAuthorized()
    }

    override fun onSuccess(t: Response<T>) {
        when (t.code()) {
            HttpURLConnection.HTTP_OK -> callback.onSuccess(t.body())
            HttpURLConnection.HTTP_UNAUTHORIZED -> callback.onUnAuthorized()
            else -> callback.onUnSuccess(t.message())
        }
    }

    override fun onSubscribe(d: Disposable) {}

    override fun onError(e: Throwable) {
        e.printStackTrace()
        callback.onObservableError(e.message)
    }
}