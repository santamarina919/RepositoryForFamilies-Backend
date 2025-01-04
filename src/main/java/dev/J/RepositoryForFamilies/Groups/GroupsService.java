package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import dev.J.RepositoryForFamilies.Users.UserRepository;
import dev.J.RepositoryForFamilies.Users.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class GroupsService
{
    private final GroupsRepository groupsRepo;

    private final UserRepository userRepo;


    @Transactional
    public void createGroup(String group_name, String email){
        UUID groupId = UUID.randomUUID();
        groupsRepo.createGroup(groupId,group_name, email);
        groupsRepo.addGroupMember(groupId,email,UserType.OWNER.name());
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
        List<UserEmailNameOnly> list = userRepo.fetchUnapprovedMembers(groupId);
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
}
