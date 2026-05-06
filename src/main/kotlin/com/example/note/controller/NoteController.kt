package com.example.note.controller

import com.example.note.controller.NoteController.NoteResponse
import com.example.note.database.model.note
import com.example.note.database.repository.NoteRepsitory
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

// POST http://localhost:8085/notes
// GET  http://localhost:8085/notes?ownerId=123
// DELETE http://localhost:8085/notes/123
@RestController
@RequestMapping("/notes")
class NoteController(
    private val repository: NoteRepsitory,
) {

    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long,
    )
    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant,
    )
    @PostMapping
    fun save(
        @RequestBody body: NoteRequest): NoteResponse {
        val note1 = repository.save(
            note(
                id = body.id?.let{ObjectId(it)} ?: ObjectId.get(),
                title = body.title,
                content =body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId()
            )
        )
        return note1.toResponse()
    }
    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String
    ): List<NoteResponse> {
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        repository.deleteById(ObjectId(id))
    }
}

private fun note.toResponse(): NoteController.NoteResponse {
    return NoteResponse(
        id = id.toHexString(),
        title = title,
        content = content,
        color = color,
        createdAt = createdAt,
    )
}