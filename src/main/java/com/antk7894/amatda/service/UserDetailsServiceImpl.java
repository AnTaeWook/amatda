package com.antk7894.amatda.service;

import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserDetailsImpl;
import com.antk7894.amatda.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlannerRepository plannerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Planner planner = plannerRepository.findByEmail(username).orElseThrow();
        return createUserDetails(new UserDetailsImpl(planner));
    }

    private UserDetails createUserDetails(UserDetailsImpl userDetail) {
        return User.builder()
                .username(userDetail.getUsername())
                .password(userDetail.getPassword())
                .roles(userDetail.getPlanner().getRole().name())
                .build();
    }

}
