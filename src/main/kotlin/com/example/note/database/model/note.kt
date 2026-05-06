package com.example.note.database.model

import org.bson.types.ObjectId
import org.springframework.boot.ssl.pem.PemContent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class note (
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    val ownerId: ObjectId,
    @Id val id: ObjectId= ObjectId.get()
)

