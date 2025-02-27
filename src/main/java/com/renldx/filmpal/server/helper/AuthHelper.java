package com.renldx.filmpal.server.helper;

import com.renldx.filmpal.server.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    public UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public long getUserId() {
        return getUserDetails().getId();
    }

}
