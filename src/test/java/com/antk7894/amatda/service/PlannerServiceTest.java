package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.planner.request.PlannerJoinDto;
import com.antk7894.amatda.dto.planner.request.PlannerLoginDto;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.repository.PlannerRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PlannerServiceTest {

    @Autowired
    private PlannerService plannerService;

    @Autowired
    private PlannerRepository plannerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        plannerRepository.save(new Planner("email1", passwordEncoder.encode("password"), UserRole.USER));
        plannerRepository.save(new Planner("email2", passwordEncoder.encode("password"), UserRole.USER));
        plannerRepository.save(new Planner("email3", passwordEncoder.encode("password"), UserRole.USER));
    }

    @AfterEach
    void tearDown() {
        plannerRepository.deleteAll();
    }

    @Test
    @DisplayName("모든 사용자를 조회한다")
    void findAll() {
        // given & when
        Page<Planner> planners = plannerService.findAll(Pageable.ofSize(10));

        // then
        assertThat(planners.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("ID를 통해 단일 사용자를 조회한다")
    void findOneById() {
        // given
        Planner planner = plannerRepository.save(new Planner("email", "password", UserRole.USER));

        // when
        Planner founded = plannerService.findOneById(planner.getPlannerId());

        // then
        assertThat(founded.getPlannerId()).isEqualTo(planner.getPlannerId());
    }

    @Test
    @DisplayName("존재하지 않는 ID를 통해 조회할 경우 NoSuchElementException 예외를 던진다")
    void findOneByIdFail() {
        assertThatThrownBy(() -> plannerService.findOneById(-1L))
                .isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("이메일을 통해 단일 사용자를 조회한다")
    void findOneByEmail() {
        // given
        Planner planner = plannerRepository.save(new Planner("email", "password", UserRole.USER));

        // when
        Planner founded = plannerService.findOneByEmail(planner.getEmail());

        // then
        assertThat(founded.getEmail()).isEqualTo(planner.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 이메일을 통해 조회할 경우 NoSuchElementException 예외를 던진다")
    void findOneByEmailFail() {
        assertThatThrownBy(() -> plannerService.findOneByEmail("IvalidEmail"))
                .isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("단일 사용자를 저장한다")
    void saveOne() {
        // given
        Planner planner = plannerService.saveOne(new PlannerJoinDto("email", "password", "USER"));

        // when
        Planner founded = plannerRepository.findByEmail(planner.getEmail()).orElseThrow();

        // then
        assertThat(founded.getPlannerId()).isEqualTo(planner.getPlannerId());
    }

    @Test
    @DisplayName("단일 사용자를 삭제한다")
    void removeOne() {
        // given
        Planner planner = plannerService.findOneByEmail("email1");

        // when
        plannerService.removeOne(planner.getPlannerId());
        Page<Planner> planners = plannerService.findAll(Pageable.ofSize(10));

        // then
        assertThat(planners.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자의 정보를 통해 로그인을 한다")
    void login() {
        assertThatCode(() -> plannerService.login(new PlannerLoginDto("email1", "password")))
                .doesNotThrowAnyException();
    }

}