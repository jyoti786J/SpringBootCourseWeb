package hkmu.wadd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {  //"/{lectureId}/comment/{commentId}/delete"
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public paths
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login?registered").permitAll()
                        .requestMatchers("/course/index").permitAll()

                        // Profile paths
                        .requestMatchers("/view/{username}").authenticated()
                        .requestMatchers("/view/{username}/edit").authenticated()
                        .requestMatchers("/view/{username}/votes").authenticated()
                        .requestMatchers("/view/{username}/comments").authenticated()
                        .requestMatchers("/view/{username}/comments/{commentId}/delete").hasAnyRole("ADMIN", "TEACHER")

                        // Admin paths
                        .requestMatchers("/admin/users/**").hasRole("ADMIN")

                        // Course paths
                        .requestMatchers("/course/{courseId}/lecture/add").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/lecture/{lectureId}/edit").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/lecture/{lectureId}/delete").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/lecture/{lectureId}/note/**").hasAnyRole("ADMIN", "TEACHER")

                        // Poll paths
                        .requestMatchers("/course/{courseId}/poll/create").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/poll/{pollId}/edit").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/poll/{pollId}/delete").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/course/{courseId}/poll/{pollId}/comment/{commentId}/delete").hasAnyRole("ADMIN", "TEACHER")

                        // Authenticated paths
                        .requestMatchers("/course/{courseId}/lecture/{lectureId}").authenticated()
                        .requestMatchers("/course/{courseId}/poll/{pollId}").authenticated()
                        .requestMatchers("/course/{courseId}/poll/{pollId}/vote").authenticated()
                        .requestMatchers("/course/{courseId}/poll/{pollId}/comment").authenticated()

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                )
                .httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}