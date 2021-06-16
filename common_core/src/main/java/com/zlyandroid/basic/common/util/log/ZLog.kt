@file:Suppress("UNCHECKED_CAST")

package com.zlyandroid.basic.common.util.log

import android.text.TextUtils
import com.zlyandroid.basic.common.AppCommon
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object ZLog {

    val LINE_SEPARATOR = System.getProperty("line.separator")
    const val NULL_TIPS = "Log with null object"

    private const val DEFAULT_MESSAGE = "execute"
    private const val PARAM = "Param"
    private const val NULL = "null"
    private const val TAG_DEFAULT = "KLog"
    private const val SUFFIX = ".java"

    const val JSON_INDENT = 4

    private const val JSON = 0x7
    private const val XML = 0x8

    private const val STACK_TRACE_INDEX_5 = 5
    private const val STACK_TRACE_INDEX_6 = 6
    private const val STACK_TRACE_INDEX_4 = 4

    private var mGlobalTag: String? = null
    private var mIsGlobalTagEmpty = true
    private var IS_SHOW_LOG = true
    private var IS_SAVE_LOG = false //是否实时保存log



    fun init(isShowLog: Boolean) {
         IS_SHOW_LOG = isShowLog
    }

    fun init(isShowLog: Boolean,isSaveLog: Boolean, tag: String? ) {
         IS_SHOW_LOG = isShowLog
         mGlobalTag = tag
         IS_SAVE_LOG = isSaveLog
         mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag)
    }
    fun v() {
         printLog(LogLevel.V, null, DEFAULT_MESSAGE)
    }

    fun v(msg: Any) {
         printLog(LogLevel.V, null, msg)
    }

    fun v(tag: String?, vararg objects: Any) {
         printLog(LogLevel.V, tag, *objects)
    }

    fun d() {
         printLog(LogLevel.D, null, DEFAULT_MESSAGE)
    }

    fun d(msg: Any) {
        printLog(LogLevel.D, null, msg)
    }

    fun d(tag: String?, vararg objects: Any) {
        printLog(LogLevel.D, tag, *objects)
    }

    fun i() {
         printLog(LogLevel.I, null, DEFAULT_MESSAGE)
    }

    fun i(msg: Any) {

         printLog(LogLevel.I, null, msg)
    }

    fun i(tag: String?, vararg objects: Any) {
         printLog(LogLevel.I, tag, *objects)
    }

    fun w() {
         printLog(LogLevel.W, null, DEFAULT_MESSAGE)
    }

    fun w(msg: Any) {
         printLog(LogLevel.W, null, msg)
    }

    fun w(tag: String?, vararg objects: Any) {
        printLog(LogLevel.W, tag, *objects)
    }


    fun e() {
         printLog(LogLevel.E, null, DEFAULT_MESSAGE)
    }

    fun e(msg: Any) {
         printLog(LogLevel.E, null, msg)
    }

    fun e(tag: String?, vararg objects: Any?) {
         printLog(LogLevel.E, tag, *objects as Array<out Any>)
    }

    fun e(index: Int, tag: String?, vararg objects: Any) {
         printLog(LogLevel.E, index, tag, *objects)
    }
    fun a() {
         printLog(LogLevel.A, null, DEFAULT_MESSAGE)
    }

    fun a(msg: Any) {
         printLog(LogLevel.A, null, msg)
    }

    fun a(tag: String?, vararg objects: Any) {
         printLog(LogLevel.A, tag, *objects)
    }
    fun json(jsonFormat: Any) {
         printLog(JSON, null, jsonFormat)
    }

    fun json(jsonFormat: String, index: Int) {
         printLog(JSON, index, null, jsonFormat)
    }

    fun json(tag: String?, jsonFormat: Any) {
        printLog(JSON, tag, jsonFormat)
    }


    fun xml(xml: String?) {
        if (xml != null) {
            printLog(XML, null, xml)
        }
    }

    fun xml(tag: String?, xml: String?) {
        if (xml != null) {
            printLog(XML, tag, xml)
        }
    }


    fun file(targetDirectory: File?, msg: Any?) {
            if (msg != null) {
                printFile(null, targetDirectory, null, msg)
            }
    }

    fun file(tag: String?, targetDirectory: File?, msg: Any?) {
            if (msg != null) {
                printFile(tag, targetDirectory, null, msg)
            }
    }

    fun file(tag: String?, targetDirectory: File?, fileName: String?, msg: Any?) {
        if (msg != null) {
            printFile(tag, targetDirectory, fileName, msg)
        }
    }
    fun writeLog(tag: String?, args: Any) {
        writeLog(LogLevel.I,tag,args)
    }
    fun writeLog(args: Any) {
        writeLog(LogLevel.I,mGlobalTag,args)
    }

    fun writeLog(level: Int, tag: String?, args: Any) {
        val timeMs = FileLog.getStringTime(FileLog.yyyymmdd_hh_mm_ss)
        val line: String =  formatLine(timeMs, level, tag, args)
        val flush = level==LogLevel.E
         v( "line",line )

        FileService.instance().logFile(
            AppCommon.getApplication(),
            FileLog.getFileName(),
            FileLog.SAVE_FILE_DIR,
            line,
            FileLog.FILE_COUNT,
            FileLog.DEFAULT_MAX_FILE_COUNT,
            FileLog.DEFAULT_MAX_TOTAL_SIZE,flush)
    }
    fun debug() {
         printDebug(null, DEFAULT_MESSAGE)
    }

    fun debug(msg: Any?) {
        if (msg != null) {
            printDebug(null, msg)
        }
    }

    fun debug(tag: String?, vararg objects: Any) {
        if (tag != null) {
            printDebug(tag, *objects)
        }
    }

    fun trace() {
         printStackTrace()
    }


    private fun printStackTrace() {
        if (! IS_SHOW_LOG) {
            return
        }
        val tr = Throwable()
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        val message = sw.toString()
        val traceString = message.split("\\n\\t").toTypedArray()
        val sb = java.lang.StringBuilder()
        sb.append("\n")
        for (trace in traceString) {
            if (trace.contains("at com.socks.library.KLog")) {
                continue
            }
            sb.append(trace).append("\n")
        }
        val contents =  wrapperContent(STACK_TRACE_INDEX_4, null, sb.toString())
        val tag = contents?.get(0)
        val msg = contents?.get(1)
        val headString = contents?.get(2)
        if (tag != null) {
            BaseLog.printDefault(LogLevel.D, tag, headString + msg)
        }
    }

    private fun printLog(type: Int, tagStr: String?, vararg objects: Any) {
        if (! IS_SHOW_LOG) {
            return
        }
        if(IS_SAVE_LOG){
            writeLog(type,tagStr,objects);
        }
        val contents =  wrapperContent(STACK_TRACE_INDEX_6, tagStr, *objects)
        val tag = contents?.get(0)
        val msg = contents?.get(1)
        val headString = contents?.get(2)
        when (type) {
            LogLevel.V, LogLevel.D -> JsonLog.printJson(LogLevel.D, tag, msg, headString)
            LogLevel.I -> JsonLog.printJson(LogLevel.I, tag, msg, headString)
            LogLevel.W -> JsonLog.printJson(LogLevel.W, tag, msg, headString)
            LogLevel.E -> JsonLog.printJson(LogLevel.E, tag, msg, headString)
            LogLevel.A, JSON -> JsonLog.printJson(tag, msg, headString)
            XML -> XmlLog.printXml(tag, msg, headString)
        }
    }


    private fun printLog(type: Int, index: Int, tagStr: String?, vararg objects: Any) {
        if (! IS_SHOW_LOG) {
            return
        }
        val contents =  wrapperContent(index, tagStr, *objects)
        val tag = contents?.get(0)
        val msg = contents?.get(1)
        val headString = contents?.get(2)
        when (type) {
            LogLevel.V, LogLevel.D, LogLevel.I, LogLevel.W, LogLevel.A -> BaseLog.printDefault(type,
                tag,
                headString + msg)
            LogLevel.E, JSON -> JsonLog.printJson(type, tag, msg, headString)
            XML -> XmlLog.printXml(tag, msg, headString)
        }
    }

    private fun printDebug(tagStr: String?, vararg objects: Any) {
        val contents =  wrapperContent(STACK_TRACE_INDEX_5, tagStr, *objects)
        val tag = contents?.get(0)
        val msg = contents?.get(1)
        val headString = contents?.get(2)
        BaseLog.printDefault(LogLevel.D, tag, headString + msg)
    }

    private fun printFile(
        tagStr: String?,
        targetDirectory: File?,
        fileName: String?,
        objectMsg: Any
    ) {
        if (! IS_SHOW_LOG) {
            return
        }
        val contents =  wrapperContent(STACK_TRACE_INDEX_5, tagStr, objectMsg)
        val tag = contents?.get(0)
        val msg = contents?.get(1)
        val headString = contents?.get(2)
        if (msg != null) {
            FileLog.printFile(tag, targetDirectory, fileName, headString, msg)
        }
    }

    private fun wrapperContent(
        stackTraceIndex: Int,
        tagStr: String?,
        vararg objects: Any
    ): Array<String?>? {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[stackTraceIndex]
        var className = targetElement.className
        val classNameInfo = className.split("\\.").toTypedArray()
        if (classNameInfo.size > 0) {
            className = classNameInfo[classNameInfo.size - 1] +  SUFFIX
        }
        if (className.contains("$")) {
            className = className.split("\\$").toTypedArray()[0] +  SUFFIX
        }
        var lineNumber = targetElement.lineNumber
        if (lineNumber < 0) {
        }
        var tag = tagStr ?: className
        if ( mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag =  TAG_DEFAULT
        } else if (! mIsGlobalTagEmpty) {
            tag =  mGlobalTag
        }
        val headString = java.lang.StringBuilder()
        val msg = getObjectsString(*objects)
        //        String headString = "[ 日志输出位置:(" + className + ":" + lineNumber + ")-日志输出方法名:" + methodName + " ] ";
        // String headString = "className : (" + className+":"+lineNumber + ") Methed:" + methodName+"()";
        headString.append(targetElement.className)
            .append(".")
            .append(targetElement.methodName)
            .append(" ")
            .append("(")
            .append(targetElement.fileName)
            .append(":")
            .append(targetElement.lineNumber)
            .append(")")
        return arrayOf(tag, msg, headString.toString())
    }

    private fun getObjectsString(vararg objects: Any): String? {

        return if (objects.size > 1) {
            val stringBuilder = StringBuilder()
            stringBuilder.append("\n")
            for (i in 0 until objects.size) {
                val `object` = objects[i]

                stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ")
                    .append(`object`.toString()).append("\n")
            }
            stringBuilder.toString()
        } else {
            val `object` = objects[0]
            `object`.toString()
        }
    }

    fun formatLine(timeInMillis: String, level: Int, tag: String?, log: Any): String  {
        val stackTrace = Thread.currentThread().stackTrace
        val targetElement = stackTrace[STACK_TRACE_INDEX_6]
        var levels="I";
        when (level) {
            LogLevel.V -> levels = "V";
            LogLevel.D -> levels = "D";
            LogLevel.E -> levels = "E";
            LogLevel.W -> levels = "W";
            LogLevel.I -> levels = "I";
        }
        val headString = java.lang.StringBuilder()
        headString.append(timeInMillis)
            .append(" ")
            .append(targetElement.className)
            .append(".")
            .append(targetElement.methodName)
            .append(" ")
            .append("(")
            .append(targetElement.fileName)
            .append(":")
            .append(targetElement.lineNumber)
            .append(")")
            .append(" ")
            .append(levels)
            .append(" ")
            .append(tag)
            .append(":")
            .append(" ")
            .append(log)
        return String.format(Locale.ENGLISH, headString.toString());
        //return log;
    }
}