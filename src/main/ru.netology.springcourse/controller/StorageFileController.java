package controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_service.exception.BadCredentialsException;
import ru.netology.cloud_service.logger.LogStatus;
import ru.netology.cloud_service.logger.Logger;
import ru.netology.cloud_service.logger.SimpleLogger;
import ru.netology.cloud_service.model.dtos.request.FileNameEditRequest;
import ru.netology.cloud_service.model.dtos.response.ResponseForGetAllFiles;
import ru.netology.cloud_service.model.entities.StorageFile;
import ru.netology.cloud_service.model.entities.User;
import ru.netology.cloud_service.service.AuthenticationService;
import ru.netology.cloud_service.service.StorageFileService;
import ru.netology.cloud_service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class StorageFileController {
    private StorageFileService storageFileService;
    private AuthenticationService authenticationService;
    private UserService userService;
    private final Logger logger = SimpleLogger.getInstance();

    @PostMapping("/file")
    public ResponseEntity<?> fileUpload(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename, MultipartFile file
    ) {
        //получаем User из репозитория залогиненых пользователей чтобы не пропустить разлогиненые живые токены
        User user = getUserByToken(authToken);
        storageFileService.fileUpload(user, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> fileDelete(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename
    ) {
        //получаем User из репозитория залогиненых пользователей чтобы не пропустить разлогиненые живые токены
        User user = getUserByToken(authToken);
        storageFileService.fileDelete(user, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> fileDownload(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename
    ) {
        //получаем User из репозитория залогиненых пользователей чтобы не пропустить разлогиненые живые токены
        User user = getUserByToken(authToken);
        StorageFile file = storageFileService.fileDownload(user, filename);
        return ResponseEntity.ok()
                .header("Content-Type", "multipart/form-data")
                .body(new ByteArrayResource(file.getFileContent()));
    }

    @PutMapping("/file")
    public ResponseEntity<?> fileNameEdit(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody FileNameEditRequest fileNameEditRequest
    ) {
        System.out.println(filename);
        //получаем User из репозитория залогиненых пользователей чтобы не пропустить разлогиненые живые токены
        User user = getUserByToken(authToken);
        storageFileService.fileNameEdit(user, filename, fileNameEditRequest.getNewFileName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<ResponseForGetAllFiles> getAllFiles(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("limit") Integer limit) {
        //получаем User из репозитория залогиненых пользователей чтобы не пропустить разлогиненые живые токены
        User user = getUserByToken(authToken);
        return storageFileService.getAllFiles(user).stream()
                .map(o -> new ResponseForGetAllFiles(o.getFileName(), o.getFileSize()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    //Метод получения User из БД залогиненных User
    private User getUserByToken(String authToken) {
        String errorMsgLogin = "Error Auth: Invalid token - user isn't logged.";
        if (authToken.startsWith("Bearer ")) {
            final String jwtToken = authToken.substring(7);
            final String username = authenticationService.getUsernameByToken(jwtToken);
            return userService.findUserByUserName(username).orElseThrow(() -> {
                logger.log(LogStatus.ERROR, errorMsgLogin);
                return new BadCredentialsException(errorMsgLogin);
            });
        }
        logger.log(LogStatus.ERROR, errorMsgLogin);
        throw new BadCredentialsException(errorMsgLogin);
    }
}
