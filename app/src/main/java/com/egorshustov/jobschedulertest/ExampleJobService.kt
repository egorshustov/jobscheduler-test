package com.egorshustov.jobschedulertest

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class ExampleJobService : JobService() {
    private var jobCancelled: Boolean = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)
        // Return true, if job needs to continue running.
        // The job remains active until we call jobFinished(JobParameters, boolean):
        return true
    }

    private fun doBackgroundWork(params: JobParameters?) {
        Thread(Runnable {
            for (i in 0..9) {
                Log.d(TAG, "run: $i")
                if (jobCancelled) {
                    return@Runnable
                }

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            Log.d(TAG, "Job finished")
            jobFinished(params, false)
        }).start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true  // true, if we want to reschedule our job
    }

    companion object {
        private const val TAG = "ExampleJobService"
    }
}