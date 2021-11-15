package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public User authenticated() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(email);
            return user;
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid User");
        }
    }

    public void validateSelfOrOther(Long id) {
        User user = authenticated();
        if (user.getId() != id) {
            throw new ForbiddenException("Acess Denied");
        }
    }


}
