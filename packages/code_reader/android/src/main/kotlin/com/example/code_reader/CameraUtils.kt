package com.example.code_reader

import android.media.CamcorderProfile
import android.util.Size

enum class ResolutionPreset {
    low, medium, high, veryHigh, ultraHigh, max
}


class CameraUtils {

    companion object {
        fun computeBestPreviewSize(cameraName: String, rp: ResolutionPreset): Size {
            var preset: ResolutionPreset = rp
            if (preset.ordinal > ResolutionPreset.high.ordinal) {
                preset = ResolutionPreset.high
            }
            val profile: CamcorderProfile = getBestAvailableCamcorderProfileForResolutionPreset(cameraName, preset)
            return Size(profile.videoFrameWidth, profile.videoFrameHeight)
        }

        fun getBestAvailableCamcorderProfileForResolutionPreset(
                cameraName: String, preset: ResolutionPreset?): CamcorderProfile {
            val cameraId = cameraName.toInt()
            return when (preset) {
                ResolutionPreset.max -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_HIGH)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_2160P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_2160P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                ResolutionPreset.ultraHigh -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_2160P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_2160P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                ResolutionPreset.veryHigh -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                ResolutionPreset.high -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                ResolutionPreset.medium -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                ResolutionPreset.low -> {
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_QVGA)) {
                        return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_QVGA)
                    }
                    if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                        CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                    } else {
                        throw IllegalArgumentException(
                                "No capture session available for current capture session.")
                    }
                }
                else -> if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_LOW)) {
                    CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW)
                } else {
                    throw IllegalArgumentException(
                            "No capture session available for current capture session.")
                }
            }
        }
    }

}