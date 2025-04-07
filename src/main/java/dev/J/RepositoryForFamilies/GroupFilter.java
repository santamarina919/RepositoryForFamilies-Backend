package dev.J.RepositoryForFamilies;

import dev.J.RepositoryForFamilies.Groups.GroupsRepository;
import dev.J.RepositoryForFamilies.Groups.MemberType;
import dev.J.RepositoryForFamilies.Users.EmailPasswordAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GroupFilter extends OncePerRequestFilter
{
    private final GroupsRepository groupsRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        //TODO: find out why i need to set content length to this number specifically
        int contentLength = 100000;
        HashSet<String> uriComponents = new HashSet<>(List.of(request.getRequestURI().split("/")));
        //Not a endpoint we need to check
        if(!uriComponents.contains("member") && !uriComponents.contains("admin")) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> groupIdStr = Optional.ofNullable(request.getParameter("groupId"));
        if(groupIdStr.isEmpty()) {
            String errorStr = "Missing groupId";
            response.setContentLength(contentLength);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorStr);
            return;
        }
        EmailPasswordAuthenticationToken auth = (EmailPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UUID groupId = UUID.fromString(groupIdStr.get());

        if(!groupsRepo.isMember(groupId,auth.getEmail())){
            String errorStr = "Not a member";
            response.setContentLength(contentLength);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, errorStr);
            return;
        }

        MemberType type = groupsRepo.fetchMemberType(groupId, auth.getEmail());
        if(type == MemberType.UNAUTHORIZED) {
            String errorStr = "Not an approved member";
            response.setContentLength(contentLength);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorStr);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
