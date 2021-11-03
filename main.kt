
data class Subject(val title: String, val grade: Int) {}

data class Student(val name: String, val dateBirth: Int, val subject: List<Subject>)
{
    val age=2021-dateBirth
    val averageGrade: Float = subject.average{it.grade.toFloat()}
}

fun <T> Iterable<T>.average(block: (T) -> Float): Float
{
    var size = 0
    var sum = 0.0
    for (element in this)
    {
        sum+=block(element)
        size++
    }
    return (sum/size).toFloat()
}

data class University(val title: String, val students: MutableList<Student>)
{
    val averageGrade: Float = students.average{it.averageGrade}
    val courses: Map<Int, List<Student>> = students.groupBy {it.age}.mapKeys { age(it.key)}
}

fun age(age: Int): Int
{
    return when (age)
    {
        17 -> 1
        18 -> 2
        19 -> 3
        20 -> 4
        21 -> 5
        else -> throw StudentTooYoungException("Ошибка!")
    }
}
class StudentTooYoungException(Text: String): Exception(Text) {}

enum class StudyProgram(val title: String)
{
    testing("Тестирование ПО"),
    programming("Основы программирования"),
    physics("Физика");

    infix fun withGrade(grade: Int) : Subject
    {
        return Subject(title, grade)
    }
}

typealias StudentsListener = (Student) -> Unit

val students = mutableListOf(
    Student("Юрий",2000,listOf(StudyProgram.physics withGrade 5, StudyProgram.programming withGrade 3, StudyProgram.testing withGrade 4)),
    Student("Артем",2001,listOf(StudyProgram.physics withGrade 5, StudyProgram.programming withGrade 4, StudyProgram.testing withGrade 5)),
    Student("Маргарита",2002,listOf(StudyProgram.physics withGrade 4, StudyProgram.programming withGrade 4, StudyProgram.testing withGrade 5)),
    Student("Иван",2002,listOf(StudyProgram.physics withGrade 4, StudyProgram.programming withGrade 4, StudyProgram.testing withGrade 4)),
    Student("Артур",2001,listOf(StudyProgram.physics withGrade 5, StudyProgram.programming withGrade 3, StudyProgram.testing withGrade 5)),
    Student("Владислав",2003,listOf(StudyProgram.physics withGrade 4, StudyProgram.programming withGrade 5, StudyProgram.testing withGrade 4)),
    Student("Мария",2001,listOf(StudyProgram.physics withGrade 5, StudyProgram.programming withGrade 5, StudyProgram.testing withGrade 5))
)

object DataSource{
    val university: University by lazy {
        University(" ", students)
    }
    var onNewStudentListener: StudentsListener ?=null

    fun addStudent (name: String, dBirth: Int, students: MutableList<Student>)
    {
        students.add(Student(name, dBirth, listOf(StudyProgram.physics withGrade 5, StudyProgram.programming withGrade 4, StudyProgram.testing withGrade 3)))
        val addedStudent = students.last()
        onNewStudentListener?.invoke(addedStudent)
    }
}

fun main() {
    DataSource.onNewStudentListener =
    {
        println("Студент $it со средней оценкой по университету ${DataSource.university.averageGrade}")
    }
    println(DataSource.university.averageGrade)
    println(DataSource.university.courses)
    DataSource.addStudent("Варвара", 2004, students)
}





