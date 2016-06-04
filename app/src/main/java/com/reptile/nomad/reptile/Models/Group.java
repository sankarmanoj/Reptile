package com.reptile.nomad.reptile.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sankarmanoj on 04/06/16.
 */
public class Group {
    public String name;
    public User creator;
    public List<User> members;
    public Group(String name,User creator)
    {
        this.name = name;
        this.creator = creator;
        members = new ArrayList<>();

    }
    public void addMember(User member)
    {
        members.add(member);
    }


}
