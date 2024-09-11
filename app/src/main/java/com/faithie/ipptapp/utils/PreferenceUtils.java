package com.faithie.ipptapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.StringRes;

import com.faithie.ipptapp.R;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

/** Utility class to retrieve shared preferences. */
public class PreferenceUtils {

    private static final int POSE_DETECTOR_PERFORMANCE_MODE_FAST = 1;

    public static boolean shouldHideDetectionInfo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(R.string.pref_key_info_hide);
        return sharedPreferences.getBoolean(prefKey, false);
    }

    public static boolean isCameraLiveViewportEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(R.string.pref_key_camera_live_viewport);
        return sharedPreferences.getBoolean(prefKey, false);
    }

    public static PoseDetectorOptionsBase getPoseDetectorOptionsForLivePreview(Context context) {
        int performanceMode =
                getModeTypePreferenceValue(
                        context,
                        Integer.parseInt("pref_key_live_preview_pose_detection_performance_mode"),
                        POSE_DETECTOR_PERFORMANCE_MODE_FAST);
        boolean preferGPU = preferGPUForPoseDetection(context);
        if (performanceMode == POSE_DETECTOR_PERFORMANCE_MODE_FAST) {
            PoseDetectorOptions.Builder builder =
                    new PoseDetectorOptions.Builder().setDetectorMode(PoseDetectorOptions.STREAM_MODE);
            if (preferGPU) {
                builder.setPreferredHardwareConfigs(PoseDetectorOptions.CPU_GPU);
            }
            return builder.build();
        } else {
            AccuratePoseDetectorOptions.Builder builder =
                    new AccuratePoseDetectorOptions.Builder()
                            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE);
            if (preferGPU) {
                builder.setPreferredHardwareConfigs(AccuratePoseDetectorOptions.CPU_GPU);
            }
            return builder.build();
        }
    }

    private static int getModeTypePreferenceValue(
            Context context, @StringRes int prefKeyResId, int defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(prefKeyResId);
        return Integer.parseInt(sharedPreferences.getString(prefKey, String.valueOf(defaultValue)));
    }

    public static boolean preferGPUForPoseDetection(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(Integer.parseInt("pref_key_pose_detector_prefer_gpu"));
        return sharedPreferences.getBoolean(prefKey, true);
    }

    public static boolean shouldShowPoseDetectionInFrameLikelihoodLivePreview(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey =
                context.getString(Integer.parseInt("pref_key_live_preview_pose_detector_show_in_frame_likelihood"));
        return sharedPreferences.getBoolean(prefKey, true);
    }

    public static boolean shouldPoseDetectionVisualizeZ(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(Integer.parseInt("pref_key_pose_detector_visualize_z"));
        return sharedPreferences.getBoolean(prefKey, true);
    }

    public static boolean shouldPoseDetectionRescaleZForVisualization(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(Integer.parseInt("pref_key_pose_detector_rescale_z"));
        return sharedPreferences.getBoolean(prefKey, true);
    }

    public static boolean shouldPoseDetectionRunClassification(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(Integer.parseInt("pref_key_pose_detector_run_classification"));
        return sharedPreferences.getBoolean(prefKey, false);
    }
}
