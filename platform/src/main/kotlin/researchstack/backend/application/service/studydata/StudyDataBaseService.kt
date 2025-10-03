package researchstack.backend.application.service.studydata

import researchstack.backend.application.port.outgoing.storage.DownloadObjectPort
import researchstack.backend.enums.StudyDataFileType
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.util.*
import java.util.zip.GZIPOutputStream

open class StudyDataBaseService(
    private val downloadObjectPort: DownloadObjectPort
) {
    suspend fun getPreview(type: StudyDataFileType, path: String): String? {
        val preview = when (type) {
            StudyDataFileType.RAW_DATA,
            StudyDataFileType.MESSAGE_LOG -> getCSVPreview(path)
            StudyDataFileType.META_INFO -> getMetaInfoPreview(path)
            else -> return null
        } ?: return null

        val gzBytes: ByteArray = ByteArrayOutputStream().use { bos ->
            GZIPOutputStream(bos).use { gzip ->
                OutputStreamWriter(gzip, Charsets.UTF_8).use { writer ->
                    writer.write(preview)
                }
                // ensure trailer is written even if a scanner expects it explicitly
                gzip.finish()
            }
            bos.toByteArray()
        }

        return Base64.getEncoder().encodeToString(gzBytes)
    }

    private suspend fun getCSVPreview(path: String): String? {
        return downloadObjectPort.getPartialObject(path)
            ?.split("\n")
            ?.dropLast(1)
            ?.joinToString("\n")
    }

    private suspend fun getMetaInfoPreview(path: String): String? {
        return downloadObjectPort.getObject(path)
    }
}
