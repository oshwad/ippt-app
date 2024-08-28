package com.faithie.ipptapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.faithie.ipptapp.CameraXViewModel
import com.faithie.ipptapp.GraphicOverlay
import com.faithie.ipptapp.R
import com.faithie.ipptapp.VisionImageProcessor
import com.faithie.ipptapp.posedetector.PoseDetectorProcessor
import com.faithie.ipptapp.preference.PreferenceUtils
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase
import java.util.concurrent.Executor

@KeepName
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CameraXLivePreviewActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private val TAG = "CameraXLivePreview"

    private var previewView: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null

    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false

    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        setContentView(R.layout.activity_vision_camerax_live_preview)
        previewView = findViewById(R.id.preview_view)
        graphicOverlay = findViewById(R.id.graphic_overlay)

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        facingSwitch.setOnCheckedChangeListener(this)

        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(this) { provider ->
                cameraProvider = provider
                bindAllCameraUseCases()
            }

        val settingsButton = findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(
                SettingsActivity.EXTRA_LAUNCH_SOURCE,
                SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW
            )
            startActivity(intent)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        cameraProvider ?: return
        val newLensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()
        try {
            if (cameraProvider!!.hasCamera(newCameraSelector)) {
                Log.d(TAG, "Set facing to $newLensFacing")
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                bindAllCameraUseCases()
            }
        } catch (e: CameraInfoUnavailableException) {
            Toast.makeText(
                applicationContext,
                "This device does not have lens with facing: $newLensFacing",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.stop()
    }

    private fun bindAllCameraUseCases() {
        cameraProvider?.unbindAll()
        bindPreviewUseCase()
        bindAnalysisUseCase()
    }

    private fun bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) return

        cameraProvider ?: return

        previewUseCase?.let {
            cameraProvider?.unbind(it)
        }

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        targetResolution?.let {
            builder.setTargetResolution(it)
        }
        previewUseCase = builder.build().apply {
            setSurfaceProvider(previewView?.surfaceProvider)
        }

        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, previewUseCase)
    }

    private fun bindAnalysisUseCase() {
        cameraProvider ?: return

        analysisUseCase?.let {
            cameraProvider?.unbind(it)
        }
        imageProcessor?.stop()

        try {
            val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
            val shouldShowInFrameLikelihood = PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
            val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
            val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
            val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)

            imageProcessor = PoseDetectorProcessor(
                this,
                poseDetectorOptions,
                shouldShowInFrameLikelihood,
                visualizeZ,
                rescaleZ,
                runClassification,
                true
            )

        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: ${e.localizedMessage}", e)
            Toast.makeText(
                applicationContext,
                "Can not create image processor: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        targetResolution?.let {
            builder.setTargetResolution(it)
        }
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true
        analysisUseCase?.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            { imageProxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        graphicOverlay?.setImageSourceInfo(
                            imageProxy.width, imageProxy.height, isImageFlipped
                        )
                    } else {
                        graphicOverlay?.setImageSourceInfo(
                            imageProxy.height, imageProxy.width, isImageFlipped
                        )
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false
                }
                try {
                    imageProcessor?.processImageProxy(imageProxy, graphicOverlay)
                } catch (e: MlKitException) {
                    Log.e(TAG, "Failed to process image. Error: ${e.localizedMessage}")
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )

        cameraProvider?.bindToLifecycle(this, cameraSelector, analysisUseCase)
    }
}
