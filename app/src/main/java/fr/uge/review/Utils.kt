package fr.uge.review

import android.util.Log
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Icon(imageVector: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        androidx.compose.material3.Icon(imageVector = imageVector, contentDescription = null)
    }
}

fun Date.withFormat(format: String): String = SimpleDateFormat(format).format(this)

fun <T> handleCall(call: Call<T>, onFailure: (Throwable?) -> Unit = { Log.i("UwU", "fail $it") }, onSuccess: (T) -> Unit) =
    call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) = onFailure(t)

            override fun onResponse(call: Call<T>, response: Response<T>) =
                if (response.isSuccessful) onSuccess(response.body()!!) else onFailure(Throwable(response.toString()))
        })

fun handleCall(call: Call<Void>, onFailure: (Throwable?) -> Unit = { Log.i("UwU", "fail $it") }, onSuccess: () -> Unit) =
    call.enqueue(object : Callback<Void> {
        override fun onFailure(call: Call<Void>, t: Throwable) = onFailure(t)

        override fun onResponse(call: Call<Void>, response: Response<Void>) =
            if (response.isSuccessful) onSuccess() else onFailure(Throwable(response.toString()))
    })