package com.gwi.phr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainDemo {
    public static void main(String args[]) {
        List<People> targetList = new ArrayList<People>();
        for (int i = 0; i < 10; i++) {
            targetList.add(getPeople());
        }
        System.out.println(Arrays.toString(targetList.toArray()));
        DisjointSet<People> set = new DisjointSet<People>();

        for (int i=0;i<targetList.size();i++) {
            set.makeSet(targetList.get(i));
        }
        int cursor=1;
        List<People> resultList = new ArrayList<People>();
        for (int i = 0; i < targetList.size(); i++) {
            People master = targetList.get(i);
            resultList.add(getDefaultP());
            for (int j = cursor; j < targetList.size(); j++) {
                People slave = targetList.get(j);
                if (master .sex == slave.sex) {
                    resultList.add(slave);
                    //set.union(master, slave);
                } else {
                    cursor = j;
                    i = j;
                }

            }
        }
//        set.toList(resultList);
//        System.out.println(Arrays.toString(resultList.toArray()));

    }



    static class People {
        int sex;
        String name;

        @Override
        public String toString() {
            return "People{" +
                    "sex=" + sex +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    static People getPeople() {
        People people = new People();
        people.name = "Name"+ new Random().nextInt(2000);
        people.sex = new Random().nextInt(10);

        return people;
    }

    static People getDefaultP() {
        People people = new People();
        people.name = "Name Deamon";
        people.sex = -110;

        return people;
    }

}
