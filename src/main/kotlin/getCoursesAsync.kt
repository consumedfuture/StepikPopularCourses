import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.OkHttpClient
import okhttp3.Request

/*
 * Получение курсов на сервере
 * Обработка и сохранение курсов
 * @param httpClient: OkHttpClient - http клиент
 * @param token: String - токен для авторизации
 * @param n: Int - необходимое количестов курсов
 * @return courses: CoursesList - список популярных курсов
 */
fun getCoursesAsync(httpClient: OkHttpClient, token: String, n: Int): Deferred<CoursesList> = async {
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
                //добавляем курс, если количество учеников больше, чем у самого непопулярного курса на данный момент
                try {
                    if (it.learners_count>courses.last().learners_count)
                        courses.add(it)
                }
                catch (e: NoSuchElementException){
                    courses.add(it)
                }
                //сортируем по убыванию учеников
                courses.sortWith(compareByDescending{ it.learners_count} )
                //убираем лишний, если вышли за границу необходимого количества курсов
                if (courses.size > n)
                    courses.removeAt(n)
            }
        }
        catch (e: Exception) {
            println("Smth went wrong. $e")
        }
    }
    courses
}