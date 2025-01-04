package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import dev.J.RepositoryForFamilies.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class GroupsController
{

    private final GroupsService groupsService;


    @PostMapping("/groups/api/creategroup")
    public ResponseEntity<Void> createGroup(EmailPasswordAuthenticationToken auth, @RequestBody CreateGroupBody body){

        groupsService.createGroup(body.getName(), auth.getName());
        return ResponseEntity
                .ok()
                .build();
    }
    /*
    Function returns a list of groups that a user is a member of.
    Used when the user initially logs on so the user chooses which one they want to view or
    the application will default with their default group
    TODO: add notifications for group before user logged back on
    TODO: add whether user is apart of group or not
     */
    @GetMapping("groups/api/listgroups")
    public List<GroupCardDTO> listGroups(EmailPasswordAuthenticationToken auth){
        List<GroupCardDTO> groupList = groupsService.groupsWhereUserIsMember(auth.getEmail());
        return groupList;
    }


    public record JoinRequestBody(String groupId){}

    @PostMapping("groups/api/joingroup")
    public ResponseEntity<String> joinGroup(EmailPasswordAuthenticationToken auth, @RequestBody JoinRequestBody body)
    {
        UUID groupId = UUID.fromString(body.groupId());
        Optional<Groups> group = groupsService.fetchGroup(groupId);
        if(group.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("reason : group not found");
        }

        //TODO: add pre approved list where users with email pre approved emails do not need to wait
        groupsService.joinGroup(groupId, auth.getEmail());

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("groups/api/admin/unapprovedmembers")
    public ResponseEntity<List<UserEmailNameOnly>> fetchUnapprovedMembers(EmailPasswordAuthenticationToken auth, @RequestParam("groupId") String rawGroupId){
        UUID groupId = UUID.fromString(rawGroupId);
        Optional<Groups> group = groupsService.fetchGroup(groupId);
        if(group.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        if(groupsService.fetchMemberType(groupId, auth.getEmail()) != UserType.ADMIN){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        List<UserEmailNameOnly> unapprovedMembers = groupsService.fetchUnapprovedMembers(groupId);

        return ResponseEntity
                .ok(unapprovedMembers);
    }


    public record ApproveRequestBody(String email, UserType approvedLevel){}

    @PostMapping("groups/api/admin/approvemember")
    public ResponseEntity<String> approveMember(EmailPasswordAuthenticationToken auth,
                                              @RequestBody ApproveRequestBody body,
                                              @RequestParam("groupId") String groupIdStr){
        UUID groupId = UUID.fromString(groupIdStr);
        Optional<Groups> group = groupsService.fetchGroup(groupId);
        if(group.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("reason : group not found");
        }


        if (!groupsService.upgradeUserType(group.get().getId(), body.email(),body.approvedLevel())) {
            return ResponseEntity
                    .badRequest()
                    .body("reason : cannot downgrade a user while when using upgrade function");
        }


        return ResponseEntity
                .ok()
                .build();
    }
    public record RejectBody(String email){}
    @PostMapping("groups/api/admin/rejectmember")
    public ResponseEntity<String> rejectUser(EmailPasswordAuthenticationToken auth,
                                             @RequestBody RejectBody body,
                                             @RequestParam("groupId")  String groupIdStr){

        UUID groupId = UUID.fromString(groupIdStr);
        Optional<Groups> group = groupsService.fetchGroup(groupId);
        if(group.isEmpty()){
            return ResponseEntity.badRequest().body("reason : group does not exist");
        }

        if(!groupsService.rejectUser(groupId,body.email())){
            return ResponseEntity.badRequest().body("reason : user is not waiting for approval or does not exist");
        }


        return ResponseEntity
                .ok()
                .build();
    }


    @GetMapping("/groups/api/member/privileges")
    public ResponseEntity<List<Privileges>> getPrivileges(EmailPasswordAuthenticationToken auth,
                                                       @RequestParam("groupId") String groupIdStr) {
        UUID groupId = UUID.fromString(groupIdStr);
        Optional<Groups> group = groupsService.fetchGroup(groupId);
        if(group.isEmpty()){
            ResponseEntity.badRequest().body("{\"reason\" : \"group does not exist\"}");
        }
        UserType userType = groupsService.fetchMemberType(groupId, auth.getEmail());
        //For now assume only two types admin and user
        List<Privileges> privileges = new ArrayList<>(Privileges.getMemberPrivileges());
        if(userType == UserType.ADMIN){
            privileges.addAll(Privileges.getAdminPrivileges());
        }
        return ResponseEntity
                .ok(privileges);
    }









}
