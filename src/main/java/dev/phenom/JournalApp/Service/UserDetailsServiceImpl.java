package dev.phenom.JournalApp.Service;
import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User name = userRepository.findByUserName(username);
    if (name!=null) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(name.getUserName())
                .password(name.getPassword())
                .roles(name.getRoles().toArray(new String[0]))
                .build();
    }
    throw new UsernameNotFoundException("User not found");
    }
}
