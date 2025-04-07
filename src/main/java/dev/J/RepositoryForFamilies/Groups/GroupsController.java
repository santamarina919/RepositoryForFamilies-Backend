package dev.J.RepositoryForFamilies.Groups;

import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import dev.J.RepositoryForFamilies.Users.UserEmailNameOnly;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class GroupsController
{

    private final GroupsService groupsService;


    public record CreateGroupBody(String groupName){}

    @PostMapping("/groups/api/creategroup")
    public ResponseEntity<Void> createGroup(EmailPasswordAuthenticationToken auth, @RequestBody CreateGroupBody body){

        groupsService.createGroup(body.groupName(), auth.getName());
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
    @GetMapping("/groups/api/listgroups")
    public List<GroupCardDTO> listGroups(EmailPasswordAuthenticationToken auth){
        List<GroupCardDTO> groupList = groupsService.groupsWhereUserIsMember(auth.getEmail());
        return groupList;
    }


    public record JoinRequestBody(String groupId){}

    @PostMapping("/groups/api/joingroup")
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

    @PostMapping("/groups/api/admin/removemember")
    public void removeMember(@RequestParam UUID groupId, @RequestParam String email){
        groupsService.removeMember(groupId,email);
    }


    @PostMapping("groups/api/admin/approvemember")
    public ResponseEntity<String> approveMember(EmailPasswordAuthenticationToken auth,
                                                @RequestParam UUID groupId,
                                                @RequestParam String email){

        if (!groupsService.upgradeUserType(groupId, email,MemberType.CITIZEN)) {
            return ResponseEntity
                    .badRequest()
                    .body("reason : cannot downgrade a user while when using upgrade function");
        }


        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("groups/api/admin/rejectmember")
    public ResponseEntity<String> rejectUser(EmailPasswordAuthenticationToken auth,
                                             @RequestParam String email,
                                             @RequestParam UUID groupId){

        if(!groupsService.rejectUser(groupId,email)){
            return ResponseEntity.badRequest().body("reason : user is not waiting for approval or does not exist");
        }


        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/groups/api/admin/adminmember")
    public ResponseEntity<Void> adminMember(@RequestParam String email, @RequestParam UUID groupId){
        if(!groupsService.upgradeUserType(groupId,email,MemberType.ADMIN)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/groups/api/member/glance")
    public GroupsService.UserHomeDTO fetchHome(@RequestParam("groupId") String groupIdStr, EmailPasswordAuthenticationToken auth){
        return groupsService.fetchGroupDetails(UUID.fromString(groupIdStr),auth);
    }


    @GetMapping("groups/api/admin/unapproved")
    public Integer fetchUnapprovedCount(@RequestParam UUID groupId){
        return groupsService.unapprovedCount(groupId);
    }

    @GetMapping("groups/api/admin/members")
    public List<Member> fetchMembers(@RequestParam UUID groupId){
        return groupsService.fetchMembers(groupId);

    }








}
