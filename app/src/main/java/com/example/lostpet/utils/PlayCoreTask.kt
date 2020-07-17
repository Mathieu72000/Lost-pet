package com.example.lostpet.utils

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Awaits for completion of the task without blocking a thread.
 *
 * This suspending function is cancellable.
 * If the [Job] of the current coroutine is cancelled or completed while this suspending function is waiting, this function
 * stops waiting for the completion stage and immediately resumes with [CancellationException].


 */
suspend fun <T> Task<T>.await(): T?
{
    if (isComplete)
    {
        return if (exception == null)
        {
            result as T
        }
        else
        {
            null
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener {
            if (exception == null)
            {
                cont.resume(result as T)
            }
            else
            {
                cont.resume(null)
            }
        }
        addOnFailureListener {
            cont.resume(null)
        }
    }
}