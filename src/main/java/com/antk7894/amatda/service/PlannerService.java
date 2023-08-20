package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.jwt.TokenInfo;
import com.antk7894.amatda.dto.planner.PlannerJoinRequestDto;
import com.antk7894.amatda.dto.planner.PlannerLoginRequestDto;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.jwt.JwtTokenProvider;
import com.antk7894.amatda.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlannerRepository plannerRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Planner saveOne(PlannerJoinRequestDto dto) {
        Planner planner = new Planner(dto.email(), passwordEncoder.encode(dto.password()), UserRole.valueOf(dto.role().toUpperCase()));
        return plannerRepository.save(planner);
    }

    public void removeOne(Long plannerId) {
        plannerRepository.deleteById(plannerId);
    }

    @Transactional
    public TokenInfo login(PlannerLoginRequestDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

}
