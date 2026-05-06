package com.example.note.database.repository

import com.example.note.database.model.note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface NoteRepsitory: MongoRepository<note, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<note>
}