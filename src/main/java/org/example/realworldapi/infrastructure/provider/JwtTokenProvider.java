package org.example.realworldapi.infrastructure.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.example.realworldapi.infrastructure.web.provider.TokenProvider;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

@ApplicationScoped
public class JwtTokenProvider implements TokenProvider {

  public final String COMPLEMENTARY_SUBSCRIPTION = "complementary-subscription";
  public final String CLAIM_ROLES = "ROLES";
  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;
  private String issuer;
  private Integer expirationTimeInMinutes;

  public JwtTokenProvider(
      @ConfigProperty(name = "jwt.issuer") String issuer,
      @ConfigProperty(name = "jwt.secret") String secret,
      @ConfigProperty(name = "jwt.expiration.time.minutes") Integer expirationTimeInMinutes) {

    this.issuer = issuer;
    this.algorithm = Algorithm.HMAC512(secret);
    this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    this.expirationTimeInMinutes = expirationTimeInMinutes;
  }

  @Override
  public String createUserToken(String subject) {
    JWTCreator.Builder builder;

    builder =
        JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withIssuedAt(new Date())
            .withClaim(COMPLEMENTARY_SUBSCRIPTION, UUID.randomUUID().toString());

    builder.withArrayClaim(CLAIM_ROLES, toArrayNames(Role.USER));

    if (expirationTimeInMinutes != null) {
      builder.withExpiresAt(plusMinutes(expirationTimeInMinutes));
    }

    return builder.sign(algorithm);
  }

  @Override
  public DecodedJWT verify(String token) {
    return jwtVerifier.verify(token);
  }

  @Override
  public Role[] extractRoles(DecodedJWT decodedJWT) {
    Claim claim = decodedJWT.getClaim(CLAIM_ROLES);
    return claim.asArray(Role.class);
  }

  private static String[] toArrayNames(Role... allowedRoles) {

    String[] names = new String[allowedRoles.length];

    for (int nameIndex = 0; nameIndex < allowedRoles.length; nameIndex++) {
      names[nameIndex] = allowedRoles[nameIndex].name();
    }

    return names;
  }

  private static Date plusMinutes(int minutes) {
    int oneMinuteInMillis = 60000;
    Calendar calendar = Calendar.getInstance();
    return new Date(calendar.getTimeInMillis() + (minutes * oneMinuteInMillis));
  }
}
