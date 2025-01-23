package com.dwan.ta_pam.model

import kotlinx.serialization.Serializable

@Serializable
data class Pasien(
    val id_pasien: String,
    val nama_pasien: String,
    val alamat: String,
    val nomor_telepon: String,
    val tanggal_lahir: String,
    val riwayat_medikal: String,
)

@Serializable
data class Terapis(
    val id_terapis: String,
    val nama_terapis: String,
    val spesialisasi: String,
    val nomor_izin_praktik: String
)

@Serializable
data class JenisTerapi(
    val id_jenis_terapi: String,
    val nama_jenis_terapi: String,
    val deskripsi_terapi: String
)

@Serializable
data class SesiTerapi(
    val id_sesi: String,
    val id_pasien: String,
    val id_terapis: String,
    val id_jenis_terapi: String,
    val tanggal_sesi: String,
    val catatan_sesi: String,
)