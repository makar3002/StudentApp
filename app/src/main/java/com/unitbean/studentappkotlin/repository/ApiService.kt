package com.unitbean.studentappkotlin.repository

import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.unitbean.studentappkotlin.utils.requests.AuthRequest
import com.unitbean.studentappkotlin.utils.requests.DatesRequest
import com.unitbean.studentappkotlin.utils.requests.LessonRequest
import com.unitbean.studentappkotlin.utils.responses.AuthResponse
import com.unitbean.studentappkotlin.utils.responses.DatesResponse
import com.unitbean.studentappkotlin.utils.responses.LessonResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApiService {

    val dataBase: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getDates(datesRequest: DatesRequest): DatesResponse? {
        val data = Tasks.await(dataBase
            .collection("schedules")
            .document("institutes")
            .collection(datesRequest.institute)
            .document("courses")
            .collection(datesRequest.course)
            .document("groups")
            .collection(datesRequest.group)
            .document("semesters")
            .collection(datesRequest.semester)
            .document("dates")
            .get()
            .addOnSuccessListener { document ->
                //Log.d(TAG, "DocumentSnapshot successfully got!")
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        ).data
        val startDate = data?.get("startDate")
        val endDate = data?.get("endDate")
        val datesResp = DatesResponse(
            if (startDate is Timestamp) startDate.toDate() else return null,
            if (endDate is Timestamp) endDate.toDate() else return null
            )
        return datesResp
    }

    fun getLessons(lessonRequest: LessonRequest): ArrayList<LessonResponse> {
        val data = Tasks.await(dataBase
            .collection("schedules")
            .document("institutes")
            .collection(lessonRequest.institute)
            .document("courses")
            .collection(lessonRequest.course)
            .document("groups")
            .collection(lessonRequest.group)
            .document("semesters")
            .collection(lessonRequest.semester)
            .document("lessons")
            .collection(lessonRequest.dayOfWeek)
            .get()
            .addOnSuccessListener { document ->
                //Log.d(TAG, "DocumentSnapshot successfully got!")
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        ).documents
        val lessonsList: ArrayList<LessonResponse> = ArrayList()
        for (document in data) {
            lessonsList.add(LessonResponse(lessonRequest.date, document.data?.get("startTime").toString(), document.data?.get("endTime").toString(),document.data?.get("lessonName").toString(), document.data?.get("teacherName").toString(), document.data?.get("auditoryNumber").toString()))
        }
        return lessonsList
    }

    fun setDates(institute: String, course: String, group: String, semester: String, startDate: Date, endDate: Date) {
        val dates = hashMapOf(
            "startDate" to Timestamp(startDate),
            "endDate" to Timestamp(endDate)
        )
        dataBase
            .collection("schedules")
            .document("institutes")
            .collection(institute)
            .document("courses")
            .collection(course)
            .document("groups")
            .collection(group)
            .document("semesters")
            .collection(semester)
            .document("dates")
            .set(dates)
            .addOnSuccessListener { document ->
                //Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getUser(authRequest: AuthRequest): AuthResponse {
        dataBase
            .collection("students")
            .document(authRequest.studentId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) registerUser(authRequest.studentId)
                //Log.d(TAG, "DocumentSnapshot successfully got!")
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        val data = Tasks.await(dataBase
            .collection("students")
            .document(authRequest.studentId)
            .get()
            .addOnSuccessListener { document ->
                //Log.d(TAG, "DocumentSnapshot successfully got!")
            }
            .addOnFailureListener { exception ->
                //Log.w(TAG, "Error getting documents: ", exception)
            }
        ).data
        val authResp = AuthResponse(
            authRequest.studentId,
            data?.get("firstName").toString(),
            data?.get("lastName").toString(),
            data?.get("institute").toString(),
            data?.get("course").toString(),
            data?.get("group").toString(),
            data?.get("recordBook").toString(),
            data?.get("semester").toString()
        )
        return authResp
    }
    fun registerUser(studentId: String) {
        val student = mapOf(
            "firstName" to "",
            "lastName" to "",
            "institute" to "",
            "course" to "",
            "group" to "",
            "recordBook" to "",
            "semester" to ""
        )
        dataBase.collection("students")
            .document(studentId)
            .set(student, SetOptions.merge())
            .addOnSuccessListener {
                //Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                //e -> Log.w(TAG, "Error writing document", e)
            }

    }

    fun registerLesson(lessonRequest: LessonRequest) {
        val lesson = mapOf(
            "lessonName" to lessonRequest.lessonName,
            "teacherName" to lessonRequest.teacherName,
            "auditoryNumber" to lessonRequest.auditoryNumber,
            "startTime" to lessonRequest.startTime,
            "endTime" to lessonRequest.endTime
        )

        val dateFormatter: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.YYYY", Locale.getDefault()) }
        val data = dataBase
            .collection("schedules")
            .document("institutes")
            .collection(lessonRequest.institute)
            .document("courses")
            .collection(lessonRequest.course)
            .document("groups")
            .collection(lessonRequest.group)
            .document("semesters")
            .collection(lessonRequest.semester)
            .document("lessons")
        if (lessonRequest.dayOfWeek != "") {
            data.collection(lessonRequest.dayOfWeek)
            .document(lessonRequest.startTime + lessonRequest.endTime)
                .set(lesson)
                .addOnSuccessListener { document ->
                    //Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { exception ->
                    //Log.w(TAG, "Error getting documents: ", exception)
                }
        } else {
            data.collection("anotherLessons")
                .document("anotherLessons")
                .collection(dateFormatter.format(lessonRequest.date))

                .document(lessonRequest.startTime + lessonRequest.endTime)
                .set(lesson)
                .addOnSuccessListener { document ->
                    //Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { exception ->
                    //Log.w(TAG, "Error getting documents: ", exception)
                }
        }

    }

    fun changeUser(studentId: String?,
                   firstName: String,
                   lastName: String,
                   institute: String,
                   course: String,
                   group: String,
                   recordBook: String,
                   semester: String) {
        val student = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "institute" to institute,
            "course" to course,
            "group" to group,
            "recordBook" to recordBook,
            "semester" to semester
        )
        if (studentId != null && studentId != "") dataBase.collection("students")
            .document(studentId)
            .set(student)
            .addOnSuccessListener {
                //Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener {
                //e -> Log.w(TAG, "Error writing document", e)
            }

    }

    fun deleteUser(studentId: String) {
        if (studentId != "") dataBase.collection("students").document(studentId).delete()
    }

    fun deleteLesson(lessonRequest: LessonRequest) {
        val data = dataBase
            .collection("schedules")
            .document("institutes")
            .collection(lessonRequest.institute)
            .document("courses")
            .collection(lessonRequest.course)
            .document("groups")
            .collection(lessonRequest.group)
            .document("semesters")
            .collection(lessonRequest.semester)
            .document("lessons")
        if (lessonRequest.dayOfWeek != "") {
            data.collection(lessonRequest.dayOfWeek)
                .document(lessonRequest.startTime + lessonRequest.endTime)
                .delete()
        } else {
            val dateFormatter: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.YYYY", Locale.getDefault()) }
            data.collection("anotherLessons")
                .document("anotherLessons")
                .collection(dateFormatter.format(lessonRequest.date))

                .document(lessonRequest.startTime + lessonRequest.endTime)
                .delete()
        }
    }
}