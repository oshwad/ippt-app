package com.faithie.ipptapp.model.posedetector.classification

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream

class PoseAssetReader(context: Context) {
    private val TAG = "PoseAssetReader"
    private val poseDetector: PoseDetector
    private val posesCsvFile: File
    init {
        // Initialize the pose detector
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        poseDetector = PoseDetection.getClient(options)

        posesCsvFile = File(context.getExternalFilesDir(null), "poses.csv")
        if (!posesCsvFile.exists()) {
            Log.d(TAG, "poses.csv file does not exist")
            posesCsvFile.createNewFile()
            Log.d(TAG, "poses.csv file created")
        }
    }

    fun deletePosesCsvFile() {
        if(posesCsvFile.exists()) {
            Log.d(TAG, "poses.csv file exists")
            posesCsvFile.delete()
            Log.d(TAG, "poses.csv file deleted")
        }
    }

    fun readPoseAssets(context: Context) {
        val assetManager: AssetManager = context.assets

        try {
            // List all the directories in the assets folder (e.g., "pushup_down", "pushup_up")
            val poseDirectories = assetManager.list("") // Empty string to list root assets

            poseDirectories?.forEach { directory ->
                if (isPoseDirectory(directory)) {
                    Log.d(TAG, "Pose Directory: $directory")

                    // List all images in the current pose directory
                    val poseImages = assetManager.list(directory)

                    poseImages?.forEach { image ->
                        // Concatenate the label with the image filename
                        val imagePath = "$directory/$image"
                        Log.d(TAG, "Image: $imagePath")

                        // Load the image as a Bitmap and process it to extract pose landmarks
                        val bitmap = loadImageFromAssets(context, imagePath)
                        bitmap?.let {
                            processImageForPose(it, directory, image)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading assets.", e)
        }
    }

    private fun processImageForPose(bitmap: Bitmap, label: String, imageName: String) {
        val augmentedImages =  augmentImage(bitmap)

        augmentedImages.forEachIndexed { index, augmentedBitmap ->
            // rotation degrees for a bitmap is just 0 (unlike image proxy)
            val inputImage = InputImage.fromBitmap(augmentedBitmap, 0)
            val augmentedImageName = "${imageName}_aug_$index"

            poseDetector.process(inputImage)
                .addOnSuccessListener { pose ->
                    // Extract pose landmarks and save to CSV
                    savePoseLandmarksToCsv(pose, label, augmentedImageName)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Pose detection failed for image: $augmentedImageName", e)
                }
        }
    }

    private fun savePoseLandmarksToCsv(pose: Pose, label: String, imageName: String) {
        if (isImageAlreadyProcessed(imageName)) {
            Log.d(TAG, "Image $imageName already processed, skipping.")
            return
        }

        try {
            val dataRow = StringBuilder("")
            val landmarks: List<PoseLandmark> = pose.allPoseLandmarks
            for (landmark in landmarks) {
                if (landmark != null) {
                    val position3D: PointF3D = landmark.position3D
                    dataRow.append(",${position3D.x},${position3D.y},${position3D.z}")
                } else {
                    // If landmark is not detected, fill with default values (e.g., 0)
                    dataRow.append(",0,0,0")
                }
            }
            if (dataRow.isEmpty()) {
                Log.d(TAG, "$label $imageName has empty data row")
                Log.d(TAG, "pose landmarks: $landmarks, pose landmarks to string: ${landmarks.toString()}")
            }

            val fullRow = "$imageName,$label$dataRow"
            FileWriter(posesCsvFile, true).use { writer ->
                writer.append(fullRow).append("\n")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving landmarks to CSV.", e)
        }
    }

    // Helper function to determine if a directory is a valid pose directory
    private fun isPoseDirectory(directoryName: String): Boolean {
        // Example logic to match pose directories
        return directoryName.startsWith("pushup") || directoryName.startsWith("situp")
    }

    // Load an image as bitmap from the assets folder
    private fun loadImageFromAssets(context: Context, assetPath: String): Bitmap? {
        val assetManager: AssetManager = context.assets
        return try {
            val inputStream: InputStream = assetManager.open(assetPath)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Log.e(TAG, "Error loading image: $assetPath", e)
            null
        }
    }

    private fun isImageAlreadyProcessed(imageName: String): Boolean {
        if (!posesCsvFile.exists()) return false

        return try {
            val processedImages = mutableSetOf<String>()
            posesCsvFile.forEachLine { line ->
                val columns = line.split(",")
                if (columns.isNotEmpty()) {
                    processedImages.add(columns[0]) // The first column is the imageName
                }
            }
            processedImages.contains(imageName)
        } catch (e: IOException) {
            Log.e(TAG, "Error reading CSV file.", e)
            false
        }
    }

    private fun augmentImage(bitmap: Bitmap): List<Bitmap> {
        val augmentedImages = mutableListOf<Bitmap>()

        // Original image
        augmentedImages.add(bitmap)

        // Flip horizontally
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        val flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        augmentedImages.add(flippedBitmap)

        // Rotate image
        val degrees = listOf(-10f, 10f)  // Rotate by -10 and +10 degrees
        degrees.forEach { degree ->
            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(degree)
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, rotateMatrix, true)
            augmentedImages.add(rotatedBitmap)
        }

        // Scale image (e.g., 90% and 110% of the original size)
        val scales = listOf(0.9f, 1.1f)
        scales.forEach { scale ->
            val scaleMatrix = Matrix()
            scaleMatrix.postScale(scale, scale)
            val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, scaleMatrix, true)
            augmentedImages.add(scaledBitmap)
        }

        // Adjust brightness (darker and brighter)
        val brightnessLevels = listOf(-50, 50) // Decrease and increase brightness
        brightnessLevels.forEach { brightness ->
            val brightnessBitmap = adjustBrightness(bitmap, brightness)
            augmentedImages.add(brightnessBitmap)
        }

        // Add noise
        val noisyBitmap = addNoise(bitmap)
        augmentedImages.add(noisyBitmap)

        return augmentedImages
    }

    private fun adjustBrightness(bitmap: Bitmap, brightness: Int): Bitmap {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(bmp)
        val paint = android.graphics.Paint()
        val filter = android.graphics.ColorMatrix()
        filter.set(floatArrayOf(
            1f, 0f, 0f, 0f, brightness.toFloat(),
            0f, 1f, 0f, 0f, brightness.toFloat(),
            0f, 0f, 1f, 0f, brightness.toFloat(),
            0f, 0f, 0f, 1f, 0f
        ))
        val colorMatrixFilter = android.graphics.ColorMatrixColorFilter(filter)
        paint.colorFilter = colorMatrixFilter
        canvas.drawBitmap(bmp, 0f, 0f, paint)
        return bmp
    }

    private fun addNoise(bitmap: Bitmap): Bitmap {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val random = java.util.Random()

        for (x in 0 until bmp.width) {
            for (y in 0 until bmp.height) {
                val pixel = bmp.getPixel(x, y)

                // Add random noise to each channel (R, G, B)
                val r = (android.graphics.Color.red(pixel) + random.nextInt(50) - 25).coerceIn(0, 255)
                val g = (android.graphics.Color.green(pixel) + random.nextInt(50) - 25).coerceIn(0, 255)
                val b = (android.graphics.Color.blue(pixel) + random.nextInt(50) - 25).coerceIn(0, 255)

                bmp.setPixel(x, y, android.graphics.Color.rgb(r, g, b))
            }
        }
        return bmp
    }

}