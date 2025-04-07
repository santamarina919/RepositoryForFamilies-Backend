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
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
public class AdminFilter extends OncePerRequestFilter {

    private final GroupsRepository groupsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HashSet<String> uriComponents = new HashSet<>(Arrays.asList(request.getRequestURI().split("/")));

        if(uriComponents.contains("admin")) {
            EmailPasswordAuthenticationToken auth = (EmailPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String groupIdStr = request.getParameter("groupId");
            UUID groupId = UUID.fromString(groupIdStr);
            MemberType type = groupsRepository.fetchMemberType(groupId,auth.getEmail());
            if(type.ordinal() < MemberType.ADMIN.ordinal()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,"Sneaky Sneaky you must be an admin to do this");
                return;//REJECT REQUEST
            }
        }


        filterChain.doFilter(request, response);
    }
}
