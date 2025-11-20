package com.greencross.lims.dao;

import com.greencross.lims.entity.readonly.UserActivated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserActivatedDAO extends AbstractJpaDAO<UserActivated> implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return find(username).orElseThrow(()->new UsernameNotFoundException("Can't find User:" + username));
    }
}
