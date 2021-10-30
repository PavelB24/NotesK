import android.app.Application
import ru.barinov.notes.domain.NotesRepository

class Application : Application() {
    val repository = NotesRepository()
}