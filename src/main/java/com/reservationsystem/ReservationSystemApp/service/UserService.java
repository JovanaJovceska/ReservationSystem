package com.reservationsystem.ReservationSystemApp.service;

import com.reservationsystem.ReservationSystemApp.model.Authorities;
import com.reservationsystem.ReservationSystemApp.model.User;
import com.reservationsystem.ReservationSystemApp.dto.UserDto;
import com.reservationsystem.ReservationSystemApp.repository.AuthorityRepository;
import com.reservationsystem.ReservationSystemApp.repository.UserRepository;
import com.reservationsystem.ReservationSystemApp.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthorityRepository authorityRepo;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    public void createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());

        String encodedPassword = customPasswordEncoder.getPasswordEncoder().encode(userDto.getPassword());
        newUser.setPassword(encodedPassword);

        userRepo.save(newUser);
        Authorities authority = new Authorities();
        authority.setAuthority("ROLE_USER");
        authority.setUser(newUser);
        authorityRepo.save(authority);
    }

    public Long getUserId(String username) {
        return userRepo.getUserId(username);
    }
}
