package com.coryrowens.egon;

/**
 * Created by cory on 12/8/2015.
 */
public enum Gender {
    MALE ("Male"), FEMALE ("Female");

    private final String name;

    private Gender(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
