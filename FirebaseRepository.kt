package com.maxfun.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * Firebase qatlamining boshlang'ich servisi.
 * Haqiqiy production security rules Firebase Console'da sozlanadi.
 */
class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun currentUserId(): String? = auth.currentUser?.uid

    fun saveUser(uid: String, data: Map<String, Any>) {
        db.collection("users").document(uid).set(data)
    }

    fun saveVideo(data: Map<String, Any>) {
        db.collection("videos").add(data)
    }

    fun saveStory(data: Map<String, Any>) {
        db.collection("stories").add(data)
    }

    fun saveNote(data: Map<String, Any>) {
        db.collection("notes").add(data)
    }

    fun saveNotification(data: Map<String, Any>) {
        db.collection("notifications").add(data)
    }

    fun storageRoot() = storage.reference
}
