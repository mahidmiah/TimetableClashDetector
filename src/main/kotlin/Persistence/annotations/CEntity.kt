package Persistence.annotations


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CEntity(
    val pkColumn: String = ""
)

