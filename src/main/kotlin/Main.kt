
object Main {
   @JvmStatic
   fun main(args: Array<String>) {
       if (args.isNotEmpty()) {
           popularCourses(args[0].toInt())
       } else {
           println("Need number.")
       }
   }
}