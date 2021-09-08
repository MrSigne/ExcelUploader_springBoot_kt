package com.example.excelUploader.model

import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.*
import javax.persistence.*

data class File(
    val name: String,
    val phones: List<String>
)
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)

@Entity
@Table(name = "people")
class FileDB(
    @Id
    var id: UUID,
    var name: String,
    @Type(type = "string-array")
    @Column(name = "phones")
    var phones: Array<String>? = arrayOf(),
    var address: String,
    @Column(name = "dob")
    var dob: Date,
    var age: Int?,
)



