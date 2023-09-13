package com.muhammhassan.epatrol.core.model

import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class PatrolDetailResponse(
    val status: String,
    @SerializedName("lokasi") val alamat: String,
    @SerializedName("tgl_pelaksanaan") private val tglPelaksanaan: String,
    @SerializedName("waktu_mulai") val jam: String,
    @SerializedName("email") val ketua: String,
    private val verified: Int,
    val id: Long,
    @SerializedName("no_polisi") val plate: String,
    @SerializedName("no_sprin") private val rawSprin: Int
) {
    val judul: String
        get() {
            val numberFormatter = DecimalFormat("00000000")
            return "Patroli-${numberFormatter.format(id)}"
        }

    val sprin: String
        get(){
            val calendar = Calendar.getInstance()
            return "SPRIN/$rawSprin/IV/PAM.5.1.1./${calendar[Calendar.YEAR]}"
        }

    val tanggal: String
        get() {
            return try {
                val responseFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = responseFormat.parse(tglPelaksanaan)
                val resultFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                if (date != null) {
                    resultFormat.format(date)
                }else throw IllegalArgumentException("Date cannot null!!")
            } catch (e: Exception) {
                Timber.e(e.message)
                "Gagal memuat tanggal"
            }
        }

}