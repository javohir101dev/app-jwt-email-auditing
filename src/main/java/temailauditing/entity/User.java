package temailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

//    From dependency javax validation
    @Size(min = 3, max = 50)    // Will not affect to the database only for checking in java
    @Column(nullable = false, length = 50)// length for database max limit
    private String firstName;

//    From org.hibernate.validator.constraints.Length;
    @Length(min = 3, max = 50)  // Will not affect to the database only for checking in java
    @Column(nullable = false, length = 50)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;   // this field as username

    @Column(nullable = false)
    private String password;

    @CreationTimestamp  // automatically saves created time to database
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp    // automatically saves when row is updated
    private Timestamp updatedAt;


//    Methods of UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
