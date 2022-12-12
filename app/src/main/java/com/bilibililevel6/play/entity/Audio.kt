package com.bilibililevel6.play.entity

data class Audio(
    val SegmentBase: SegmentBase,
    val backupUrl: List<String>,
    val backup_url: List<String>,
    val bandwidth: Int,
    val baseUrl: String,
    val base_url: String,
    val codecid: Int,
    val codecs: String,
    val frameRate: String,
    val frame_rate: String,
    val height: Int,
    val id: Int,
    val mimeType: String,
    val mime_type: String,
    val sar: String,
    val segment_base: SegmentBaseX,
    val startWithSap: Int,
    val start_with_sap: Int,
    val width: Int
)