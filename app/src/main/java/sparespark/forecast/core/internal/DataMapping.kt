package sparespark.forecast.core.internal

import com.google.firebase.auth.FirebaseUser
import sparespark.forecast.data.db.entity.CurrentUserEntry
import sparespark.forecast.data.model.User


internal val User.toUserEntry: CurrentUserEntry
    get() = CurrentUserEntry(
        this.uid,
        this.name
    )
internal val FirebaseUser.toUser: User
    get() = User(
        uid = this.uid,
        name = this.displayName ?: "",
    )
