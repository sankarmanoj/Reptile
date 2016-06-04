package com.reptile.nomad.reptile.Models;

<<<<<<< HEAD
import com.reptile.nomad.reptile.Reptile;

=======
>>>>>>> origin/master
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
    public Group(String name)
    {
        this.name = name;
        this.creator = Reptile.mUser;

        members = new ArrayList<>();

    }
    public void addMember(User member)
    {
        members.add(member);
    }


}
