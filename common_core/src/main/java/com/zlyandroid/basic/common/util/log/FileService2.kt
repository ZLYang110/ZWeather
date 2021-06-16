package com.zlyandroid.basic.common.util.log

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.zlyandroid.basic.common.util.log.FileLog.ensureDir
import com.zlyandroid.basic.common.util.log.FileLog.ensureFile
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Thread.UncaughtExceptionHandler
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit

object FileService2 {
      val TAG = "FileService"
      var mQueue: BlockingQueue<LogData> = LinkedBlockingDeque()
    var mIsRunning = false
    private val FILE_COMPARATOR =
        java.util.Comparator<File> { o1, o2 ->
            val lm1 = o1.lastModified()
            val lm2 = o2.lastModified()
            if (lm1 < lm2) -1 else if (lm1 == lm2) 0 else 1
        }

    fun instance(): FileService2 {
        return InstanceHolder.INSTANCE
    }

    internal object InstanceHolder {
        val INSTANCE = FileService2
    }




    fun logFile(
        context: Context?, fileName: String?, dirPath: String?, line: String?,
        retentionPolicy: Int, maxFileCount: Int, maxTotalSize: Long, flush: Boolean
    ) {

        ensureThread()
        val addResult = mQueue.offer(LogData.Builder().context(context)
            .fileName(fileName)
            .dirPath(dirPath)
            .line(line)
            .retentionPolicy(retentionPolicy)
            .maxFileCount(maxFileCount)
            .maxSize(maxTotalSize)
            .flush(flush)
            .build())
        if (!addResult) {
            Log.i(TAG, "failed to add to file logger service queue")
        }
    }
    private fun ensureThread() {
        if (!mIsRunning) {
            synchronized(this) {
                if (!mIsRunning) {
                    mIsRunning = true
                    Log.i(TAG, "start file logger service thread")
                     LogFileThread().start()
                }
            }
        }
    }


    private class LogFileThread : Thread() {

        private var mWriter: BufferedWriter? = null
        private var mPath: String? = null
        private var mRetentionPolicy = 0
        private var mMaxFileCount = 0
        private var mMaxSize: Long = 0
        var logFileDir: File? = null
        var patho: String? = null
        override fun run() {
            super.run()
            currentThread().uncaughtExceptionHandler =
                UncaughtExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                    mIsRunning = false
                }
            try {
                while (true) {
                    var log: LogData = mQueue.take()
                    logLine(log)
                    collectParams(log)
                    while (mQueue.poll(2, TimeUnit.SECONDS).let ({ log = it }) != null) {
                        logLine(log)
                        collectParams(log)
                    }
                    closeWriter()
                    startHouseKeeping()
                }
            } catch (e: InterruptedException) {
                Log.e(TAG, "file logger service thread is interrupted")
            }
            Log.i(TAG, "file logger service thread stopped")
            mIsRunning = false
        }

        private fun collectParams(log: LogData) {
            mRetentionPolicy = log.retentionPolicy
            mMaxFileCount = log.maxFileCount
            mMaxSize = log.maxTotalSize
        }

        private fun logLine(log: LogData) {
            check(!TextUtils.isEmpty(log.fileName)) { "invalid file name: [" + log.fileName + "]" }
            check(!TextUtils.isEmpty(log.dirPath)) { "invalid directory path: [" + log.dirPath + "]" }
            if (TextUtils.isEmpty(log.line)) {
                return
            }
            val dir = File(log.dirPath!!)
            if (!ensureDir(dir)) {
                dir.mkdirs()
            }
            logFileDir = File(log.dirPath, log.fileName!!)
            if (!logFileDir!!.exists()) {
                try {
                    logFileDir!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            patho = logFileDir!!.absolutePath
            if (mWriter != null && patho == mPath) {
                try {
                    mWriter!!.write(log.line!!)
                    mWriter!!.write("\n")
                    if (log.flush) {
                        mWriter!!.flush()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.message!!)

                }
            } else {
                closeWriter()
                ensureFile(logFileDir!!)
                try {
                    mWriter = createWriter(logFileDir!!)
                    mPath = logFileDir!!.absolutePath
                    mWriter!!.write(log.line!!)
                    mWriter!!.write("\n")
                    if (log.flush) {
                        mWriter!!.flush()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, e.message!!)
                }
            }
        }

        @Throws(IOException::class)
        private fun createWriter(file: File): BufferedWriter {
            // one line ~100 characters = ~100-400 bytes
            // use default buf size ~8k = ~20-80 lines
            return BufferedWriter(FileWriter(file, true))
        }

        private fun startHouseKeeping() {
            if (TextUtils.isEmpty(mPath)) {
                return
            }
            if (mRetentionPolicy == 1) {
                houseKeepByCount(mMaxFileCount)
            } else if (mRetentionPolicy == 2) {
                houseKeepBySize(mMaxSize)
            }
        }

        private fun houseKeepByCount(maxCount: Int) {
            check(maxCount > 0) { "invalid max file count: $maxCount" }
            val file = File(mPath)
            val dir = file.parentFile ?: return
            val files = dir.listFiles()
            if (files == null || files.size <= maxCount) {
                return
            }
            Arrays.sort(files, FILE_COMPARATOR)
            val deleteCount = files.size - maxCount
            var successCount = 0
            for (i in 0 until deleteCount) {
                if (files[i].delete()) {
                    successCount++
                }
            }
            // Log.d(TAG,"house keeping complete: file count ["+files.length+" ->"+files.length - successCount+"]" +"2222");
        }

        private fun houseKeepBySize(maxSize: Long) {
            check(maxSize > 0) { "invalid max total size: $maxSize" }
            val file = File(mPath!!)
            val dir = file.parentFile ?: return
            val files = dir.listFiles() ?: return
            var totalSize: Long = 0
            for (f in files) {
                totalSize += f.length()
            }
            if (totalSize <= maxSize) {
                return
            }
            Arrays.sort(files, FILE_COMPARATOR)
            var newSize = totalSize
            for (f in files) {
                val size = f.length()
                if (f.delete()) {
                    newSize -= size
                    if (newSize <= maxSize) {
                        break
                    }
                }
            }
            Log.d(TAG,
                "house keeping complete: total size [$totalSize -> $newSize]")
        }

        private fun closeWriter() {
            if (mWriter != null) {
                try {
                    mWriter!!.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.message!!)
                }
                mWriter = null
            }
        }
    }
    class LogData private constructor(b: Builder) {
        val context: Context?
        val fileName: String?
        val dirPath: String?
        val line: String?
        val retentionPolicy: Int
        val maxFileCount: Int
        var maxTotalSize: Long
        var flush: Boolean

        internal class Builder {
            var context: Context? = null
            var fileName: String? = null
            var dirPath: String? = null
            var line: String? = null
            var retentionPolicy = 0
            var maxFileCount = 0
            var maxTotalSize: Long = 0
            var flush = false
            fun context(context: Context?): Builder {
                this.context = context
                return this
            }

            fun fileName(fileName: String?): Builder {
                this.fileName = fileName
                return this
            }

            fun dirPath(dirPath: String?): Builder {
                this.dirPath = dirPath
                return this
            }

            fun line(line: String?): Builder {
                this.line = line
                return this
            }

            fun retentionPolicy(retentionPolicy: Int): Builder {
                this.retentionPolicy = retentionPolicy
                return this
            }

            fun maxFileCount(maxFileCount: Int): Builder {
                this.maxFileCount = maxFileCount
                return this
            }

            fun maxSize(maxSize: Long): Builder {
                maxTotalSize = maxSize
                return this
            }

            fun flush(flush: Boolean): Builder {
                this.flush = flush
                return this
            }

            fun build(): LogData {
                return LogData(this)
            }
        }

        init {
            context = b.context
            fileName = b.fileName
            dirPath = b.dirPath
            line = b.line
            retentionPolicy = b.retentionPolicy
            maxFileCount = b.maxFileCount
            maxTotalSize = b.maxTotalSize
            flush = b.flush
        }
    }
}