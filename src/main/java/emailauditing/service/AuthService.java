package emailauditing.service;

import com.sun.mail.smtp.SMTPMessage;
import emailauditing.entity.enums.RoleName;
import emailauditing.repository.RoleRepository;
import emailauditing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import emailauditing.entity.User;
import emailauditing.payload.ApiResponse;
import emailauditing.payload.RegisterDto;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse registerUser(RegisterDto registerDto){

        boolean exists = userRepository.existsByEmail(registerDto.getEmail());
        if (exists){
            return new ApiResponse(
                    "This username: " + registerDto.getEmail() + " is already taken"
                    , false);
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRoleName(RoleName.ROLE_USER)));

        user.setCode(UUID.randomUUID().toString());

        userRepository.save(user);
//      Saving user is finished

//      Sending code to email for verifying account
        Boolean isEmailSent = sendEmail(user.getEmail(), user.getCode());
        return new ApiResponse(
                "You have successfully registered. Now confirm your email account",
                isEmailSent);
    }

    public Boolean sendEmail(String sendingEmail, String code){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mailsender1java"); // email sending accant
            message.setTo(sendingEmail);  // email receiving account
            message.setSubject("Confirm your account");  // Subject of email message
            message.setText("<a href='http://localhost:8080/api/auth/veriftEmail?code="
                    + code + "&email=" + sendingEmail + "'>Confirm</a>");  // Body or message of email
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



}
