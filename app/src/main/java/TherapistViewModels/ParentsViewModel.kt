package TherapistViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase


class ParentsViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance("https://pronuntiappfirebase-default-rtdb.europe-west1.firebasedatabase.app/")
    val usersRef = database.getReference("users")
}