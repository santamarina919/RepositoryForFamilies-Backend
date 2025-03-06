package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Events.EventGlance;
import dev.J.RepositoryForFamilies.Lists.ListsGlance;
import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import dev.J.RepositoryForFamilies.Users.UserInfo;

import java.util.List;

/**
 * This interface represents what the user will see when they log into their group
 */
public interface UserHomeDTO {

    GroupDetails getGroupDetails();

    //a list of users for reference for each glance object
    List<UserInfo> getUsers();

    List<EventGlance> getEventGlances();

    List<ListsGlance> getShoppingLists();

    Integer getUsersWaitingApproval();

}
