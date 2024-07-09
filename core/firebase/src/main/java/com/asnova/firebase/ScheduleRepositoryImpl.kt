package com.asnova.firebase

import com.asnova.domain.repository.firebase.ScheduleRepository
import com.asnova.model.Resource
import com.asnova.model.Schedule
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor() : ScheduleRepository {
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _databaseReference: CollectionReference = _database.collection("schedule")
    override fun addNewLesson(schedule: Schedule, callback: (Resource<Boolean>) -> Unit) {
        callback(Resource.Loading())
        val id = _databaseReference.document().id

        val lesson = com.asnova.firebase.model.Schedule(
            id = id,
            date = Timestamp(
                Date.from(
                    schedule.date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                )
            ),
            start = Timestamp(
                Date.from(
                    LocalDate.now().atTime(schedule.start).atZone(ZoneId.systemDefault())
                        .toInstant()
                )
            ),
            end = Timestamp(
                Date.from(
                    LocalDate.now().atTime(schedule.end).atZone(ZoneId.systemDefault()).toInstant()
                )
            ),
            lesson = schedule.lesson,
            status = schedule.status,
            classRoom = schedule.classRoom,
            teacher = schedule.teacher,
            grade = schedule.grade,
            task = schedule.task,
            homeWork = schedule.homeWork
        )
        _databaseReference.document(id).set(lesson).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(Resource.Success(true))
            } else {
                callback(Resource.Success(false))
            }
        }.addOnFailureListener {
            callback(Resource.Error(it.message.toString()))
        }
    }

    override fun getAllSchedule(callback: (Resource<List<Schedule>>) -> Unit) {
        callback(Resource.Loading())
        _databaseReference.get().addOnSuccessListener { snapshot ->
            if (!snapshot.isEmpty) {
                val scheduleList = mutableListOf<Schedule>()
                snapshot.forEach {
                    val temp = it.toObject(com.asnova.firebase.model.Schedule::class.java)
                    val schedule = Schedule(
                        id = temp.id,
                        date = LocalDateTime.ofInstant(
                            temp.date.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalDate(),
                        start = LocalDateTime.ofInstant(
                            temp.start.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalTime(),
                        end = LocalDateTime.ofInstant(
                            temp.end.toDate().toInstant(),
                            ZoneId.systemDefault()
                        ).toLocalTime(),
                        lesson = temp.lesson,
                        status = temp.status,
                        classRoom = temp.classRoom,
                        teacher = temp.teacher,
                        grade = temp.grade,
                        task = temp.task,
                        homeWork = temp.homeWork
                    )
                    scheduleList.add(schedule)
                }
                callback(Resource.Success(scheduleList))
            } else {
                callback(Resource.Success(emptyList()))
            }
        }.addOnFailureListener {
            callback(Resource.Error(it.message.toString()))
        }
    }
}