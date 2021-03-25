package org.intership.intern;

import java.util.Comparator;

public class StudentComparator implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        final int compareStrings = o1.getName().compareTo(o2.getName());
        if(compareStrings == 0) return o2.getDateOfBirth().compareTo(o1.getDateOfBirth());
        return compareStrings;
    }
}
