import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*

/*
 * Поиск популярных курсов
 * @param n: Int - количество курсов
 */
fun popularCourses(n: Int){
    val httpClient = OkHttpClient()
    val token = getAuthTokenAsync(httpClient)
    runBlocking {
        val wantedCourses = getCoursesAsync(httpClient, token.await(), n).await()
        wantedCourses.forEach{
            println("${it.title}: ${it.learners_count} участников.")
        }
    }
}

