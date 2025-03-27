package repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Хранение данных о пользователях, которые логинились в систему и получили токен
// (реализована примитивная версия - информация живет пока работает сервер и не удаляется
// по истечении срока жизни токена)
@Repository
@Transactional
public class AuthenticationRepository {
    private final Map<String, String> authMap = new ConcurrentHashMap<>();

    public void putAuth (String token, String username) {
        authMap.put(token, username);
    }

    public void removeAuth(String token) {
        authMap.remove(token);
    }

    public String getUsername(String token) {
        return authMap.get(token);
    }
}
