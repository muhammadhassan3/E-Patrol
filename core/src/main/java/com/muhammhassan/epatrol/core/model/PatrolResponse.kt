package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class PatrolResponse(
    @SerializedName("list data Patroli") val list: List<PatrolItemResponse>
)

data class PatrolItemResponse(
    val status: String?,
    @SerializedName("lokasi") val alamat: String,
    @SerializedName("tgl_pelaksanaan") val tglPelaksanaan: String,
    @SerializedName("waktu_mulai") val jam: String,
    @SerializedName("ketua_regu") private val ketuaRaw: UserModel,
    private val verified: Int,
    val id: Long,
    @SerializedName("patroli_id")  val patrolId: Long,
    @SerializedName("no_polisi") val plate: String
) {
    val ketua: String get() = ketuaRaw.email ?: "Gagal memuat user"
    val judul: String
        get() {
            val numberFormatter = DecimalFormat("00000000")
            return "Patroli-${numberFormatter.format((id.toString()+patrolId.toString()).toInt())}"
        }

    val isVerified: Boolean
        get() = verified == 1

    val tanggal: String
        get() {
            return try {
                val responseFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = responseFormat.parse(tglPelaksanaan)
                val resultFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                if (date != null) {
                    resultFormat.format(date)
                }else{
                    "Tanggal tidak tersedia"
                }
            } catch (e: Exception) {
                Timber.e(IllegalArgumentException("Invalid date format pattern"))
                "Gagal memuat tanggal"
            }
        }

    fun getSelisihHari(): Int{
        return try {
            val responseFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = responseFormat.parse(tglPelaksanaan)
            if (date != null) {
                val today = Date()
                val diffInMillis = today.time - date.time
                (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            } else {
                -1 // Invalid date
            }
        } catch (e: Exception) {
            Timber.e(e, "Error calculating days difference")
            -1 // Error case
        }
    }
}