package com.example.b1esimageweb.web.Security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.b1esimageweb.model.User;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;
    
    // Define a field to store the secret key
    private final String secretKey;

    // Initialize the secret key during construction
    public JwtTokenProvider() {
        secretKey = SecretKeyGenerator.generateSecretKey(256); // Adjust key length as needed
    }

    public String createToken(User userDetails) {
        try {
            JWSSigner signer = new MACSigner(secretKey);

            Date now = new Date();
            Date validity = new Date(now.getTime() + validityInMilliseconds);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUserName())
                    .issueTime(now)
                    .expirationTime(validity)
                    .claim("customKey", "customValue")
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            // Handle exception
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(secretKey);
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claims.getSubject();
        } catch (Exception e) {
            // Handle exception
        }
        return null;
    }
}
