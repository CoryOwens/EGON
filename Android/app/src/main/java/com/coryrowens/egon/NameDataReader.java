package com.coryrowens.egon;

import android.content.res.Resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by cory on 12/8/2015.
 */
public class NameDataReader {

    private Resources res;

    public NameDataReader(Resources res) {
        this.res = res;
    }

    public Set<String> getTimePeriodOptions() {
        Set<String> timePeriods = new HashSet<>();
        InputStream is = res.openRawResource(R.raw.time_period_list);
        Scanner lineScanner = new Scanner(is);
        while (lineScanner.hasNextLine()) {
            timePeriods.add(lineScanner.nextLine());
        }
        return timePeriods;
    }

    public Set<String> getCultureOptions() {
        Set<String> cultures = new HashSet<>();
        InputStream is = res.openRawResource(R.raw.culture_list);
        Scanner lineScanner = new Scanner(is);
        while (lineScanner.hasNextLine()) {
            cultures.add(lineScanner.nextLine());
        }
        return cultures;
    }

    //TODO: Actually get from data
    public Set<String> getFormats(String timePeriod, String culture, Gender gender) {
        String filename = culture;
        if (timePeriod != null) {
            filename += "_" + timePeriod;
        }
        filename += "_" + gender + "_format";
        filename = filename.toLowerCase();
        int id = res.getIdentifier(filename, "raw", "com.coryrowens.egon");
        if (id == 0 && timePeriod != null) {
            return getFormats(null, culture, gender);
        } else if (id == 0 && timePeriod == null) {
            return new HashSet<>();
        }
        InputStream is = res.openRawResource(id);
        Scanner lineScanner = new Scanner(is);
        Set<String> formats = new HashSet<>();
        while (lineScanner.hasNextLine()) {
            String format = lineScanner.nextLine();
            formats.add(format);
        }
        return formats;
    }

    //TODO: Actually get from data
    public Map<String, Set<String>> getNames(String timePerdiod, String culture, Gender gender) {
        Set<String> formats = getFormats(timePerdiod, culture, gender);
        Map<String, Set<String>> names = new HashMap<>();
        for (String format : formats) {
            String[] components = Name.splitFormat(format);
            for (String component : components) {
                Set<String> nameSet = getNamesByComponent(timePerdiod, culture, gender, component);
                names.put(component, nameSet);
            }
        }
        return names;
    }

    public Set<String> getNamesByComponent(String timePeriod, String culture, Gender gender, String component) {
        Set<String> names = new HashSet<>();
        if (component.charAt(0) == '"') {
            String literal = component.substring(1, component.length() - 1);
            names.add(literal);
            return names;
        }
        if (Character.isDigit(component.charAt(component.length() - 1))) {
            component = component.replaceAll("[0-9]", "");
        }
        String filename = culture;
        if (timePeriod != null) {
            filename += "_" + timePeriod;
        }
        if (component.contains("gendered")) {
            String trimmedComp = component.replace("gendered", "");
            filename += "_" + gender + "_" + trimmedComp;
        } else if (component.contains("male")) {
            String trimmedComp = component.replace("male", "");
            filename += "_" + Gender.MALE + "_" + trimmedComp;
        } else if (component.contains("female")) {
            String trimmedComp = component.replace("female", "");
            filename += "_" + Gender.FEMALE + "_" + trimmedComp;
        } else {
            filename += "_" + component;
        }
        filename = filename.toLowerCase();
        int id = res.getIdentifier(filename, "raw", "com.coryrowens.egon");
        if (id == 0 && timePeriod != null) {
            return getNamesByComponent(null, culture, gender, component);
        } else if (id == 0 && timePeriod == null) {
            return names;
        }
        InputStream is = res.openRawResource(id);
        Scanner scanner = new Scanner(is);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> tokens = Arrays.asList(line.split(", "));
            names.addAll(tokens);
        }
        return names;
    }
}
