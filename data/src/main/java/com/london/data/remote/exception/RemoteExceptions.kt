package com.london.data.remote.exception


sealed class RemoteExceptions : Exception() {
    class TimeoutException : RemoteExceptions()
    class ServerErrorException : RemoteExceptions()
    class EntryNotFoundException : RemoteExceptions()
}
