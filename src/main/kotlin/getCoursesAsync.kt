import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.OkHttpClient
import okhttp3.Request

fun getCoursesAsync(httpClient: OkHttpClient, token: String): Deferred<CoursesList> = async {
    var pageNum = 0
    var hasNextPage = true
    val courses = CoursesList()
    while (hasNextPage){
        try {
            pageNum += 1
            val request = Request.Builder()
                    .url("https://stepik.org/api/courses?page=$pageNum&is_popular=true")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            val response = httpClient.newCall(request).execute()
            val pageContent = JsonParser().parse(response.body()?.string()).asJsonObject
            hasNextPage = pageContent.get("meta").asJsonObject.get("has_next").asBoolean
            val pageCourses: CoursesList = Gson().fromJson(pageContent.get("courses"), CoursesList::class.java)
            pageCourses.forEach {
                if (it.learners_count>0)
                    courses.add(it)
            }
        }
        catch (e: Exception) {
            println("Smth went wrong. $e")
        }
    }
    courses
}