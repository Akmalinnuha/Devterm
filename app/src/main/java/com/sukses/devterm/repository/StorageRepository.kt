package com.sukses.devterm.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.sukses.devterm.model.Terms

const val TERMS_COLLECTION_REF = "terms"

class StorageRepository {
    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val termsRef: CollectionReference = Firebase
        .firestore.collection(TERMS_COLLECTION_REF)

    fun getUserTerms(
        userId: String,
    ): Flow<Resources<List<Terms>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = termsRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val terms = snapshot.toObjects(Terms::class.java)
                        Resources.Success(data = terms)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getAllTerm(): Flow<Resources<List<Terms>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = termsRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val terms = snapshot.toObjects(Terms::class.java)
                        Resources.Success(data = terms)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getTermByCategory(category: String) : Flow<Resources<List<Terms>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = termsRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("category", category)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val terms = snapshot.toObjects(Terms::class.java)
                        Resources.Success(data = terms)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }

        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getTerm(
        termId:String,
        onError:(Throwable?) -> Unit,
        onSuccess: (Terms?) -> Unit
    ){
        termsRef
            .document(termId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Terms::class.java))
            }
            .addOnFailureListener {result ->
                onError.invoke(result.cause)
            }
    }

    fun addTerm(
        userId: String,
        title: String,
        category: String,
        description: String,
        timestamp: Timestamp,
        onComplete: (Boolean) -> Unit,
    ){
        val documentId = termsRef.document().id
        val term = Terms(
            userId,
            title,
            category,
            description,
            timestamp,
            documentId = documentId
        )
        termsRef
            .document(documentId)
            .set(term)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deleteNote(termId: String,onComplete: (Boolean) -> Unit){
        termsRef.document(termId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateTerm(
        title: String,
        category: String,
        desc:String,
        termId: String,
        onResult:(Boolean) -> Unit
    ){
        val updateData = hashMapOf<String,Any>(
            "title" to title,
            "category" to category,
            "description" to desc
        )

        termsRef.document(termId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

    fun signOut() = Firebase.auth.signOut()
}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)

}