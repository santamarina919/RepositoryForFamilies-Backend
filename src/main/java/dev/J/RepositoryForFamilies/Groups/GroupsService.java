package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Events.EventRepository;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import dev.J.RepositoryForFamilies.Users.UserInfo;
import dev.J.RepositoryForFamilies.Users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    
    private final MemberRepository memberRepo;

    @Transactional
    public void createGroup(String group_name, String email){
        UUID groupId = UUID.randomUUID();
        groupsRepo.createGroup(groupId,group_name, email);
        groupsRepo.addGroupMember(groupId,email, MemberType.ADMIN.name());
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
        groupsRepo.addGroupMember(groupId,email, MemberType.UNAUTHORIZED.name());
    }

    @Transactional
    public boolean upgradeUserType(UUID groupId, String email, MemberType upgradeType){

        MemberType currentMemberType = groupsRepo.fetchMemberType(groupId,email);
        //Comparing using idicies a little nasty
        if(upgradeType.ordinal() <= currentMemberType.ordinal()){
            return false;
        }
        groupsRepo.updateMemberType(groupId,email,upgradeType.name());
        return true;
    }

    public List<UserEmailNameOnly> fetchUnapprovedMembers(UUID groupId){
        List<UserEmailNameOnly> list = userRepo.fetchUnapprovedMembers(groupId,UserEmailNameOnly.class);
        return list;
    }

    public MemberType fetchMemberType(UUID groupId, String email){
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

    public Integer unapprovedCount(UUID groupId) {
        return groupsRepo.fetchUnapprovedCount(groupId);
    }

    public List<Member> fetchMembers(UUID groupId) {
        List<Member> members = memberRepo.fetchMembers(groupId);
        return members;
    }

    public interface UserHomeDTO {

        GroupDetails getGroupDetails();

        //a list of users for reference for each glance object
        List<UserInfo> getUsers();

        MemberType getUserType();

    }

    public UserHomeDTO fetchGroupDetails(UUID groupId, EmailPasswordAuthenticationToken auth) {

        List<UserInfo> userInfos = userRepo.findUsersInGroup(groupId, UserInfo.class);
        GroupDetails groupDetails = groupsRepo.findById(groupId,GroupDetails.class);
        MemberType currMemberType = groupsRepo.fetchMemberType(groupId,auth.getEmail());
            return new UserHomeDTO(){
                @Override
                public List<UserInfo> getUsers() {
                    return userInfos;
                }


                @Override
                public GroupDetails getGroupDetails() {
                    return groupDetails;
                }

                @Override
                public MemberType getUserType() { return currMemberType; }
            };
    }

    @Transactional
    public void removeMember(UUID groupId, String email){
        groupsRepo.removeMember(groupId,email);
        //TODO remove everything about that user from the group
    }

}
