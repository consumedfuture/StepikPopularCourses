import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*

fun popularCourses(n: Int){
    val httpClient = OkHttpClient()
    val token = getAuthTokenAsync(httpClient)
    runBlocking {
        val courses = getCoursesAsync(httpClient, token.await()).await()
        courses.sortWith(compareByDescending<Course> { it.learners_count}.thenBy { it.title } )
        val wantedCourses = courses.slice(0..n-1)
        wantedCourses.forEach{
            println("${it.title}: ${it.learners_count} участников.")
        }
    }
}

