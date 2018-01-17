package com.ytu.constant;

public interface JSONConst {
    String BEGIN_OBJECT = "{";
    String END_OBJECT = "}";
    String BEGIN_ARRAY = "[";
    String END_ARRAY = "]";
    String NAME_SEPERATOR = ":";
    String VALUE_SEPERATOR = ",";
    String TOKEN_TRUE = "-1";
    String TOKEN_FALSE = "-2";
    String TOKEN_NULL = "-3";
    String TOKEN_EMPTY_STRING = "-4";
    String TOKEN_UNDEFINED = "-5";
    String EMPTY_TOKEN_TYPE = "empty";
    String NULL_TOKEN_TYPE = "null";
    String STRING_TOKEN_TYPE = "strings";
    String INTEGER_TOKEN_TYPE = "integers";
    String FLOAT_TOKEN_TYPE = "floats";
    String BOOLEAN_TOKEN_TYPE = "boolean";
    String OBJECT_DELIMETER = "|";
    String DICT_DELIMETER = "^";
    String DICT_OBJECT_TOKEN = "$";
    String DICT_ARRAY_TOKEN = "@";
}
