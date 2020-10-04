package ru.abtank.springsecurity.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import ru.abtank.springsecurity.servises.UserService;

import javax.sql.DataSource;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/only_for_admins/**").hasRole("ADMIN")
                .antMatchers("/read_profile/**").hasAuthority("READ_PROFILE")
//                .antMatchers("/admin/**").hasAnyRole("ADMIN","SUPERADMIN")
//                .antMatchers("/profile/**").authenticated()
                .and()
//                .httpBasic() // сразу в корень
                .formLogin()//стандартная форма
//                .loginProcessingUrl("/hellologin")  // указываем свой путь запроса
//                .successHandler()  // добавляем хандлер для обработки при успехе
                .and()
                .logout().logoutSuccessUrl("/");
//                .and().csrf().disable(); //если аутонтификация по REST
    }
//        -------In-memory-------
//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2y$12$NBZ55hMDUILLVyC1pMVQ3uIaxH95D2Hi0zDgJpBEsoK1ADUc3eXBW")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2y$12$NBZ55hMDUILLVyC1pMVQ3uIaxH95D2Hi0zDgJpBEsoK1ADUc3eXBW")
//                .roles("USER","ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }

//    -------jdbc-authentication--------

    @Bean
    public JdbcUserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("c")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2y$12$NBZ55hMDUILLVyC1pMVQ3uIaxH95D2Hi0zDgJpBEsoK1ADUc3eXBW")
//                .roles("USER","ADMIN")
//                .build();
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//        if(manager.userExists(user.getUsername())){
//            manager.deleteUser(user.getUsername());
//        }
//        if(manager.userExists(admin.getUsername())){
//            manager.deleteUser(admin.getUsername());
//        }
//        manager.createUser(user);
//        manager.createUser(admin);
        return manager;
    }

    //    ---------DAO-authentication----------
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
