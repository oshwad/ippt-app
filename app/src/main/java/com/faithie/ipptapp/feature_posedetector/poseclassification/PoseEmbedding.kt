package com.faithie.ipptapp.feature_posedetector.poseclassification

import com.faithie.ipptapp.feature_posedetector.poseclassification.Utils.average
import com.faithie.ipptapp.feature_posedetector.poseclassification.Utils.l2Norm2D
import com.faithie.ipptapp.feature_posedetector.poseclassification.Utils.multiplyAll
import com.faithie.ipptapp.feature_posedetector.poseclassification.Utils.subtract
import com.faithie.ipptapp.feature_posedetector.poseclassification.Utils.subtractAll

import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.PoseLandmark

/**
 * Generates embedding for given list of Pose landmarks.
 */
object PoseEmbedding { // object: singleton class

    private const val TORSO_MULTIPLIER = 2.5f

    fun getPoseEmbedding(landmarks: List<PointF3D>): List<PointF3D> {
        val normalizedLandmarks = normalize(landmarks)
        return getEmbedding(normalizedLandmarks)
    }

    private fun normalize(landmarks: List<PointF3D>): List<PointF3D> {
        val normalizedLandmarks = ArrayList(landmarks)
        val center = average(
            landmarks[PoseLandmark.LEFT_HIP],
            landmarks[PoseLandmark.RIGHT_HIP]
        )
        subtractAll(center, normalizedLandmarks)

        val scale = 1 / getPoseSize(normalizedLandmarks)
        multiplyAll(normalizedLandmarks, scale * 100)
        return normalizedLandmarks
    }

    private fun getPoseSize(landmarks: List<PointF3D>): Float {
        val hipsCenter = average(
            landmarks[PoseLandmark.LEFT_HIP],
            landmarks[PoseLandmark.RIGHT_HIP]
        )

        val shouldersCenter = average(
            landmarks[PoseLandmark.LEFT_SHOULDER],
            landmarks[PoseLandmark.RIGHT_SHOULDER]
        )

        val torsoSize = l2Norm2D(subtract(hipsCenter, shouldersCenter))

        var maxDistance = torsoSize * TORSO_MULTIPLIER
        for (landmark in landmarks) {
            val distance = l2Norm2D(subtract(hipsCenter, landmark))
            maxDistance = maxOf(maxDistance, distance)
        }
        return maxDistance
    }

    private fun getEmbedding(lm: List<PointF3D>): List<PointF3D> {
        val embedding: MutableList<PointF3D> = ArrayList()

        // We use several pairwise 3D distances to form pose embedding. These were selected
        // based on experimentation for best results with our default pose classes as captured in the
        // pose samples csv. Feel free to play with this and add or remove for your use-cases.

        // We group our distances by number of joints between the pairs.
        // One joint.
        embedding.add(
            subtract(
                average(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.RIGHT_HIP]),
                average(lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.RIGHT_SHOULDER])
            )
        )
        embedding.add(
            subtract(
                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_ELBOW]
            )
        )
        embedding.add(
            subtract(
                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_ELBOW]
            )
        )
        embedding.add(subtract(lm[PoseLandmark.LEFT_ELBOW], lm[PoseLandmark.LEFT_WRIST]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_ELBOW], lm[PoseLandmark.RIGHT_WRIST]))
        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_KNEE]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_KNEE]))
        embedding.add(subtract(lm[PoseLandmark.LEFT_KNEE], lm[PoseLandmark.LEFT_ANKLE]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_KNEE], lm[PoseLandmark.RIGHT_ANKLE]))

        // Two joints.
        embedding.add(
            subtract(
                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_WRIST]
            )
        )
        embedding.add(
            subtract(
                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_WRIST]
            )
        )
        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_ANKLE]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_ANKLE]))

        // Four joints.
        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_WRIST]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_WRIST]))

        // Five joints.
        embedding.add(
            subtract(
                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_ANKLE]
            )
        )
        embedding.add(
            subtract(
                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_ANKLE]
            )
        )
        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_WRIST]))
        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_WRIST]))

        // Cross body.
        embedding.add(subtract(lm[PoseLandmark.LEFT_ELBOW], lm[PoseLandmark.RIGHT_ELBOW]))
        embedding.add(subtract(lm[PoseLandmark.LEFT_KNEE], lm[PoseLandmark.RIGHT_KNEE]))
        embedding.add(subtract(lm[PoseLandmark.LEFT_WRIST], lm[PoseLandmark.RIGHT_WRIST]))
        embedding.add(subtract(lm[PoseLandmark.LEFT_ANKLE], lm[PoseLandmark.RIGHT_ANKLE]))
        return embedding
    }
}

//    // pasted from java code and automatically converted to kotlin
//
//object PoseEmbedding {
//    // Multiplier to apply to the torso to get minimal body size. Picked this by experimentation.
//    private const val TORSO_MULTIPLIER = 2.5f
//    fun getPoseEmbedding(landmarks: List<PointF3D?>?): List<PointF3D> {
//        val normalizedLandmarks: List<PointF3D> =
//            normalize(landmarks)
//        return com.google.mlkit.vision.demo.java.posedetector.classification.PoseEmbedding.getEmbedding(
//            normalizedLandmarks
//        )
//    }
//
//    private fun normalize(landmarks: List<PointF3D>): List<PointF3D> {
//        val normalizedLandmarks: List<PointF3D> = ArrayList(landmarks)
//        // Normalize translation.
//        val center: PointF3D = average(
//            landmarks[PoseLandmark.LEFT_HIP], landmarks[PoseLandmark.RIGHT_HIP]
//        )
//        subtractAll(center, normalizedLandmarks)
//
//        // Normalize scale.
//        multiplyAll(
//            normalizedLandmarks,
//            1 / com.google.mlkit.vision.demo.java.posedetector.classification.PoseEmbedding.getPoseSize(
//                normalizedLandmarks
//            )
//        )
//        // Multiplication by 100 is not required, but makes it easier to debug.
//        multiplyAll(normalizedLandmarks, 100)
//        return normalizedLandmarks
//    }
//
//    // Translation normalization should've been done prior to calling this method.
//    private fun getPoseSize(landmarks: List<PointF3D>): Float {
//        // Note: This approach uses only 2D landmarks to compute pose size as using Z wasn't helpful
//        // in our experimentation but you're welcome to tweak.
//        val hipsCenter: PointF3D = average(
//            landmarks[PoseLandmark.LEFT_HIP], landmarks[PoseLandmark.RIGHT_HIP]
//        )
//        val shouldersCenter: PointF3D = average(
//            landmarks[PoseLandmark.LEFT_SHOULDER],
//            landmarks[PoseLandmark.RIGHT_SHOULDER]
//        )
//        val torsoSize: Float = l2Norm2D(subtract(hipsCenter, shouldersCenter))
//        var maxDistance: Float =
//            torsoSize * com.google.mlkit.vision.demo.java.posedetector.classification.PoseEmbedding.TORSO_MULTIPLIER
//        // torsoSize * TORSO_MULTIPLIER is the floor we want based on experimentation but actual size
//        // can be bigger for a given pose depending on extension of limbs etc so we calculate that.
//        for (landmark in landmarks) {
//            val distance: Float = l2Norm2D(subtract(hipsCenter, landmark))
//            if (distance > maxDistance) {
//                maxDistance = distance
//            }
//        }
//        return maxDistance
//    }
//
//    private fun getEmbedding(lm: List<PointF3D>): List<PointF3D> {
//        val embedding: MutableList<PointF3D> = ArrayList()
//
//        // We use several pairwise 3D distances to form pose embedding. These were selected
//        // based on experimentation for best results with our default pose classes as captured in the
//        // pose samples csv. Feel free to play with this and add or remove for your use-cases.
//
//        // We group our distances by number of joints between the pairs.
//        // One joint.
//        embedding.add(
//            subtract(
//                average(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.RIGHT_HIP]),
//                average(lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.RIGHT_SHOULDER])
//            )
//        )
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_ELBOW]
//            )
//        )
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_ELBOW]
//            )
//        )
//        embedding.add(subtract(lm[PoseLandmark.LEFT_ELBOW], lm[PoseLandmark.LEFT_WRIST]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_ELBOW], lm[PoseLandmark.RIGHT_WRIST]))
//        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_KNEE]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_KNEE]))
//        embedding.add(subtract(lm[PoseLandmark.LEFT_KNEE], lm[PoseLandmark.LEFT_ANKLE]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_KNEE], lm[PoseLandmark.RIGHT_ANKLE]))
//
//        // Two joints.
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_WRIST]
//            )
//        )
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_WRIST]
//            )
//        )
//        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_ANKLE]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_ANKLE]))
//
//        // Four joints.
//        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_WRIST]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_WRIST]))
//
//        // Five joints.
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.LEFT_SHOULDER], lm[PoseLandmark.LEFT_ANKLE]
//            )
//        )
//        embedding.add(
//            subtract(
//                lm[PoseLandmark.RIGHT_SHOULDER], lm[PoseLandmark.RIGHT_ANKLE]
//            )
//        )
//        embedding.add(subtract(lm[PoseLandmark.LEFT_HIP], lm[PoseLandmark.LEFT_WRIST]))
//        embedding.add(subtract(lm[PoseLandmark.RIGHT_HIP], lm[PoseLandmark.RIGHT_WRIST]))
//
//        // Cross body.
//        embedding.add(subtract(lm[PoseLandmark.LEFT_ELBOW], lm[PoseLandmark.RIGHT_ELBOW]))
//        embedding.add(subtract(lm[PoseLandmark.LEFT_KNEE], lm[PoseLandmark.RIGHT_KNEE]))
//        embedding.add(subtract(lm[PoseLandmark.LEFT_WRIST], lm[PoseLandmark.RIGHT_WRIST]))
//        embedding.add(subtract(lm[PoseLandmark.LEFT_ANKLE], lm[PoseLandmark.RIGHT_ANKLE]))
//        return embedding
//    }
//}