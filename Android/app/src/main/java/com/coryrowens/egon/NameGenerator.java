package com.coryrowens.egon;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by cory on 12/8/2015.
 */
public class NameGenerator {

    private Set<String> timePeriodOptions, cultureOptions;
    private Set<String> timePeriods, cultures;
    private Set<Gender> genders;
    // Maps combination of timePeriod and culture to a set of nameFormatStrings
    private Map<String, Set<String>> formats;
    // Maps combination of timePeriod and culture to a Map, which maps nameFormatStrings component to sets of Names
    private Map<String, Map<String, Set<String>>> nameData;
    private NameDataReader nameDataReader;
    private Random random;
    private Resources res;

    public NameGenerator(NameDataReader nameDataReader) {
        this.nameDataReader = nameDataReader;
        timePeriodOptions = nameDataReader.getTimePeriodOptions();
        cultureOptions = nameDataReader.getCultureOptions();
        timePeriods = new HashSet<>();
        for (String t : timePeriodOptions) {
            timePeriods.add(t);
        }
        // Default to all options;
        cultures = new HashSet<>();
        for (String t : cultureOptions) {
            cultures.add(t);
        }
        genders = new HashSet<>();
        genders.add(Gender.FEMALE);
        genders.add(Gender.MALE);
        formats = new HashMap<>();
        nameData = new HashMap<>();
        random = new Random();
    }

    public Set<String> getTimePeriodOptions() {
        return timePeriodOptions;
    }

    public Set<String> getCultureOptions() {
        return cultureOptions;
    }

    public Set<String> getTimePeriods() {
        return timePeriods;
    }

    public void setTimePeriods(Set<String> timePeriods) {
        clearTimePeriods();
        for (String tp : timePeriods) {
            toggleTimePeriod(tp);
        }
    }

    public Set<String> getCultures() {
        return cultures;
    }

    public void setCultures(Set<String> cultures) {
        clearCultures();
        for (String c : cultures) {
            toggleCulture(c);
        }
    }

    public Set<Gender> getGenders() {
        return genders;
    }

    public void setGenders(Set<Gender> genders) {
        clearGenders();
        for (Gender g : genders) {
            toggleGender(g);
        }
    }

    public void clearTimePeriods() {
        timePeriods.clear();
    }

    public boolean toggleTimePeriod(String period) {
        if (!timePeriodOptions.contains(period)) {
            throw new IllegalArgumentException("Unrecognized Time Period: " + period);
        }
        if (timePeriods.contains(period)) {
            timePeriods.remove(period);
            return false;
        } else {
            timePeriods.add(period);
            return true;
        }
    }

    public void clearCultures() {
        cultures.clear();
    }


    public boolean toggleCulture(String culture) {
        if (!cultureOptions.contains(culture)) {
            throw new IllegalArgumentException("Unrecognized Culture: " + culture);
        }
        if (cultures.contains(culture)) {
            cultures.remove(culture);
            return false;
        } else {
            cultures.add(culture);
            return true;
        }
    }

    public void clearGenders() {
        genders.clear();
    }

    public boolean toggleGender(Gender gender) {
        if (genders.contains(gender)) {
            genders.remove(gender);
            return false;
        } else {
            genders.add(gender);
            return true;
        }
    }

    public Set<String> getFormats(String timePeriod, String culture, Gender gender) {
        String combination = culture+"_"+timePeriod+"_"+gender;
        if (formats.containsKey(combination)) {
            return formats.get(combination);
        } else {
            Set<String> formatSet = nameDataReader.getFormats(timePeriod, culture, gender);
            formats.put(combination, formatSet);
            return formatSet;
        }
    }

    public Name generate() {
        Name name = new Name();
        if (timePeriods.size() == 0 || cultures.size() == 0 || genders.size() == 0) {
            return name;
        }
        String[] tpArray = timePeriods.toArray(new String[timePeriods.size()]);
        String timePeriod = tpArray[random.nextInt(tpArray.length)];
        name.setTimePeriod(timePeriod);
        String[] cArray = cultures.toArray(new String[cultures.size()]);
        String culture = cArray[random.nextInt(cArray.length)];
        name.setCulture(culture);
        Gender[] gArray = genders.toArray(new Gender[genders.size()]);
        Gender gender = gArray[random.nextInt(gArray.length)];
        name.setGender(gender);
        Set<String> formatSet = getFormats(timePeriod, culture, gender);
        String[] fArray = formatSet.toArray(new String[formatSet.size()]);
        String format = fArray[random.nextInt(fArray.length)];
        name.setFormat(format);
        String[] labels = name.getComponentLabels();
        Map<String, Set<String>> names = getNames(timePeriod, culture, gender);
        for (String label : labels) {
            Set<String> nameSet = names.get(label);
            if (nameSet == null) {
                continue;
            }
            String[] nameArray = nameSet.toArray(new String[nameSet.size()]);
            String comp = nameArray[random.nextInt(nameArray.length)];
            name.addComponent(label, comp);
        }
        return name;
    }

    private Map<String, Set<String>> getNames(String timePeriod, String culture, Gender gender) {
        String combination = culture+"_"+timePeriod+"_"+gender;
        if (nameData.containsKey(combination)) {
            return nameData.get(combination);
        } else {
            Map<String, Set<String>> names = nameDataReader.getNames(timePeriod, culture, gender);
            nameData.put(combination, names);
            return names;
        }
    }
}
