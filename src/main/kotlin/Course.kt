//Дата класс для курсов
data class Course(
        val title: String,
        val learners_count: Int
)
//Список курсов
class CoursesList: ArrayList<Course>()