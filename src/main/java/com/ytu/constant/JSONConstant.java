package com.ytu.constant;

public enum JSONConstant {
    BEGIN_OBJECT("{"),
    END_OBJECT("}"),
    BEGIN_ARRAY("["),
    END_ARRAY("]"),
    NAME_SEPERATOR(":"),
    VALUE_SEPERATOR(","),
    TOKEN_TRUE("-1"),
    TOKEN_FALSE("-2"),
    TOKEN_NULL("-3"),
    TOKEN_EMPTY_STRING("-4"),
    TOKEN_UNDEFINED("-5"),
    EMPTY_TOKEN_TYPE("empty"),
    NULL_TOKEN_TYPE(null),
    STRING_TOKEN_TYPE("strings"),
    INTEGER_TOKEN_TYPE("integers"),
    DOUBLE_TOKEN_TYPE("floats"),
    BOOLEAN_TOKEN_TYPE("boolean"),
    OBJECT_DELIMETER("|"),
    DICT_DELIMETER("^"),
    DICT_OBJECT_TOKEN("$"),
    DICT_ARRAY_TOKEN("@");

    private String character;

    JSONConstant(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
