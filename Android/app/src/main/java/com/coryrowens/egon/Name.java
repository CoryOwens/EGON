package com.coryrowens.egon;

import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by cory on 12/7/2015.
 */
public class Name {


    public static final String FORMAT_SPLIT = "[\\s'\\+-]";
    private String timePeriod;
    private String culture;
    private Gender gender;
    private String format;
    private Map<String, String> components;

    public Name() {
        this("");
    }

    public Name(String format) {
        this.format = format;
        this.components = new HashMap<>();
    }

    public static String[] splitFormat(String format) {
        if (format == null) {
            return new String[0];
        }
        return format.split(FORMAT_SPLIT);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public void addComponent(String key, String value) {
        components.put(key, value);
    }

    public String[] getComponentLabels() {
        return splitFormat(format);
    }

    public String getComponent(String key) {
        return components.get(key);
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (format == null) {
            return "";
        }
        int tokStart = 0;
        for (int i = 0; i < format.length(); i++) {
            char next = format.charAt(i);
            if (next == ' ' || next == '\'' || next == '+' || next == '-') {
                String tok = format.substring(tokStart, i);
                String comp = "";
                if (tok == null || tok.length() == 0) {
                    continue;
                } else if (tok.charAt(0) == '"') { // Literal component, not label
                    comp = tok.substring(1, tok.length() - 1);
                } else {
                    comp = getComponent(tok);
                }
                sb.append(comp);
                tokStart = i + 1;
                if (next == ' ' || next == '\'' || next == '-') {
                    sb.append(next);
                } else if (next == '+') {
                    // Don't append - '+' is special symbol for fused components
                }
            }
        }
        String tok = format.substring(tokStart, format.length());
        String comp = "";
        if (tok == null || tok.length() == 0) {
            // Ignore
        } else if (tok.charAt(0) == '"') { // Literal component, not label
            comp = tok.substring(1, tok.length());
        } else {
            comp = getComponent(tok);
        }
        sb.append(comp);
        return sb.toString();
    }
}
