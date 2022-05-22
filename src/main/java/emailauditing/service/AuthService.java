package emailauditing.service;

import com.sun.mail.smtp.SMTPMessage;
import emailauditing.entity.Role;
import emailauditing.entity.enums.RoleName;
import emailauditing.payload.LoginDto;
import emailauditing.repository.RoleRepository;
import emailauditing.repository.UserRepository;
import emailauditing.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import emailauditing.entity.User;
import emailauditing.payload.ApiResponse;
import emailauditing.payload.RegisterDto;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean exists = userRepository.existsByEmail(registerDto.getEmail());
        if (exists) {
            return new ApiResponse(
                    "This username: " + registerDto.getEmail() + " is already taken"
                    , false);
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));

        user.setCode(UUID.randomUUID().toString());

        userRepository.save(user);
//      Saving user is finished

//      Sending code to email for verifying account
        Boolean isEmailSent = sendEmail(user.getEmail(), user.getCode());
        return new ApiResponse(
                "You have successfully registered. Now confirm your email account",
                isEmailSent);
    }

    public Boolean sendEmail(String sendingEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mailsender1java@gmail.com"); // email sending accant
            message.setTo(sendingEmail);  // email receiving account
            message.setSubject("Confirm your account");  // Subject of email message
            message.setText("<a href='http://localhost:8080/api/auth/verifyEmail?code="
                    + code + "&email=" + sendingEmail + "'>Confirm</a>");  // Body or message of email
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public ApiResponse verifyEmail(String email, String code) {
        Optional<User> optionalUser = userRepository.findByEmailAndCode(email, code);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setCode(null);
            userRepository.save(user);
            return new ApiResponse("Your account is verified", true);
        }
        return new ApiResponse("Your account is active", false);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(), loginDto.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            Set<Role> roles = user.getRoles();
            String token = jwtProvider.generateToken(loginDto.getUsername(), roles);
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Login or password wrong", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException("This : " + username + " is not found");
    }
}
