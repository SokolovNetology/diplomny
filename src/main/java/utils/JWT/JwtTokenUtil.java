package utils.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import model.entities.Role;
import model.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    private final String ROLES = "roles";

    //Метод для генерации токена
    public String generatedToken(User user) {
        //мапа для хранения информации помещаемой в полезную нагрузку токена
        Map<String, Object> claims = new HashMap<>();
        //преобразуем список ролей User в лист String
        List<String> rolesList = user.getRoles().stream().map(Role::getName).toList();
        claims.put(ROLES, rolesList);
        //Фиксируем время выпуска токена
        Date issuedDate = new Date();
        //Фиксируем время жизни токена исходя из jwtLifeTime заданного в проперти
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifeTime.toMillis());
        //Собираем Токен
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserName())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //метод получения имени пользователя из Токена
    public String getUserName(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    //метод получения ролей пользователя из Токена
    public List<String> getUserRoles(String token) {
        return getAllClaimsFromToken(token).get(ROLES, List.class);
    }

    //метод для парсинга токена и получения полезной информации
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}