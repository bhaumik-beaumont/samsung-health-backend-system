package researchstack.backend.adapter.outgoing.mongo.entity.healthdata

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp

@Document("userCompliance")
data class UserCompliance(
    @Id
    override val id: String? = null,
    override val subjectId: String,
    val weekNumber: Int,
    val startDate: String,
    val endDate: String,
    val totalActivityMinutes: Int,
    val resistanceSessionCount: Int,
    val biaRecordCount: Int,
    val weightRecordCount: Int,
    val timestamp: Timestamp,
    val avgWeight: Float = 0F,
    @JsonProperty("time_offset")
    val timeOffset: String? = null
) : HealthDataEntity
