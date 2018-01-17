package com.ytu.compression;

import com.ytu.constant.JSONConst;
import com.ytu.constant.JSONConstant;
import com.ytu.model.Field;
import com.ytu.utils.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JSONCompress {

    private static List<String> strings = new ArrayList<>();
    private static List<Integer> integers = new ArrayList<>();
    private static List<Double> doubles = new ArrayList<>();
    private static List originalDictionary = new ArrayList();
    private static List<String> tokens = new ArrayList<>();
    private static int index = 0;

    public static String pack(String json) {
        Object j = JSONUtil.isJsonArray(json) ? new JSONArray(json) : new JSONObject(json);
        Object ast = recursiveAst(j);

        return String.join(JSONConst.OBJECT_DELIMETER, strings) +
                JSONConst.DICT_DELIMETER +
                String.join(JSONConst.OBJECT_DELIMETER, integers.stream().map(String::valueOf).collect(Collectors.toList())) +
                JSONConst.DICT_DELIMETER +
                String.join(JSONConst.OBJECT_DELIMETER, doubles.stream().map(String::valueOf).collect(Collectors.toList())) +
                JSONConst.DICT_DELIMETER +
                recursiveParser(ast);
    }

    public static Object unpack(String packed) {

        String[] rawDicts = StringUtils.splitByWholeSeparatorPreserveAllTokens(packed, JSONConst.DICT_DELIMETER);

        String stringRawDict = rawDicts[0];
        String integerRawDict = rawDicts[1];
        String doubleRawDict = rawDicts[2];

        //String Dict
        if (!StringUtils.isEmpty(stringRawDict)) {
            String[] stringRawDictSplit = StringUtils.split(stringRawDict, JSONConst.OBJECT_DELIMETER);

            for (String aStringRawDictSplit : stringRawDictSplit) {
                originalDictionary.add(JSONUtil.decode(aStringRawDictSplit));
            }
        }

        //Integer Dict
        if (!StringUtils.isEmpty(integerRawDict)) {
            String[] integerRawDictSplit = StringUtils.split(integerRawDict, JSONConst.OBJECT_DELIMETER);

            for (String aIntegerRawDictSplit : integerRawDictSplit) {
                originalDictionary.add(JSONUtil.base36To10(aIntegerRawDictSplit));
            }
        }

        //Double Dict
        if (!StringUtils.isEmpty(doubleRawDict)) {
            String[] doubleRawDictSplit = StringUtils.split(doubleRawDict, JSONConst.OBJECT_DELIMETER);

            for (String aDoubleRawDictSplit : doubleRawDictSplit) {
                originalDictionary.add(Double.parseDouble(aDoubleRawDictSplit));
            }
        }

        //Tokenize Structure
        StringBuilder base36Number = new StringBuilder();
        for (int i = 0; i < rawDicts[3].length(); i++) {
            String token = String.valueOf(rawDicts[3].charAt(i));
            if (tokenChecker(token)) {
                if (!StringUtils.isEmpty(base36Number.toString())) {
                    tokens.add(String.valueOf(JSONUtil.base36To10(base36Number.toString())));
                    base36Number.delete(0, base36Number.length());
                }
                if (!token.equals(JSONConst.OBJECT_DELIMETER)) {
                    tokens.add(token);
                }
            } else {
                base36Number.append(token);
            }
        }


        return recursiveUnpackerParser();
    }

    private static Object recursiveUnpackerParser() {

        String token = tokens.get(index++);

        if (token.equals(JSONConst.DICT_ARRAY_TOKEN)) {
            JSONArray jsonArray = new JSONArray();

            for (; index < tokens.size(); index++) {
                String value = tokens.get(index);

                if (value.equals(JSONConst.END_ARRAY)) {
                    return jsonArray;
                }

                if (value.equals(JSONConst.DICT_ARRAY_TOKEN) ||
                        value.equals(JSONConst.DICT_OBJECT_TOKEN)) {
                    jsonArray.put(recursiveUnpackerParser());
                } else {
                    switch (value) {
                        case JSONConst.TOKEN_TRUE:
                            jsonArray.put(Boolean.TRUE);
                            break;
                        case JSONConst.TOKEN_FALSE:
                            jsonArray.put(Boolean.FALSE);
                            break;
                        case JSONConst.TOKEN_NULL:
                            jsonArray.put("null");
                            break;
                        case JSONConst.TOKEN_EMPTY_STRING:
                            jsonArray.put("");
                            break;
                        default:
                            jsonArray.put(originalDictionary.get(Integer.valueOf(value)));
                    }
                }
            }
            return jsonArray;
        }

        if (token.equals(JSONConst.DICT_OBJECT_TOKEN)) {
            JSONObject jsonObject = new JSONObject();

            for (; index < tokens.size(); index++) {
                String key = tokens.get(index);

                if (key.equals(JSONConst.END_ARRAY)) {
                    return jsonObject;
                }

                if (key.equals(JSONConst.EMPTY_TOKEN_TYPE)) {
                    key = "";
                } else {
                    key = (String) originalDictionary.get(Integer.valueOf(key));
                }

                String value = tokens.get(++index);

                if (value.equals(JSONConst.DICT_ARRAY_TOKEN) ||
                        value.equals(JSONConst.DICT_OBJECT_TOKEN)) {
                    jsonObject.put(key, recursiveUnpackerParser());
                } else {
                    switch (value) {
                        case JSONConst.TOKEN_TRUE:
                            jsonObject.put(key, Boolean.TRUE);
                            break;
                        case JSONConst.TOKEN_FALSE:
                            jsonObject.put(key, Boolean.FALSE);
                            break;
                        case JSONConst.TOKEN_NULL:
                            jsonObject.put(key, "null");
                            break;
                        case JSONConst.TOKEN_EMPTY_STRING:
                            jsonObject.put(key, "");
                            break;
                        default:
                            jsonObject.put(key, originalDictionary.get(Integer.valueOf(value)));
                    }
                }
            }
            return jsonObject;
        }
        return null;
    }

    private static boolean tokenChecker(String token) {
        return token.equals(JSONConstant.OBJECT_DELIMETER.getCharacter()) ||
                token.equals(JSONConstant.DICT_OBJECT_TOKEN.getCharacter()) ||
                token.equals(JSONConstant.DICT_ARRAY_TOKEN.getCharacter()) ||
                token.equals(JSONConstant.END_ARRAY.getCharacter());
    }

    private static Object recursiveAst(Object object) {
        if (null == object) {
            return new Field(JSONConstant.NULL_TOKEN_TYPE, JSONConst.TOKEN_NULL);
        }
        if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            List<Object> list = new ArrayList<>();
            list.add("@");
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(recursiveAst(jsonArray.get(i)));
            }
            return list;
        }

        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            List<Object> list = new ArrayList<>();
            list.add("$");
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                list.add(recursiveAst(key));
                list.add(recursiveAst(jsonObject.get(key)));
            }
            return list;
        }

        if (object instanceof String) {
            String s = (String) object;
            if (StringUtils.isEmpty(s)) {
                return new Field(JSONConstant.EMPTY_TOKEN_TYPE, JSONConst.TOKEN_EMPTY_STRING);
            } else {
                int index = strings.indexOf(s);
                if (index == -1) {
                    strings.add(JSONUtil.encode(s));
                    index = strings.size() - 1;
                }
                return new Field(JSONConstant.STRING_TOKEN_TYPE, String.valueOf(index));
            }
        }

        if (object instanceof Integer) {
            Integer number = (Integer) object;
            int index = integers.indexOf(number);
            if (index == -1) {
                integers.add(number);
                index = integers.size() - 1;
            }
            return new Field(JSONConstant.INTEGER_TOKEN_TYPE, String.valueOf(index));

        }

        if (object instanceof Double) {
            Double number = (Double) object;
            int index = doubles.indexOf(number);
            if (index == -1) {
                doubles.add(number);
                index = doubles.size() - 1;
            }
            return new Field(JSONConstant.DOUBLE_TOKEN_TYPE, String.valueOf(index));

        }

        if (object instanceof Boolean) {
            Boolean bool = (Boolean) object;
            return new Field(JSONConstant.BOOLEAN_TOKEN_TYPE, bool ? JSONConst.TOKEN_TRUE : JSONConst.TOKEN_FALSE);
        }

        return null;
    }

    private static String recursiveParser(Object object) {

        if (object instanceof List) {
            List list = (List) object;
            Object remove = list.remove(0);
            for (Object aList : list) {
                remove += recursiveParser(aList) + JSONConst.OBJECT_DELIMETER;
            }

            String packed = (String) remove;

            boolean equals = String.valueOf(packed.charAt(packed.length() - 1)).equals(JSONConst.OBJECT_DELIMETER);
            return (equals ? packed.substring(0, packed.length() - 1) : packed) + JSONConst.END_ARRAY;
        }

        Field field = (Field) object;

        switch (field.getType()) {
            case STRING_TOKEN_TYPE:
                return JSONUtil.base10To36(field.getIndex());
            case INTEGER_TOKEN_TYPE:
                return JSONUtil.base10To36(String.valueOf(strings.size() + Integer.valueOf(field.getIndex())));
            case DOUBLE_TOKEN_TYPE:
                return JSONUtil.base10To36(String.valueOf(strings.size() + integers.size() + Integer.valueOf(field.getIndex())));
            case BOOLEAN_TOKEN_TYPE:
                return field.getIndex();
            case NULL_TOKEN_TYPE:
                return JSONConst.TOKEN_NULL;
            case EMPTY_TOKEN_TYPE:
                return JSONConst.TOKEN_EMPTY_STRING;
        }
        return null;
    }


}
