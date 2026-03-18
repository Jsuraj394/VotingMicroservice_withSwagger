package com.Sura.CandidateService.Repository;

import com.Sura.CandidateService.Entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<Users,Long> {
    Boolean existsByUsername(String username);

//    List<Users> findByUsername(String username);
    Users findByUsername(String username);
}
