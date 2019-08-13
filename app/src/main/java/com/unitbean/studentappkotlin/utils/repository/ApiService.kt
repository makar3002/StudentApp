package com.unitbean.studentappkotlin.utils.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.unitbean.studentappkotlin.utils.repository.model.UserTokenModel
import com.unitbean.studentappkotlin.utils.repository.requests.AuthRequest
import com.unitbean.studentappkotlin.utils.repository.responses.AuthResponse

class ApiService() {

    val dataBase: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getUser(authRequest: AuthRequest): AuthResponse {
        dataBase
            .collection("students")
            .document(authRequest.studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    if (authRequest.type == UserTokenModel.TokenType.NotVK){
                        deleteUser(authRequest.studentId)
                        registerUser(authRequest.studentId)
                    }
                }
                else registerUser(authRequest.studentId)
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        val data = Tasks.await(dataBase
            .collection("students")
            .document(authRequest.studentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (authRequest.type == UserTokenModel.TokenType.NotVK){
                        deleteUser(authRequest.studentId)
                        registerUser(authRequest.studentId)
                    }
                }
                else registerUser(authRequest.studentId)
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        ).data
        val authResp = AuthResponse(authRequest.studentId, data?.get("firstName").toString(), data?.get("lastName").toString(), data?.get("institute").toString(), data?.get("course").toString(), data?.get("group").toString(), data?.get("recordBook").toString())
        return authResp
    }
    fun registerUser(studentId: String) {
        val student = mapOf(
            "firstName" to "",
            "lastName" to "",
            "institute" to "",
            "course" to "",
            "group" to "",
            "recordBook" to ""
        )
        dataBase.collection("students")
            .document(studentId)
            .set(student)
            .addOnSuccessListener {
                    //Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                    //e -> Log.w(TAG, "Error writing document", e)
            }

    }
    fun changeUser(studentId: String?,
                   firstName: String,
                   lastName: String,
                   institute: String,
                   course: String,
                   group: String,
                   recordBook: String) {
        val student = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "institute" to institute,
            "course" to course,
            "group" to group,
            "recordBook" to recordBook
        )
        if (studentId != null) dataBase.collection("students")
            .document(studentId)
            .set(student)
            .addOnSuccessListener {
                //Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                //e -> Log.w(TAG, "Error writing document", e)
            }.isSuccessful

    }
    fun deleteUser(studentId: String) {
        if (studentId != "") dataBase.collection("students").document(studentId).delete()
    }
}