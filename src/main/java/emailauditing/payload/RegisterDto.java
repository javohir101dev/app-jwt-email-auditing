package emailauditing.payload;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    //    From dependency javax validation
    @Size(min = 3, max = 50)    // Will not affect to the database only for checking in java
    @NonNull
    private String firstName;

    //    From org.hibernate.validator.constraints.Length;
    @Length(min = 3, max = 50)  // Will not affect to the database only for checking in java
    @NonNull
    private String lastName;

    @Email
    @NonNull
    private String email;   // this field as username

    @NonNull
    private String password;

}
