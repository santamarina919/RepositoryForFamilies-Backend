package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Events.EventRepository;
import dev.J.RepositoryForFamilies.Events.EventGlance;
import dev.J.RepositoryForFamilies.Lists.ListsGlance;
import dev.J.RepositoryForFamilies.Lists.ListsRepository;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import dev.J.RepositoryForFamilies.Users.UserInfo;
import dev.J.RepositoryForFamilies.Users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class GroupsService
{
    private final GroupsRepository groupsRepo;

    private final UserRepository userRepo;

    private final EventRepository eventRepo;

    private final ListsRepository listsRepo;

    @Transactional
    public void createGroup(String group_name, String email){
        UUID groupId = UUID.randomUUID();
        groupsRepo.createGroup(groupId,group_name, email);
        groupsRepo.addGroupMember(groupId,email,UserType.ADMIN.name());
    }

    public List<GroupCardDTO> groupsWhereUserIsMember(String email)
    {
        return groupsRepo.groupsWhereUserIsMember(email);
    }

    public Optional<Groups> fetchGroup(UUID groupId)
    {
        return groupsRepo.findById(groupId);
    }

    @Transactional
    public void joinGroup(UUID groupId, String email)
    {
        //TODO add check for user that is already in group or already requested to join
        groupsRepo.addGroupMember(groupId,email,UserType.UNAUTHORIZED.name());
    }

    @Transactional
    public boolean upgradeUserType(UUID groupId, String email, UserType upgradeType){

        UserType currentUserType = groupsRepo.fetchMemberType(groupId,email);
        //Comparing using idicies a little nasty
        if(upgradeType.ordinal() <= currentUserType.ordinal()){
            return false;
        }
        groupsRepo.updateMemberType(groupId,email,upgradeType.name());
        return true;
    }

    public List<UserEmailNameOnly> fetchUnapprovedMembers(UUID groupId){
        List<UserEmailNameOnly> list = userRepo.fetchUnapprovedMembers(groupId,UserEmailNameOnly.class);
        return list;
    }

    public UserType fetchMemberType(UUID groupId, String email){
        return groupsRepo.fetchMemberType(groupId,email);
    }

    @Transactional
    public boolean rejectUser(UUID groupId, String email)
    {
        groupsRepo.removeMember(groupId,email);

        return true;
    }

    public boolean userIsApartOfGroup(UUID groupId, String email){
        return groupsRepo.isMember(groupId,email);
    }

    public UserHomeDTO fetchHome(UUID groupId) {
        List<EventGlance> events = eventRepo.findAllByGroupId(groupId, EventGlance.class);
        List<UserInfo> userInfos = userRepo.findUsersInGroup(groupId, UserInfo.class);
        GroupDetails groupDetails = groupsRepo.findById(groupId,GroupDetails.class);
        EmailPasswordAuthenticationToken auth = (EmailPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return new UserHomeDTO(){
                @Override
                public List<UserInfo> getUsers() {
                    return userInfos;
                }

                @Override
                public List<EventGlance> getEventGlances() {
                    return events;
                }

                @Override
                public GroupDetails getGroupDetails() {
                    return groupDetails;
                }

                @Override
                public List<ListsGlance> getShoppingLists(){
                    return  listsRepo.findByGroupId(groupId,ListsGlance.class);
                }

                @Override
                public Integer getUsersWaitingApproval(){
                    UserType type = groupsRepo.fetchMemberType(groupId,auth.getEmail());
                    if(type == UserType.ADMIN){
                        return fetchUnapprovedMembers(groupId).size();
                    }
                    else {
                        return null;
                    }
                }
            };
    }
}
