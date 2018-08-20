package th.potikorn.firebaseplayground.exception

class NotSetLayoutException : RuntimeException("getLayoutView() not return 0")

class NotSetViewModelException(modelClass: String) : IllegalArgumentException("unknown model class $modelClass")

class NotCreateViewModelException(e: Exception) : RuntimeException(e)