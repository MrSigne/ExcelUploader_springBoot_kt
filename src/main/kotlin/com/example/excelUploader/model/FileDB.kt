package com.example.excelUploader.model

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

data class File(
    val name: String,
    val phones: List<String>
)

@Entity
@Table(name = "people")
class FileDB (
    @Id
    var id: UUID,
    var name: String,
    @Type(type = "string")
    var phones: String
    )



