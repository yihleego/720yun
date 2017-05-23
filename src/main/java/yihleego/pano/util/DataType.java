package yihleego.pano.util;




import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DataType {
    public static final String DATATYPE_STRING = "String";
    public static final String DATATYPE_SHORT = "Short";
    public static final String DATATYPE_INTEGER = "Integer";
    public static final String DATATYPE_LONG = "Long";
    public static final String DATATYPE_DOUBLE = "Double";
    public static final String DATATYPE_FLOAT = "Float";
    public static final String DATATYPE_BYTE = "Byte";
    public static final String DATATYPE_CHAR = "Char";
    public static final String DATATYPE_BOOLEAN = "Boolean";
    public static final String DATATYPE_DATE = "Date";
    public static final String DATATYPE_TIME = "Time";
    public static final String DATATYPE_DATETIME = "DateTime";
    public static final String DATATYPE_OBJECT = "Object";
    public static final String DATATYPE_short = "short";
    public static final String DATATYPE_int = "int";
    public static final String DATATYPE_long = "long";
    public static final String DATATYPE_double = "double";
    public static final String DATATYPE_float = "float";
    public static final String DATATYPE_byte = "byte";
    public static final String DATATYPE_char = "char";
    public static final String DATATYPE_boolean = "boolean";

    public DataType() {
    }

    public static boolean isNeedFullClassName(String type) {
        return type.equals("String")?false:(type.equals("Short")?false:(type.equals("Integer")?false:(type.equals("Long")?false:(type.equals("Double")?false:(type.equals("Float")?false:(type.equals("Byte")?false:(type.equals("Char")?false:(type.equals("Boolean")?false:(type.equals("Date")?true:(type.equals("Time")?true:(type.equals("DateTime")?true:(type.equals("Object")?false:(type.equals("short")?false:(type.equals("int")?false:(type.equals("long")?false:(type.equals("double")?false:(type.equals("float")?false:(type.equals("byte")?false:(type.equals("char")?false:!type.equals("boolean"))))))))))))))))))));
    }

    public int getDatabaseDataType(String type) throws Exception {
        if(type.equalsIgnoreCase("String")) {
            return 12;
        } else if(type.equalsIgnoreCase("Short")) {
            return 4;
        } else if(type.equalsIgnoreCase("Integer")) {
            return 4;
        } else if(type.equalsIgnoreCase("Long")) {
            return 4;
        } else if(type.equalsIgnoreCase("Double")) {
            return 8;
        } else if(type.equalsIgnoreCase("Float")) {
            return 6;
        } else if(type.equalsIgnoreCase("Byte")) {
            return 4;
        } else if(type.equalsIgnoreCase("Char")) {
            return 12;
        } else if(type.equalsIgnoreCase("Boolean")) {
            return 4;
        } else if(type.equalsIgnoreCase("Date")) {
            return 91;
        } else if(type.equalsIgnoreCase("Time")) {
            return 92;
        } else if(type.equalsIgnoreCase("DateTime")) {
            return 93;
        } else {
            String msg = "不能获取数据类型：" + type + " 的数据库相关数据类型";
            throw new Exception(msg);
        }
    }

    public static String getJavaObjectType(String type) {
        return type.equalsIgnoreCase("String")?"String":(!type.equalsIgnoreCase("Short") && !type.equalsIgnoreCase("short")?(!type.equalsIgnoreCase("Integer") && !type.equalsIgnoreCase("int")?(!type.equalsIgnoreCase("Long") && !type.equalsIgnoreCase("long")?(!type.equalsIgnoreCase("Double") && !type.equalsIgnoreCase("double")?(!type.equalsIgnoreCase("Float") && !type.equalsIgnoreCase("float")?(!type.equalsIgnoreCase("Byte") && !type.equalsIgnoreCase("byte")?(!type.equalsIgnoreCase("Char") && !type.equalsIgnoreCase("char")?(!type.equalsIgnoreCase("Boolean") && !type.equalsIgnoreCase("boolean")?(type.equalsIgnoreCase("Date")?"Date":(type.equalsIgnoreCase("Time")?"Time":(type.equalsIgnoreCase("DateTime")?"Timestamp":type))):"Boolean"):"Character"):"Byte"):"Float"):"Double"):"Long"):"Integer"):"Short");
    }



    public static String getTypeDefine(String type) throws Exception {
        return type.equalsIgnoreCase("String")?"DataType.DATATYPE_STRING":(type.equalsIgnoreCase("Short")?"DataType.DATATYPE_SHORT":(type.equalsIgnoreCase("Integer")?"DataType.DATATYPE_INTEGER":(type.equalsIgnoreCase("Long")?"DataType.DATATYPE_LONG":(type.equalsIgnoreCase("Double")?"DataType.DATATYPE_DOUBLE":(type.equalsIgnoreCase("Float")?"DataType.DATATYPE_FLOAT":(type.equalsIgnoreCase("Byte")?"DataType.DATATYPE_BYTE":(type.equalsIgnoreCase("Char")?"DataType.DATATYPE_CHAR":(type.equalsIgnoreCase("Boolean")?"DataType.DATATYPE_BOOLEAN":(type.equalsIgnoreCase("Date")?"DataType.DATATYPE_DATE":(type.equalsIgnoreCase("Time")?"DataType.DATATYPE_TIME":(type.equalsIgnoreCase("DateTime")?"DataType.DATATYPE_DATETIME":type)))))))))));
    }

    public static String getTransFunc(String type) throws Exception {
        return type.equalsIgnoreCase("String")?"getAsString":(type.equalsIgnoreCase("Short")?"getAsShort":(type.equalsIgnoreCase("Integer")?"getAsInt":(type.equalsIgnoreCase("Long")?"getAsLong":(type.equalsIgnoreCase("Double")?"getAsDouble":(type.equalsIgnoreCase("Float")?"getAsFloat":(type.equalsIgnoreCase("Byte")?"getAsByte":(type.equalsIgnoreCase("Char")?"getAsChar":(type.equalsIgnoreCase("Boolean")?"getAsBoolean":(type.equalsIgnoreCase("Date")?"getAsDate":(type.equalsIgnoreCase("Time")?"getAsTime":(type.equalsIgnoreCase("DateTime")?"getAsDateTime":"getObject")))))))))));
    }

    public static String getSimpleDataType(String type) throws Exception {
        return type.equalsIgnoreCase("String")?"String":(type.equalsIgnoreCase("Short")?"short":(type.equalsIgnoreCase("Integer")?"int":(type.equalsIgnoreCase("Long")?"long":(type.equalsIgnoreCase("Double")?"double":(type.equalsIgnoreCase("Float")?"float":(type.equalsIgnoreCase("Byte")?"byte":(type.equalsIgnoreCase("Char")?"char":(type.equalsIgnoreCase("Boolean")?"boolean":(type.equalsIgnoreCase("Date")?"Date":(type.equalsIgnoreCase("Time")?"Time":(type.equalsIgnoreCase("DateTime")?"Timestamp":type)))))))))));
    }

    public static String getDataTypeBySimple(String type) throws Exception {
        return type.equalsIgnoreCase("short")?"Short":(type.equalsIgnoreCase("int")?"Integer":(type.equalsIgnoreCase("long")?"Long":(type.equalsIgnoreCase("double")?"Double":(type.equalsIgnoreCase("float")?"Float":(type.equalsIgnoreCase("byte")?"Byte":(type.equalsIgnoreCase("char")?"Char":(type.equalsIgnoreCase("boolean")?"Boolean":(type.equalsIgnoreCase("Date")?"Date":(type.equalsIgnoreCase("Time")?"Time":(type.equalsIgnoreCase("Timestamp")?"DateTime":(type.equalsIgnoreCase("java.sql.Timestamp")?"DateTime":(type.equalsIgnoreCase("java.util.Date")?"Date":type))))))))))));
    }

    public static boolean isSimpleDataType(String type) {
        return type.equalsIgnoreCase("String")?false:(type.equalsIgnoreCase("Short")?true:(type.equalsIgnoreCase("short")?true:(type.equalsIgnoreCase("Integer")?true:(type.equalsIgnoreCase("int")?true:(type.equalsIgnoreCase("Long")?true:(type.equalsIgnoreCase("long")?true:(type.equalsIgnoreCase("Double")?true:(type.equalsIgnoreCase("double")?true:(type.equalsIgnoreCase("Float")?true:(type.equalsIgnoreCase("float")?true:(type.equalsIgnoreCase("Byte")?true:(type.equalsIgnoreCase("byte")?true:(type.equalsIgnoreCase("Char")?true:(type.equalsIgnoreCase("char")?true:(type.equalsIgnoreCase("Boolean")?true:(type.equalsIgnoreCase("boolean")?true:(type.equalsIgnoreCase("Date")?false:(type.equalsIgnoreCase("Time")?false:(type.equalsIgnoreCase("DateTime")?false:false)))))))))))))))))));
    }

    public static Class getSimpleDataType(Class aClass) {
        return Integer.class.equals(aClass)?Integer.TYPE:(Short.class.equals(aClass)?Short.TYPE:(Long.class.equals(aClass)?Long.TYPE:(Double.class.equals(aClass)?Double.TYPE:(Float.class.equals(aClass)?Float.TYPE:(Byte.class.equals(aClass)?Byte.TYPE:(Character.class.equals(aClass)?Character.TYPE:(Boolean.class.equals(aClass)?Boolean.TYPE:aClass)))))));
    }

    public static String getNullValueString(String type) {
        return type.equalsIgnoreCase("String")?"null":(type.equalsIgnoreCase("Short")?"(short)0":(type.equalsIgnoreCase("Integer")?"0":(type.equalsIgnoreCase("Long")?"0":(type.equalsIgnoreCase("Double")?"0":(type.equalsIgnoreCase("Float")?"0":(type.equalsIgnoreCase("Byte")?"((byte)0)":(type.equalsIgnoreCase("Char")?"((char)0)":(type.equalsIgnoreCase("Boolean")?"false":(type.equalsIgnoreCase("Date")?"null":(type.equalsIgnoreCase("Time")?"null":(type.equalsIgnoreCase("DateTime")?"null":"null")))))))))));
    }

    public static String getNullValueString(Class type) {
        if(type.equals(Short.TYPE)) {
            return "(short)0";
        } else if(type.equals(Integer.TYPE)) {
            return "0";
        } else if(type.equals(Long.TYPE)) {
            return "0";
        } else if(type.equals(Double.TYPE)) {
            return "0";
        } else if(type.equals(Float.TYPE)) {
            return "0";
        } else if(type.equals(Byte.TYPE)) {
            return "((byte)0)";
        } else if(type.equals(Character.TYPE)) {
            return "((char)0)";
        } else if(type.equals(Boolean.TYPE)) {
            return "false";
        } else {
            String msg = "不能获取" + type + "的空值属性";
            throw new RuntimeException(msg);
        }
    }

    public static String getToSimpleDataTypeFunction(String type) {
        return type.equalsIgnoreCase("String")?"":(!type.equalsIgnoreCase("Short") && !type.equalsIgnoreCase("short")?(!type.equalsIgnoreCase("Integer") && !type.equalsIgnoreCase("int")?(!type.equalsIgnoreCase("Long") && !type.equalsIgnoreCase("long")?(!type.equalsIgnoreCase("Double") && !type.equalsIgnoreCase("double")?(!type.equalsIgnoreCase("Float") && !type.equalsIgnoreCase("float")?(!type.equalsIgnoreCase("Byte") && !type.equalsIgnoreCase("byte")?(!type.equalsIgnoreCase("Char") && !type.equalsIgnoreCase("char")?(!type.equalsIgnoreCase("Boolean") && !type.equalsIgnoreCase("boolean")?(type.equalsIgnoreCase("Date")?"":(type.equalsIgnoreCase("Time")?"":(type.equalsIgnoreCase("DateTime")?"":""))):"booleanValue"):"charValue"):"byteValue"):"floatValue"):"doubleValue"):"longValue"):"intValue"):"shortValue");
    }

    public static String getToSimpleDataTypeFunction(Class type) {
        return !type.equals(Short.class) && !type.equals(Short.TYPE)?(!type.equals(Integer.class) && !type.equals(Integer.TYPE)?(!type.equals(Long.class) && !type.equals(Long.TYPE)?(!type.equals(Double.class) && !type.equals(Double.TYPE)?(!type.equals(Float.class) && !type.equals(Float.TYPE)?(!type.equals(Byte.class) && !type.equals(Byte.TYPE)?(!type.equals(Character.class) && !type.equals(Character.TYPE)?(!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)?"":"booleanValue"):"charValue"):"byteValue"):"floatValue"):"doubleValue"):"longValue"):"intValue"):"shortValue";
    }

    public static void setPrepareStatementParameter(PreparedStatement stmt, int index, String type, Object value) throws SQLException {
        if(type.equalsIgnoreCase("String")) {
            String content = value.toString();
            if(content.length() > 2000) {
                stmt.setCharacterStream(index, new StringReader(content), content.length());
            } else {
                stmt.setString(index, content);
            }
        } else if(type.equalsIgnoreCase("Short")) {
            stmt.setShort(index, Short.parseShort(value.toString()));
        } else if(type.equalsIgnoreCase("Integer")) {
            stmt.setInt(index, Integer.parseInt(value.toString()));
        } else if(type.equalsIgnoreCase("Long")) {
            stmt.setLong(index, Long.parseLong(value.toString()));
        } else if(type.equalsIgnoreCase("Double")) {
            stmt.setDouble(index, Double.parseDouble(value.toString()));
        } else if(type.equalsIgnoreCase("Float")) {
            stmt.setFloat(index, Float.parseFloat(value.toString()));
        } else if(type.equalsIgnoreCase("Byte")) {
            stmt.setByte(index, Byte.parseByte(value.toString()));
        } else if(type.equalsIgnoreCase("Char")) {
            stmt.setString(index, value.toString());
        } else if(type.equalsIgnoreCase("Boolean")) {
            stmt.setBoolean(index, Boolean.getBoolean(value.toString()));
        } else if(type.equalsIgnoreCase("Date")) {
            if(value instanceof Date) {
                stmt.setDate(index, (Date)((Date)value));
            } else {
                stmt.setDate(index, Date.valueOf(value.toString()));
            }
        } else if(type.equalsIgnoreCase("Time")) {
            if(value instanceof Time) {
                stmt.setTime(index, (Time)((Time)value));
            } else {
                stmt.setTime(index, Time.valueOf(value.toString()));
            }
        } else if(type.equalsIgnoreCase("DateTime")) {
            if(value instanceof Timestamp) {
                stmt.setTimestamp(index, (Timestamp)((Timestamp)value));
            } else if(value instanceof Date) {
                stmt.setTimestamp(index, new Timestamp(((Date)value).getTime()));
            } else {
                stmt.setTimestamp(index, Timestamp.valueOf(value.toString()));
            }
        } else if(value instanceof Character) {
            stmt.setString(index, value.toString());
        } else {
            stmt.setObject(index, value);
        }

    }

    public static String transferToString(Object value, String type, int precision) {
        if(value == null) {
            return "";
        } else {
            String result = "";
            SimpleDateFormat DATA_FORMAT_yyyyMMddHHmmss;
            if(type.equalsIgnoreCase("Date")) {
                if(!(value instanceof java.util.Date) && !(value instanceof Timestamp)) {
                    result = value.toString();
                } else {
                    try {
                        DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd");
                        result = DATA_FORMAT_yyyyMMddHHmmss.format(value);
                    } catch (Exception var9) {
                        var9.printStackTrace();
                        result = "";
                    }
                }
            } else if(type.equalsIgnoreCase("Time")) {
                if(!(value instanceof java.util.Date) && !(value instanceof Time) && !(value instanceof Timestamp)) {
                    result = value.toString();
                } else {
                    try {
                        DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("HH:mm:ss");
                        result = DATA_FORMAT_yyyyMMddHHmmss.format(value);
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        result = "";
                    }
                }
            } else if(type.equalsIgnoreCase("DateTime")) {
                if(!(value instanceof java.util.Date) && !(value instanceof Timestamp)) {
                    result = value.toString();
                } else {
                    try {
                        DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        result = DATA_FORMAT_yyyyMMddHHmmss.format(value);
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        result = "";
                    }
                }
            } else if(!type.equalsIgnoreCase("Double") && !type.equalsIgnoreCase("Float")) {
                result = value.toString();
            } else {
                NumberFormat nf = NumberFormat.getInstance();
                if(precision >= 0) {
                    try {
                        nf.setMaximumFractionDigits(precision);
                        nf.setGroupingUsed(false);
                        result = nf.format(nf.parse(value.toString()).doubleValue());
                    } catch (Exception var6) {
                        var6.printStackTrace();
                        result = value.toString();
                    }
                } else {
                    result = value.toString();
                }
            }

            return result;
        }
    }

    public static Object transfer(Object value, Class type) {
        if(value == null) {
            return null;
        } else if(value instanceof String && value.toString().trim().equals("")) {
            return String.class.equals(type)?value:null;
        } else if(!type.equals(Short.class) && !type.equals(Short.TYPE)) {
            if(!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                if(!type.equals(Character.class) && !type.equals(Character.TYPE)) {
                    if(!type.equals(Long.class) && !type.equals(Long.TYPE)) {
                        if(type.equals(String.class)) {
                            return value instanceof String?value:value.toString();
                        } else {
                            String tmpstr;
                            SimpleDateFormat a;
                            if(type.equals(Date.class)) {
                                if(value instanceof Date) {
                                    return value;
                                } else if(value instanceof java.util.Date) {
                                    return new Date(((java.util.Date)value).getTime());
                                } else {
                                    try {
                                        a = new SimpleDateFormat("yyyy-MM-dd");
                                        return new Date(a.parse(value.toString()).getTime());
                                    } catch (Exception var4) {
                                        tmpstr = "不能将对象" + value.toString() + "转换为Date类型";
                                        throw new RuntimeException(tmpstr);
                                    }
                                }
                            } else if(type.equals(Time.class)) {
                                if(value instanceof Time) {
                                    return value;
                                } else if(value instanceof java.util.Date) {
                                    return new Time(((java.util.Date)value).getTime());
                                } else {
                                    try {
                                        a = new SimpleDateFormat("HH:mm:ss");
                                        return new Time(a.parse(value.toString()).getTime());
                                    } catch (Exception var5) {
                                        tmpstr = "不能将对象" + value.toString() + "转换为Time类型";
                                        throw new RuntimeException(tmpstr);
                                    }
                                }
                            } else if(type.equals(Timestamp.class)) {
                                if(value instanceof Timestamp) {
                                    return value;
                                } else if(value instanceof java.util.Date) {
                                    return new Timestamp(((java.util.Date)value).getTime());
                                } else {
                                    try {
                                        a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        tmpstr = value.toString();
                                        if(tmpstr.trim().length() <= 10) {
                                            tmpstr = tmpstr + " 00:00:00";
                                        }

                                        return new Timestamp(a.parse(tmpstr).getTime());
                                    } catch (Exception var6) {
                                        tmpstr = "不能将对象" + value.toString() + "转换为DateTime类型";
                                        throw new RuntimeException(tmpstr);
                                    }
                                }
                            } else if(!type.equals(Double.class) && !type.equals(Double.TYPE)) {
                                if(!type.equals(Float.class) && !type.equals(Float.TYPE)) {
                                    if(!type.equals(Byte.class) && !type.equals(Byte.TYPE)) {
                                        if(!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
                                            return value;
                                        } else if(value instanceof Boolean) {
                                            return value;
                                        } else if(value instanceof Number) {
                                            return ((Number)value).doubleValue() > 0.0D?new Boolean(true):new Boolean(false);
                                        } else if(value instanceof String) {
                                            return !((String)value).equalsIgnoreCase("true") && !((String)value).equalsIgnoreCase("y")?new Boolean(false):new Boolean(true);
                                        } else {
                                            String msg = "不能将对象" + value.toString() + "转换为Boolean类型";
                                            throw new RuntimeException(msg);
                                        }
                                    } else {
                                        return value instanceof Byte?value:new Byte((new BigDecimal(value.toString())).byteValue());
                                    }
                                } else {
                                    return value instanceof Float?value:new Float((new BigDecimal(value.toString())).floatValue());
                                }
                            } else {
                                return value instanceof Double?value:new Double((new BigDecimal(value.toString())).doubleValue());
                            }
                        }
                    } else {
                        return value instanceof Long?value:new Long((new BigDecimal(value.toString())).longValue());
                    }
                } else {
                    return value instanceof Character?value:new Character(value.toString().charAt(0));
                }
            } else {
                return value instanceof Integer?value:new Integer((new BigDecimal(value.toString())).intValue());
            }
        } else {
            return value instanceof Short?value:new Short((new BigDecimal(value.toString())).shortValue());
        }
    }

    public static String transferToString(Object value, String type) {
        return transferToString(value, type, -1);
    }

    public static Object transfer(Object value, String type) {
        if(value == null) {
            return null;
        } else if(value instanceof String && value.toString().trim().equals("")) {
            return "String".equalsIgnoreCase(type)?value:null;
        } else if(!type.equalsIgnoreCase("Short") && !type.equalsIgnoreCase("short")) {
            if(!type.equalsIgnoreCase("Integer") && !type.equalsIgnoreCase("int")) {
                if(!type.equalsIgnoreCase("Char") && !type.equalsIgnoreCase("char")) {
                    if(!type.equalsIgnoreCase("Long") && !type.equalsIgnoreCase("long")) {
                        if(type.equalsIgnoreCase("String")) {
                            return value instanceof String?value:value.toString();
                        } else {
                            String msg;
                            String tmpstr;
                            if(type.equalsIgnoreCase("Date")) {
                                if(value instanceof Date) {
                                    return value;
                                } else if(value instanceof Timestamp) {
                                    return new Date(((Timestamp)value).getTime());
                                } else {
                                    try {
                                        msg = value.toString().replace('/', '-');
                                        SimpleDateFormat DATA_FORMAT_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                                        return new Date(DATA_FORMAT_yyyyMMdd.parse(msg).getTime());
                                    } catch (Exception var6) {
                                        if(var6 instanceof RuntimeException) {
                                            throw (RuntimeException)var6;
                                        } else {
                                            tmpstr = "不能将对象" + value.toString() + "转换为Date类型";
                                            throw new RuntimeException(tmpstr, var6);
                                        }
                                    }
                                }
                            } else {
                                SimpleDateFormat a;
                                if(type.equalsIgnoreCase("Time")) {
                                    if(value instanceof Time) {
                                        return value;
                                    } else if(value instanceof Timestamp) {
                                        return new Time(((Timestamp)value).getTime());
                                    } else {
                                        try {
                                            a = new SimpleDateFormat("HH:mm:ss");
                                            return new Time(a.parse(value.toString()).getTime());
                                        } catch (Exception var4) {
                                            tmpstr = "不能将对象" + value.toString() + "转换为Time类型";
                                            throw new RuntimeException(tmpstr, var4);
                                        }
                                    }
                                } else if(type.equalsIgnoreCase("DateTime")) {
                                    if(value instanceof Timestamp) {
                                        return value;
                                    } else if(value instanceof java.util.Date) {
                                        return new Timestamp(((java.util.Date)value).getTime());
                                    } else {
                                        try {
                                            a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            tmpstr = value.toString();
                                            if(tmpstr.trim().length() <= 10) {
                                                tmpstr = tmpstr + " 00:00:00";
                                            }

                                            return new Timestamp(a.parse(tmpstr).getTime());
                                        } catch (Exception var5) {
                                            tmpstr = "不能将对象" + value.toString() + "转换为DateTime类型";
                                            throw new RuntimeException(tmpstr);
                                        }
                                    }
                                } else if(!type.equalsIgnoreCase("Double") && !type.equalsIgnoreCase("double")) {
                                    if(!type.equalsIgnoreCase("Float") && !type.equalsIgnoreCase("float")) {
                                        if(!type.equalsIgnoreCase("Byte") && !type.equalsIgnoreCase("byte")) {
                                            if(!type.equalsIgnoreCase("Boolean") && !type.equalsIgnoreCase("boolean")) {
                                                return value;
                                            } else if(value instanceof Boolean) {
                                                return value;
                                            } else if(value instanceof Number) {
                                                return ((Number)value).doubleValue() > 0.0D?new Boolean(true):new Boolean(false);
                                            } else if(value instanceof String) {
                                                return !((String)value).equalsIgnoreCase("true") && !((String)value).equalsIgnoreCase("y")?new Boolean(false):new Boolean(true);
                                            } else {
                                                msg = "不能将对象" + value.toString() + "转换为Boolean类型";
                                                throw new RuntimeException(msg);
                                            }
                                        } else {
                                            return value instanceof Byte?value:new Byte((new BigDecimal(value.toString())).byteValue());
                                        }
                                    } else {
                                        return value instanceof Float?value:new Float((new BigDecimal(value.toString())).floatValue());
                                    }
                                } else {
                                    return value instanceof Double?value:new Double((new BigDecimal(value.toString())).doubleValue());
                                }
                            }
                        }
                    } else {
                        return value instanceof Long?value:new Long((new BigDecimal(value.toString())).longValue());
                    }
                } else {
                    return value instanceof Character?value:new Character(value.toString().charAt(0));
                }
            } else {
                return value instanceof Integer?value:new Integer((new BigDecimal(value.toString())).intValue());
            }
        } else {
            return value instanceof Short?value:new Short((new BigDecimal(value.toString())).shortValue());
        }
    }

    public static String getAsString(Object obj) {
        return obj == null?null:obj.toString();
    }

    public static short getAsShort(Object obj) {
        return obj == null?0:(obj instanceof Number?((Number)obj).shortValue():((Short)transfer(obj, Short.class)).shortValue());
    }

    public static int getAsInt(Object obj) {
        return obj == null?0:(obj instanceof Number?((Number)obj).intValue():((Integer)transfer(obj, Integer.class)).intValue());
    }

    public static long getAsLong(Object obj) {
        return obj == null?0L:(obj instanceof Number?((Number)obj).longValue():((Long)transfer(obj, Long.class)).longValue());
    }

    public static double getAsDouble(Object obj) {
        return obj == null?0.0D:(obj instanceof Number?((Number)obj).doubleValue():((Double)transfer(obj, Double.class)).doubleValue());
    }

    public static float getAsFloat(Object obj) {
        return obj == null?0.0F:(obj instanceof Number?((Number)obj).floatValue():((Float)transfer(obj, Float.class)).floatValue());
    }

    public static byte getAsByte(Object obj) {
        return obj == null?0:(obj instanceof Number?((Number)obj).byteValue():((Byte)transfer(obj, Byte.class)).byteValue());
    }

    public static boolean getAsBoolean(Object obj) {
        return obj == null?false:(obj instanceof Boolean?((Boolean)obj).booleanValue():((Boolean)transfer(obj, Boolean.class)).booleanValue());
    }

    public static char getAsChar(Object obj) {
        return obj == null?'\u0000':(obj instanceof Character?((Character)obj).charValue():(obj instanceof String && ((String)obj).length() == 1?((String)obj).charAt(0):((Character)transfer(obj, Character.class)).charValue()));
    }

    public static Date getAsDate(Object obj) {
        if(obj == null) {
            return null;
        } else if(obj instanceof Date) {
            return (Date)obj;
        } else if(obj instanceof Timestamp) {
            return new Date(((Timestamp)obj).getTime());
        } else {
            String msg = "数据不是一个java.sql.Date类型，不能转换为date类型";
            throw new RuntimeException(msg);
        }
    }

    public static Time getAsTime(Object obj) {
        if(obj == null) {
            return null;
        } else if(obj instanceof Time) {
            return (Time)obj;
        } else if(obj instanceof Timestamp) {
            return new Time(((Timestamp)obj).getTime());
        } else {
            String msg = "数据不是一个java.sql.Date类型，不能转换为date类型";
            throw new RuntimeException(msg);
        }
    }

    public static Timestamp getAsDateTime(Object obj) {
        if(obj == null) {
            return null;
        } else if(obj instanceof Timestamp) {
            return (Timestamp)obj;
        } else if(obj instanceof Date) {
            return new Timestamp(((Date)obj).getTime());
        } else {
            String msg = "数据不是一个java.sql.Date类型，不能转换为date类型";
            throw new RuntimeException(msg);
        }
    }

    public static String getModifyName(int mod) {
        StringBuilder sb = new StringBuilder();
        if((mod & 1) != 0) {
            sb.append("public ");
        }

        if((mod & 4) != 0) {
            sb.append("protected ");
        }

        if((mod & 2) != 0) {
            sb.append("private ");
        }

        if((mod & 16) != 0) {
            sb.append("final ");
        }

        if(Modifier.isStatic(mod)) {
            sb.append(" static ");
        }

        int len;
        return (len = sb.length()) > 0?sb.toString().substring(0, len - 1):"";
    }

    public static String getClassName(Class className) {
        String name = className.getName();
        return getClassName(name);
    }

    public static String getClassName(String name) {
        String arrays = "";
        int point;
        if(name.indexOf("[") >= 0) {
            for(point = 0; name.charAt(point) == 91; ++point) {
                arrays = arrays + "[]";
            }

            if(name.charAt(point) == 76) {
                name = name.substring(point + 1, name.length() - 1);
            } else if(name.charAt(point) == 90) {
                name = "boolean";
            } else if(name.charAt(point) == 66) {
                name = "byte";
            } else if(name.charAt(point) == 67) {
                name = "char";
            } else if(name.charAt(point) == 68) {
                name = "double";
            } else if(name.charAt(point) == 70) {
                name = "float";
            } else if(name.charAt(point) == 73) {
                name = "int";
            } else if(name.charAt(point) == 74) {
                name = "long";
            } else if(name.charAt(point) == 83) {
                name = "short";
            }
        }

        point = name.lastIndexOf(46);
        if(point > 0 && name.substring(0, point).equals("java.lang")) {
            name = name.substring(point + 1);
        }

        name = name + arrays;
        return name;
    }

    public static String[] getDataTypeNames() {
        return new String[]{"String", "Short", "Integer", "Long", "Double", "Float", "Byte", "Char", "Boolean", "Date", "Time", "DateTime", "Object", "short", "int", "long", "long", "float", "byte", "char", "boolean", "UserInfoInterface"};
    }

    public static Class getPrimitiveClass(Class type) {
        return type.equals(Short.TYPE)?Short.class:(type.equals(Integer.TYPE)?Integer.class:(type.equals(Long.TYPE)?Long.class:(type.equals(Double.TYPE)?Double.class:(type.equals(Float.TYPE)?Float.class:(type.equals(Byte.TYPE)?Byte.class:(type.equals(Character.TYPE)?Character.class:(type.equals(Boolean.TYPE)?Boolean.class:type)))))));
    }

    public static Class getSimpleClass(Class type) {
        return type.equals(Short.class)?Short.TYPE:(type.equals(Integer.class)?Integer.TYPE:(type.equals(Long.class)?Long.TYPE:(type.equals(Double.class)?Double.TYPE:(type.equals(Float.class)?Float.TYPE:(type.equals(Byte.class)?Byte.TYPE:(type.equals(Character.class)?Character.TYPE:(type.equals(Boolean.class)?Boolean.TYPE:type)))))));
    }

    public static String getPrimitiveClass(String type) {
        return type.equals("short")?Short.class.getName():(type.equals("int")?Integer.class.getName():(type.equals("long")?Long.class.getName():(type.equals("double")?Double.class.getName():(type.equals("float")?Float.class.getName():(type.equals("byte")?Byte.class.getName():(type.equals("char")?Character.class.getName():(type.equals("boolean")?Boolean.class.getName():type)))))));
    }

    public static Method findMethod(Class baseClass, String methodName, Class[] types, boolean publicOnly, boolean isStatic) {
        Vector candidates = gatherMethodsRecursive(baseClass, methodName, types.length, publicOnly, isStatic, (Vector)null);
        Method method = findMostSpecificMethod(types, (Method[])((Method[])candidates.toArray(new Method[0])));
        return method;
    }

    static Constructor findConstructor(Class baseClass, Class[] types) {
        Constructor[] constructors = baseClass.getConstructors();
        Class[][] candidateSigs = new Class[constructors.length][];
        List list = new ArrayList();

        int i;
        for(i = 0; i < constructors.length; ++i) {
            if(constructors[i].getParameterTypes().length == types.length) {
                list.add(constructors[i].getParameterTypes());
            }
        }

        i = findMostSpecificSignature(types, (Class[][])((Class[][])list.toArray(new Class[0][])));
        return i == -1?null:constructors[i];
    }

    static int findMostSpecificSignature(Class[] idealMatch, Class[][] candidates) {
        Class[] bestMatch = null;
        int bestMatchIndex = -1;

        for(int i = candidates.length - 1; i >= 0; --i) {
            Class[] targetMatch = candidates[i];
            if(isSignatureAssignable(idealMatch, targetMatch) && (bestMatch == null || isSignatureAssignable(targetMatch, bestMatch))) {
                bestMatch = targetMatch;
                bestMatchIndex = i;
            }
        }

        if(bestMatch != null) {
            return bestMatchIndex;
        } else {
            return -1;
        }
    }

    static boolean isSignatureAssignable(Class[] from, Class[] to) {
        for(int i = 0; i < from.length; ++i) {
            if(!isAssignable(to[i], from[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAssignable(Class dest, Class sour) {
        if(dest == sour) {
            return true;
        } else if(dest == null) {
            return false;
        } else if(sour == null) {
            return !dest.isPrimitive();
        } else if(dest.isPrimitive() && sour.isPrimitive()) {
            if(dest == sour) {
                return true;
            } else if(sour == Byte.TYPE && (dest == Short.TYPE || dest == Integer.TYPE || dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE)) {
                return true;
            } else if(sour != Short.TYPE || dest != Integer.TYPE && dest != Long.TYPE && dest != Float.TYPE && dest != Double.TYPE) {
                if(sour == Character.TYPE && (dest == Integer.TYPE || dest == Long.TYPE || dest == Float.TYPE || dest == Double.TYPE)) {
                    return true;
                } else if(sour != Integer.TYPE || dest != Long.TYPE && dest != Float.TYPE && dest != Double.TYPE) {
                    if(sour == Long.TYPE && (dest == Float.TYPE || dest == Double.TYPE)) {
                        return true;
                    } else if(sour == Float.TYPE && dest == Double.TYPE) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else if(dest.isAssignableFrom(sour)) {
            return true;
        } else {
            return false;
        }
    }

    static Method findMostSpecificMethod(Class[] idealMatch, Method[] methods) {
        Class[][] candidateSigs = new Class[methods.length][];

        int match;
        for(match = 0; match < methods.length; ++match) {
            candidateSigs[match] = methods[match].getParameterTypes();
        }

        match = findMostSpecificSignature(idealMatch, candidateSigs);
        return match == -1?null:methods[match];
    }

    private static Vector gatherMethodsRecursive(Class baseClass, String methodName, int numArgs, boolean publicOnly, boolean isStatic, Vector candidates) {
        if(candidates == null) {
            candidates = new Vector();
        }

        addCandidates(baseClass.getDeclaredMethods(), methodName, numArgs, publicOnly, isStatic, candidates);
        Class[] intfs = baseClass.getInterfaces();

        for(int i = 0; i < intfs.length; ++i) {
            gatherMethodsRecursive(intfs[i], methodName, numArgs, publicOnly, isStatic, candidates);
        }

        Class superclass = baseClass.getSuperclass();
        if(superclass != null) {
            gatherMethodsRecursive(superclass, methodName, numArgs, publicOnly, isStatic, candidates);
        }

        return candidates;
    }

    private static Vector addCandidates(Method[] methods, String methodName, int numArgs, boolean publicOnly, boolean isStatic, Vector candidates) {
        for(int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            if(m.getName().equals(methodName) && m.getParameterTypes().length == numArgs && (!publicOnly || isPublic(m) && (!isStatic || isStatic(m)))) {
                candidates.add(m);
            }
        }

        return candidates;
    }

    private static boolean isPublic(Class c) {
        return Modifier.isPublic(c.getModifiers());
    }

    private static boolean isPublic(Method m) {
        return Modifier.isPublic(m.getModifiers());
    }

    private static boolean isStatic(Method m) {
        return Modifier.isStatic(m.getModifiers());
    }
}
