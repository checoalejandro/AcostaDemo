package com.acostadev.acostademo.ui.livepreview

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.acostadev.acostademo.R
import com.acostadev.acostademo.ui.livepreview.processors.objectdetector.ObjectDetectorProcessor
import com.acostadev.acostademo.ui.livepreview.vision.CameraSource
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import kotlinx.android.synthetic.main.activity_live_preview.*
import java.io.IOException
import java.lang.Exception

class LivePreviewActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private var cameraSource: CameraSource? = null
    private var selectedModel = FACE_CONTOUR
    private val requiredPermissions: Array<String?>
        get() {
            return try {
                val info = this.packageManager
                        .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
                val ps = info.requestedPermissions
                if (ps != null && ps.isNotEmpty()) {
                    ps
                } else {
                    arrayOfNulls(0)
                }
            } catch (e: Exception) {
                arrayOfNulls(0)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_preview)

        if (firePreview == null) {
            Log.d(TAG, "Preview is null")
        }

        if (fireFaceOverlay == null) {
            Log.d(TAG, "graphicOverlay is null")
        }

        if (allPermissionsGranted()) {
            createCameraSource(selectedModel)
        } else {
            getRuntimePermissions()
        }
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, fireFaceOverlay)
        }

        try {
//            when (model) {
//                CLASSIFICATION_QUANT -> {
//                    Log.i(TAG, "Using Custom Image Classifier (quant) Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(
//                            CustomImageClassifierProcessor(
//                                    this,
//                                    true
//                            )
//                    )
//                }
//                CLASSIFICATION_FLOAT -> {
//                    Log.i(TAG, "Using Custom Image Classifier (float) Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(
//                            CustomImageClassifierProcessor(
//                                    this,
//                                    false
//                            )
//                    )
//                }
//                TEXT_DETECTION -> {
//                    Log.i(TAG, "Using Text Detector Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(TextRecognitionProcessor())
//                }
//                FACE_DETECTION -> {
//                    Log.i(TAG, "Using Face Detector Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(FaceDetectionProcessor(resources))
//                }
//                OBJECT_DETECTION -> {
            Log.i(TAG, "Using Object Detector Processor")
            val objectDetectorOptions = FirebaseVisionObjectDetectorOptions.Builder()
                    .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                    .enableClassification()
                    .build()
            cameraSource?.setMachineLearningFrameProcessor(
                    ObjectDetectorProcessor(objectDetectorOptions)
            )
//                }
//                AUTOML_IMAGE_LABELING -> {
//                    cameraSource?.setMachineLearningFrameProcessor(AutoMLImageLabelerProcessor(this))
//                }
//                BARCODE_DETECTION -> {
//                    Log.i(TAG, "Using Barcode Detector Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(BarcodeScanningProcessor())
//                }
//                IMAGE_LABEL_DETECTION -> {
//                    Log.i(TAG, "Using Image Label Detector Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(ImageLabelingProcessor())
//                }
//                FACE_CONTOUR -> {
//                    Log.i(TAG, "Using Face Contour Detector Processor")
//                    cameraSource?.setMachineLearningFrameProcessor(FaceContourDetectorProcessor())
//                }
//                else -> Log.e(TAG, "Unknown model: $model")
//            }
        } catch (e: FirebaseMLException) {
            Log.e(TAG, "can not create camera source: $model")
        }
    }

    private fun startCameraSource() {
        cameraSource?.let {
            try {
                if (firePreview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (fireFaceOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                firePreview?.start(cameraSource!!, fireFaceOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        startCameraSource()
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        firePreview?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission!!)) {
                return false
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = arrayListOf<String>()
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission!!)) {
                allNeededPermissions.add(permission)
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        Log.i(TAG, "Permission granted!")
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val FACE_DETECTION = "Face Detection"
        private const val TEXT_DETECTION = "Text Detection"
        private const val OBJECT_DETECTION = "Object Detection"
        private const val AUTOML_IMAGE_LABELING = "AutoML Vision Edge"
        private const val BARCODE_DETECTION = "Barcode Detection"
        private const val IMAGE_LABEL_DETECTION = "Label Detection"
        private const val CLASSIFICATION_QUANT = "Classification (quantized)"
        private const val CLASSIFICATION_FLOAT = "Classification (float)"
        private const val FACE_CONTOUR = "Face Contour"
        private const val TAG = "LivePreviewActivity"
        private const val PERMISSION_REQUESTS = 1

        private fun isPermissionGranted(context: Context, permission: String): Boolean {
            if (ContextCompat.checkSelfPermission(
                            context,
                            permission
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "Permission granted: $permission")
                return true
            }
            Log.i(TAG, "Permission NOT granted: $permission")
            return false
        }
    }
}
