package org.example.seminar2;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HomeWork {
    public static void main(String[] args) {
        Person rndPerson = ObjectCreator.createObj(Person.class);

        System.out.println("age1 = " + rndPerson.firstDate);
        System.out.println("age2 = " + rndPerson.secondDate);
    }



    public static class Person {

        @RandomDate
        private Date firstDate;

        @RandomDate(min = 1704067200020L, max = 1735689608234L)
        private Date secondDate;

    }
}