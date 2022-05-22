package emailauditing.security;

import emailauditing.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private static final long expiryTime = 1000 * 60 * 60 * 24; // 24 hour
    private static final String secretWord = "OurSecretWord";

    public String generateToken(String username, Set<Role> roleSet) {
        String generatedToken = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .claim("roles", roleSet)
                .signWith(SignatureAlgorithm.HS512, secretWord)
                .compact();
        return generatedToken;
    }

    public String getEmailFromToken(String token) {
        try {
            String email = Jwts
                    .parser()
                    .setSigningKey(secretWord)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


}
