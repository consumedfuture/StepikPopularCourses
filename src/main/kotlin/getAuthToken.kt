import com.google.gson.JsonParser
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


fun getAuthTokenAsync(httpClient: OkHttpClient): Deferred<String> = async {
    val client_id = "OieK1UdXHX6H7vYuP7qJsh8pZydQKYjA8MvlY3PE"
    val client_secret = "D8UHW7Q70vediWFmNU4iwrnxrObRx2qGXIHeX5mBfmoSQ0nLC9aGXc5xx46vXNzD5egqomkzFaoazHQaqUPlSufwbccuyRLQ6cmZvkrT9e3kIXPGgsdcjHgMMdmeU6CC"

    val credentials = Credentials.basic(client_id, client_secret)
    val body = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()
    val request = Request.Builder()
            .url("https://stepik.org/oauth2/token/")
            .post(body)
            .addHeader("Authorization", credentials)
            .build()
    val response = httpClient.newCall(request).execute()
    val obj = JsonParser().parse(response.body()?.string())
    val token = obj.asJsonObject.get("access_token").toString()
    token
}
