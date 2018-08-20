package th.potikorn.firebaseplayground.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import th.potikorn.firebaseplayground.exception.NotCreateViewModelException
import th.potikorn.firebaseplayground.exception.NotSetViewModelException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw NotSetViewModelException(modelClass.name)
        try {
            return creator.get() as T
        } catch (e: ClassCastException) {
            throw NotCreateViewModelException(e)
        }
    }
}