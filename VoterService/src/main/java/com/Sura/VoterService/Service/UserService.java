package com.Sura.VoterService.Service;


import com.Sura.VoterService.Entity.UserPrincipal;
import com.Sura.VoterService.Entity.Users;
import com.Sura.VoterService.Exception.ResourceNotFoundException;
import com.Sura.VoterService.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService  {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Users register(Users users) {
        Boolean exists =  userRepo.existsByUsername(users.getUsername());
        if(exists) {
            throw new RuntimeException("User with username " + users.getUsername() + " already exists.");
        }
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepo.save(users);
    }


    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public Users getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public Users getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Users updateUser(Users users) {
        if (!userRepo.existsById(users.getUserId())) {
            throw new ResourceNotFoundException("User with ID " + users.getUserId() + " not found.");
        }
        Users existingUser = userRepo.findById(users.getUserId()).orElse(null);
        if (existingUser != null && !existingUser.getUsername().equals(users.getUsername())) {
            existingUser.setUsername(users.getUsername());
        }
        if (existingUser != null && !existingUser.getPassword().equals(encoder.encode(users.getPassword()))) {
            existingUser.setPassword(encoder.encode(users.getPassword()));
        }
        if (existingUser != null && !existingUser.getRoles().equals(users.getRoles())) {
            existingUser.setRoles(users.getRoles());
        }
        return userRepo.save(existingUser);
    }

    public String deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " not found.");
        }
        userRepo.deleteById(id);
        return "User with ID " + id + " has been deleted.";
    }



//    public String verify(Users user) {
//        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
//        System.out.println("in the service verify layer");
//        // creating an authentication token using the username and password provided by the user and passing it to the authentication manager for authentication
//        return auth.isAuthenticated() ? jwtService.generateToken(user.getUsername()) : "Authentication failed";  // returning a simple message based on the authentication result
//    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user =  userRepo.findByUsername(username);
        System.out.println("User found: " + user);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserPrincipal(user);
    }
}
