package com.fulaan;

import java.util.*;

/**
 * Created by moslpc on 2016/12/23.
 */
public class SetTest {

    public static void main(String[] args) {

        User user = new User();
        user.setId("123456");

        User user1 = new User();
        user1.setId("123456");

        List<User> list = new ArrayList<User>();
        list.add(user);
        list.add(user1);

        Set<User> set = new TreeSet<User>(new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof User && o2 instanceof User) {
                    return ((User) o1).getId().equals(((User)o2).getId()) ? 0 : 1;
                }
                return 1;
            }
        });
        set.addAll(list);
        list.clear();
        list.addAll(set);

        list.size();
    }
}
