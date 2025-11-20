package com.greencross.lims.dao;

import com.greencross.lims.entity.UserActivated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserActivatedDAO implements UserDetailsService {
    @PersistenceContext
    private EntityManager em;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return find(username).orElseThrow(()->new UsernameNotFoundException("Can't find User:" + username));
    }
    public Optional<UserActivated> find(Object id) {
        return Optional.ofNullable(em.find(UserActivated.class, id));
    }
}
