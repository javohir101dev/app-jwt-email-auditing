package emailauditing.payload;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;

@Data
public class LoginDto {

    @Email
    @NonNull
    private String username;

    @NonNull
    private String password;

}
