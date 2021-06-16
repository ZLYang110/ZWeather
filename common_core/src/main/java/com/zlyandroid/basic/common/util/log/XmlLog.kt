@file:Suppress("NAME_SHADOWING")

package com.zlyandroid.basic.common.util.log

import android.util.Log
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object XmlLog {
    fun printXml(tag: String?, xml: String?, headString: String?) {
        var xml = xml
        if (xml != null) {
            xml =  formatXML(xml)
            xml = """
            $headString
            $xml
            """.trimIndent()
        } else {
            xml = headString + ZLog.NULL_TIPS
        }
        ZLogUtil.printLine(tag, true)
        val lines = xml.split(ZLog.LINE_SEPARATOR.toString()).toTypedArray()
        for (line in lines) {
            if (!ZLogUtil.isEmpty(line)) {
                Log.d(tag, "║ $line")
            }
        }
        ZLogUtil.printLine(tag, false)
    }

    private fun formatXML(inputXML: String): String? {
        return try {
            val xmlInput: Source = StreamSource(StringReader(inputXML))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(xmlInput, xmlOutput)
            xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
        } catch (e: Exception) {
            e.printStackTrace()
            inputXML
        }
    }
}