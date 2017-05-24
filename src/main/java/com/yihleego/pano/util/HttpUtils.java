package com.yihleego.pano.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by wbt on 17-5-23.
 */

public class HttpUtils {
    private static transient Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static String RootName = "ROOTINFO";
    public static final String XML_CONTENT_TYPE = "text/xml; charset=GBK";
    public static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String XML_CONTENT_TYPE_FOR_SAVE = "text/xml; charset=UTF-8";
    public static final String SETNAME_CLASS_PREFIX = "${";
    public static final String SETNAME_CLASS_SUFFIX = "}";
    public static final String CHARSET_ISO8859 = "ISO-8859-1";
    public static final String CHARSET_GB2312 = "GB2312";
    public static final String CHARSET_UTF8 = "UTF8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_GB18030 = "GB18030";
    private static String Version = "1.0";
    private static String Encoding = "GBK";

    public HttpUtils() {
    }

    public static String getVersion() {
        return Version;
    }

    public static void setVersion(String aVersion) {
        Version = aVersion;
    }

    public static String getEncoding() {
        return Encoding;
    }

    public static void setEncoding(String aEncoding) {
        Encoding = aEncoding;
    }

    public static String getRootName() {
        return RootName;
    }

    public static void setRootName(String aRootName) {
        RootName = aRootName;
    }

    public static String getXmlDeclare() {
        return "<?xml version ='" + getVersion() + "' encoding = '" + getEncoding() + "'?>\n\n";
    }

    public static final String getRootNameStart() {
        return "<" + RootName + ">";
    }

    public static final String getRootNameEnd() {
        return "</" + RootName + ">";
    }

    public static String getXmlContentType() {
        return "text/xml; charset=GBK";
    }

    public static String getHtmlContentType() {
        return "text/html; charset=UTF-8";
    }

    public static String getXmlContentTypeForSave() {
        return "text/xml; charset=UTF-8";
    }

    public static String getErrorXml(String aErrorId) {
        String Result = getXmlDeclare();
        Result = Result + "<ERROR>" + aErrorId + "</ERROR>";
        return Result;
    }

    public static String getOkXml(String aErrorId) {
        String Result = getXmlDeclare();
        Result = Result + "<OK>" + aErrorId + "</OK>";
        return Result;
    }

    public static String getStringFromBufferedReader(HttpServletRequest request) {
        String bodyData = "";

        try {
            byte[] buffer = new byte[8192];
            ServletInputStream sis = request.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean var5 = false;

            int bLen;
            while((bLen = sis.read(buffer)) > 0) {
                baos.write(buffer, 0, bLen);
            }

            bodyData = new String(baos.toByteArray(), "UTF-8");
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
        }

        return bodyData;
    }

    public static String getStringFromBufferedReaderOld(HttpServletRequest request) {
        ServletInputStream aReader = null;

        try {
            aReader = request.getInputStream();
        } catch (Exception var7) {
            return null;
        }

        byte[] buf = new byte[8192];
        StringBuilder sbuf = new StringBuilder();

        try {
            while(true) {
                int result = aReader.readLine(buf, 0, buf.length);
                if(result == -1) {
                    return sbuf.toString();
                }

                String line = new String(buf, 0, result, "UTF-8");
                sbuf.append(line);
            }
        } catch (Exception var8) {
            return null;
        }
    }

    public static Object getObject(ServletRequest request, String paraName) throws Exception {
        Object result = request.getAttribute(paraName);
        if(result == null) {
            result = request.getParameter(paraName);
            if(result != null) {
                return result;
            }
        }

        return result;
    }

    public static Object getObject(ServletRequest request, String paraName, String pCharset) throws Exception {
        Object result = request.getAttribute(paraName);
        if(result == null) {
            result = request.getParameter(paraName);
            if(result != null) {
                result = new String(((String)result).getBytes("UTF8"), pCharset);
            }
        }

        return result;
    }

    public static String getParameter(ServletRequest request, String paraName) throws Exception {
        return DataType.transferToString(getObject(request, paraName), "String");
    }

    public static String getParameter(ServletRequest request, String paraName, String pCharset) throws Exception {
        return DataType.transferToString(getObject(request, paraName, pCharset), "String");
    }

    public static String getAsString(ServletRequest request, String paraName) throws Exception {
        return DataType.transferToString(getObject(request, paraName), "String");
    }

    public static int getAsInt(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsInt(DataType.transfer(getObject(request, paraName), "Integer"));
    }

    public static long getAsLong(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsLong(DataType.transfer(getObject(request, paraName), "Long"));
    }

    public static short getAsShort(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsShort(DataType.transfer(getObject(request, paraName), "Short"));
    }

    public static char getAsChar(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsChar(DataType.transfer(getObject(request, paraName), "Char"));
    }

    public static double getAsDouble(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsDouble(DataType.transfer(getObject(request, paraName), "Double"));
    }

    public static float getAsFloat(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsFloat(DataType.transfer(getObject(request, paraName), "Float"));
    }

    public static boolean getAsBoolean(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsBoolean(DataType.transfer(getObject(request, paraName), "Boolean"));
    }

    public static byte getAsByte(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsByte(DataType.transfer(getObject(request, paraName), "Byte"));
    }

    public static Date getAsDate(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsDate(DataType.transfer(getObject(request, paraName), "Date"));
    }

    public static Time getAsTime(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsTime(DataType.transfer(getObject(request, paraName), "Time"));
    }

    public static Timestamp getAsDateTime(ServletRequest request, String paraName) throws Exception {
        return DataType.getAsDateTime(DataType.transfer(getObject(request, paraName), "DateTime"));
    }
}
