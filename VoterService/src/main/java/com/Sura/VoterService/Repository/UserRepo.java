package com.Sura.VoterService.Repository;


import com.Sura.VoterService.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users,Long> {
    Boolean existsByUsername(String username);

//    List<Users> findByUsername(String username);
    Users findByUsername(String username);
}
