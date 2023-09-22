package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.jwt.TokenInfo;
import com.antk7894.amatda.dto.planner.request.PlannerJoinDto;
import com.antk7894.amatda.dto.planner.request.PlannerLoginDto;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.jwt.JwtTokenProvider;
import com.antk7894.amatda.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlannerService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PlannerRepository plannerRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(readOnly = true)
    public Page<Planner> findAll(Pageable pageable) {
        return plannerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Planner findOneById(Long plannerId) {
        return plannerRepository.findById(plannerId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Planner findOneByEmail(String email) {
        return plannerRepository.findByEmail(email).orElseThrow();
    }

    public Planner saveOne(PlannerJoinDto dto) {
        Planner planner = new Planner(dto.email(), passwordEncoder.encode(dto.password()), UserRole.valueOf(dto.role().toUpperCase()));
        return plannerRepository.save(planner);
    }

    public void removeOne(Long plannerId) {
        plannerRepository.deleteById(plannerId);
    }

    public TokenInfo login(PlannerLoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

}
