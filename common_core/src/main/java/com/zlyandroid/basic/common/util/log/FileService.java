package com.zlyandroid.basic.common.util.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;


/**
 * @author zhangliyang
 * @date 2020/11/18
 * GitHub: https://github.com/ZLYang110
 */

public class FileService {
    private static String TAG="FileService";

    private static final Comparator<File> FILE_COMPARATOR = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            long lm1 = o1.lastModified();
            long lm2 = o2.lastModified();
            return lm1 < lm2 ? -1 : lm1 == lm2 ? 0 : 1;
        }
    };

    public static FileService instance() {
        return InstanceHolder.INSTANCE;
    }

    public static class InstanceHolder {
        static final FileService INSTANCE = new FileService();
    }

    private final BlockingQueue<LogData> mQueue;
    private volatile boolean mIsRunning;

    public FileService() {
        mQueue = new LinkedBlockingDeque<>();
    }

    public void logFile(Context context, String fileName, String dirPath, String line,
                 int retentionPolicy, int maxFileCount, long maxTotalSize, boolean flush) {
        ensureThread();
        boolean addResult = mQueue.offer(new LogData.Builder().context(context)
                .fileName(fileName)
                .dirPath(dirPath)
                .line(line)
                .retentionPolicy(retentionPolicy)
                .maxFileCount(maxFileCount)
                .maxSize(maxTotalSize)
                .flush(flush)
                .build());
        if (!addResult) {
            Log.i(TAG,"failed to add to file logger service queue");
        }
    }

    private void ensureThread() {
        if (!mIsRunning) {
            synchronized (this) {
                if (!mIsRunning) {
                    mIsRunning = true;
                    Log.i(TAG,"start file logger service thread");
                    new LogFileThread().start();
                }
            }
        }
    }

    private class LogFileThread extends Thread {

        private BufferedWriter mWriter;
        private String mPath;
        private int mRetentionPolicy;
        private int mMaxFileCount;
        private long mMaxSize;

        File logFileDir = null;
        String patho;

        @Override
        public void run() {
            super.run();
            Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    throwable.printStackTrace();
                    mIsRunning = false;
                }
            });

            try {
                for (; ; ) {
                    LogData log = mQueue.take();
                    logLine(log);
                    collectParams(log);
                    while ((log = mQueue.poll(2, TimeUnit.SECONDS)) != null) {
                        logLine(log);
                        collectParams(log);
                    }

                    closeWriter();
                    startHouseKeeping();
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "file logger service thread is interrupted");
            }

            Log.i(TAG,"file logger service thread stopped");
            mIsRunning = false;
        }

        private void collectParams(LogData log) {
            mRetentionPolicy = log.retentionPolicy;
            mMaxFileCount = log.maxFileCount;
            mMaxSize = log.maxTotalSize;
        }

        private void logLine(LogData log) {
            if (TextUtils.isEmpty(log.fileName)) {
                throw new IllegalStateException("invalid file name: [" + log.fileName + "]");
            }

            if (TextUtils.isEmpty(log.dirPath)) {
                throw new IllegalStateException("invalid directory path: [" + log.dirPath + "]");
            }

            if (TextUtils.isEmpty(log.line)) {
                return;
            }

            File dir = new File(log.dirPath);
            if (!FileLog.ensureDir(dir)) {
                dir.mkdirs();
            }
            logFileDir = new File(log.dirPath, log.fileName);
            if (!logFileDir.exists()) {
                try {
                    logFileDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            patho= logFileDir.getAbsolutePath();
            if (mWriter != null && patho.equals(mPath)) {
                try {
                    mWriter.write(log.line);
                    mWriter.write("\n");
                    if (log.flush) {
                        mWriter.flush();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            } else {
                closeWriter();
                FileLog.ensureFile(logFileDir);
                try {
                    mWriter = createWriter(logFileDir);
                    mPath = logFileDir.getAbsolutePath();

                    mWriter.write(log.line);
                    mWriter.write("\n");
                    if (log.flush) {
                        mWriter.flush();
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        private BufferedWriter createWriter(File file) throws IOException {
            // one line ~100 characters = ~100-400 bytes
            // use default buf size ~8k = ~20-80 lines
            return new BufferedWriter(new FileWriter(file, true));
        }
        private void startHouseKeeping() {
            if (TextUtils.isEmpty(mPath)) {
                return;
            }

            if (mRetentionPolicy == 1) {
                houseKeepByCount(mMaxFileCount);
            } else if (mRetentionPolicy == 2) {
                houseKeepBySize(mMaxSize);
            }
        }

        private void houseKeepByCount(int maxCount) {
            if (maxCount <= 0) {
                throw new IllegalStateException("invalid max file count: " + maxCount);
            }

            File file = new File(mPath);
            File dir = file.getParentFile();
            if (dir == null) {
                return;
            }

            File[] files = dir.listFiles();
            if (files == null || files.length <= maxCount) {
                return;
            }

            Arrays.sort(files, FILE_COMPARATOR);
            int deleteCount = files.length - maxCount;
            int successCount = 0;
            for (int i = 0; i < deleteCount; i++) {
                if (files[i].delete()) {
                    successCount++;
                }
            }
           // Log.d(TAG,"house keeping complete: file count ["+files.length+" ->"+files.length - successCount+"]" +"2222");
        }

        private void houseKeepBySize(long maxSize) {
            if (maxSize <= 0) {
                throw new IllegalStateException("invalid max total size: " + maxSize);
            }

            File file = new File(mPath);
            File dir = file.getParentFile();
            if (dir == null) {
                return;
            }

            File[] files = dir.listFiles();
            if (files == null) {
                return;
            }

            long totalSize = 0;
            for (File f : files) {
                totalSize += f.length();
            }

            if (totalSize <= maxSize) {
                return;
            }

            Arrays.sort(files, FILE_COMPARATOR);
            long newSize = totalSize;
            for (File f : files) {
                long size = f.length();
                if (f.delete()) {
                    newSize -= size;
                    if (newSize <= maxSize) {
                        break;
                    }
                }
            }
            Log.d(TAG,  "house keeping complete: total size ["+totalSize+" -> "+newSize+"]" );
        }

        private void closeWriter() {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                    Log.e(TAG,  e.getMessage());
                }
                mWriter = null;
            }
        }
    }

    static class LogData {
        final Context context;
        final String fileName, dirPath, line;
        final int retentionPolicy, maxFileCount;
        long maxTotalSize;
        boolean flush;

        LogData(Builder b) {
            context = b.context;
            fileName = b.fileName;
            dirPath = b.dirPath;
            line = b.line;
            retentionPolicy = b.retentionPolicy;
            maxFileCount = b.maxFileCount;
            maxTotalSize = b.maxTotalSize;
            flush = b.flush;
        }

        static class Builder {
            Context context;
            String fileName, dirPath, line;
            int retentionPolicy, maxFileCount;
            long maxTotalSize;
            boolean flush;

            Builder context(Context context) {
                this.context = context;
                return this;
            }

            Builder fileName(String fileName) {
                this.fileName = fileName;
                return this;
            }

            Builder dirPath(String dirPath) {
                this.dirPath = dirPath;
                return this;
            }

            Builder line(String line) {
                this.line = line;
                return this;
            }

            Builder retentionPolicy(int retentionPolicy) {
                this.retentionPolicy = retentionPolicy;
                return this;
            }
            Builder maxFileCount(int maxFileCount) {
                this.maxFileCount = maxFileCount;
                return this;
            }

            Builder maxSize(long maxSize) {
                this.maxTotalSize = maxSize;
                return this;
            }

            Builder flush(boolean flush) {
                this.flush = flush;
                return this;
            }

            LogData build() {
                return new LogData(this);
            }
        }
    }
}
