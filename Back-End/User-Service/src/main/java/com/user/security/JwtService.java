package com.user.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static String theKEY;

    static {
     String generateKey = " ";
     try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
      SecretKey key = keyGenerator.generateKey();
         generateKey = Base64.getEncoder().encodeToString(key.getEncoded());
     } catch (Exception e) {throw new RuntimeException(e); }

     theKEY = generateKey;
    }


    public String getJwtToken(String username) {
        Map<String, Object> clms = new  HashMap<>();
        return Jwts.builder()
                .claims()
                .add(clms)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 25*60*1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
      byte [] sKey = Decoders.BASE64.decode(theKEY);
      return Keys.hmacShaKeyFor(sKey);
    }


    //JWt Stuffs

    public String getUsernameByToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsernameByToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
