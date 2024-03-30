package fr.uge.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
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


val language = CodeLang.Java
val theme = CodeThemeType.Monokai.theme()
val themeBackground = Color.hsv(0f, 0f, 0.18f)

@Composable
fun CodeBlock(code: String, modifier: Modifier) {
    val parser = remember { PrettifyParser() }

    val parsedCode = remember {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = code
        )
    }
    Text(text = parsedCode, modifier = modifier)
}

@Composable
fun InjectCodeBlock(content: String) {
    val trimmedContent = content.trim()
    val regex = "```(.*)```".toRegex(RegexOption.DOT_MATCHES_ALL)
    var lastIndex = 0

    Column {
        regex.findAll(trimmedContent).forEach { result ->
            if (result.range.first > lastIndex) {
                Text(text = trimmedContent.substring(lastIndex, result.range.first))
            }
            CodeBlock(code = result.groupValues[1], Modifier
                .fillMaxWidth()
                .background(themeBackground)
                .border(1.dp, Color.Black)
                .background(Color.Transparent)
                .padding(10.dp)
            )
            lastIndex = result.range.last + 1
        }
        if (lastIndex < trimmedContent.length) {
            Text(text = trimmedContent.substring(lastIndex))
        }
    }
}