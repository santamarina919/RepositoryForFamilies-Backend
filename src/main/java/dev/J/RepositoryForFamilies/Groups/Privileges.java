package dev.J.RepositoryForFamilies.Groups;

import lombok.Getter;

import java.util.*;


public enum Privileges
{
    LISTS(UserType.CITIZEN),

    ACCEPT_MEMBERS(UserType.ADMIN);

    private UserType type;

    Privileges(UserType type){
        this.type = type;
    }


    private final static ArrayList<Privileges> adminPrivileges = new ArrayList<Privileges>();

    static {
        Privileges[] privs = Privileges.values();
        for(Privileges priv : privs){
            if(priv.type == UserType.ADMIN){
                adminPrivileges.add(priv);
            }
        }
    }


    private final static List<Privileges> memberPrivileges = new ArrayList<Privileges>();

    static {
        Privileges[] privs = Privileges.values();
        for(Privileges priv : privs){
            if(priv.type == UserType.CITIZEN){
                adminPrivileges.add(priv);
            }
        }
    }


    public static List<Privileges> getAdminPrivileges() {
        return Collections.unmodifiableList(adminPrivileges);
    }

    public static List<Privileges> getMemberPrivileges() {
        return Collections.unmodifiableList(memberPrivileges);
    }

}
