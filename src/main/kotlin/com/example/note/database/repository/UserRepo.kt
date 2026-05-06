package com.example.note.database.repository

import com.example.note.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?
}