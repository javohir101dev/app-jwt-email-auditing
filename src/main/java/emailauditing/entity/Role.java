package emailauditing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import emailauditing.entity.enums.RoleName;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)    // default EnumType.ORDINAL
    private RoleName roleName;  // enum in default will be written to the database as integer

    @Override
    public String getAuthority() {  // return name of role as String
        return roleName.name();
    }
}
