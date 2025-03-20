package service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import exception.DeleteFileException;
import exception.InputDataException;
import exception.UploadFileException;
import logger.LogStatus;
import logger.Logger;
import logger.SimpleLogger;
import model.entities.StorageFile;
import model.entities.User;
import repository.StorageFileRepository;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class StorageFileService {
    private final Logger logger = SimpleLogger.getInstance();
    private final StorageFileRepository storageFileRepository;


    // Метод для загрузки файла в хранилище
    @Transactional
    public void fileUpload(User user, String fileName, MultipartFile fileContent) {
        //Запись в БД
        try {
            //Проверим, что у нашего User уже не записан такой файл
            if (storageFileRepository.findByUserAndFileName(user, fileName) != null) {
                String errorMsgUpload = String.format("Save Error: File |'%s'| from User {'%s'} " +
                        "is already in the database", fileName, user.getUserName());
                logger.log(LogStatus.ERROR, errorMsgUpload);
                throw new InputDataException(errorMsgUpload);
            }
            // Пробуем записать файл в БД
            storageFileRepository.save(StorageFile.builder()
                    .fileName(fileName)
                    .dateAndTime(LocalDateTime.now())
                    .fileSize(fileContent.getSize())
                    .fileContent(fileContent.getBytes())
                    .user((org.apache.catalina.User) user)
                    .build());
            logger.log(
                    LogStatus.INFO,
                    String.format("File |'%s'| from User {'%s'} success upload", fileName, user.getUserName())
            );
        } catch (IOException e) {
            String errorMsgUpload = "Save Error: Invalid input data. File not uploaded";
            logger.log(LogStatus.ERROR, errorMsgUpload);
            throw new InputDataException(errorMsgUpload);
        }
    }

    // Метод для удаления файла в хранилище
    @Transactional
    public void fileDelete(User user, String fileName) {
        //Проверяем наличия файла в БД
        String errorMsgNotFound = String.format("Delete Error: File |'%s'| from User {'%s'} not found in DataBase;",
                fileName, user.getUserName());
        checkFile(storageFileRepository.findByUserAndFileName(user, fileName), errorMsgNotFound);
        // Пробуем удалить и проверяем, что файл удален
        storageFileRepository.deleteByUserAndFileName(user, fileName);
        if (storageFileRepository.findByUserAndFileName(user, fileName) != null) {
            String msg = String.format("Delete Error: File |'%s'| from User {'%s'} wasn't delete;",
                    fileName, user.getUserName());
            logger.log(LogStatus.ERROR, msg);
            throw new DeleteFileException(msg);
        }
        logger.log(
                LogStatus.INFO,
                String.format("File |'%s'| from User {'%s'} success Delete;", fileName, user.getUserName())
        );
    }

    // Метод для скачивания файла
    public StorageFile fileDownload(User user, String fileName) {
        //Проверяем наличие файла в БД
        StorageFile file = storageFileRepository.findByUserAndFileName(user, fileName);
        String errorMsgDownload = String.format("Download Error: File |'%s'| from User {'%s'} " +
                "not found in DataBase;", fileName, user.getUserName());
        checkFile(file, errorMsgDownload);
        logger.log(
                LogStatus.INFO,
                String.format("User {'%s'} download File |'%s'|;", fileName, user.getUserName())
        );
        return file;
    }

    // Метод для изменения имени файла
    @Transactional
    public void fileNameEdit(User user, String fileName, String newFileName) {
        // Проверим есть ли fileName для данного User в ханилище и что нет файла с новым именем
        String errorMsgFileNameEdit = String.format("FileNameEdit Error: File |'%s'| from User {'%s'} " +
                "not found in DataBase;", fileName, user.getUserName());
        checkFile(storageFileRepository.findByUserAndFileName(user, fileName), errorMsgFileNameEdit);
        if (storageFileRepository.findByUserAndFileName(user, newFileName) != null) {
            throw new InputDataException(String.format("FileNameEdit Error: New FileName |'%s'| " +
                            "from User {'%s'} is already in the database;",
                    newFileName, user.getUserName()));
        }
        // Изменим Имя
        storageFileRepository.editFileNameByUser(user, fileName, newFileName);
        // Проверим, что файл изменил имя в БД
        if (
                storageFileRepository.findByUserAndFileName(user, fileName) != null
                        || storageFileRepository.findByUserAndFileName(user, newFileName) == null
        ) {
            String msg = String.format("FileNameEdit Error: File |'%s'| from User {'%s'}" +
                    " wasn't edit;", fileName, user.getUserName());
            logger.log(LogStatus.ERROR, msg);
            throw new UploadFileException(msg);
        }
        logger.log(
                LogStatus.INFO,
                String.format("User {'%s'} edit fileName of File |'%s'| on new fileName |'%s'|",
                        user.getUserName(), fileName, newFileName)
        );
    }

    // Метод получения всего списка хранящихся данных
    public List<StorageFile> getAllFiles(User user) {;
        logger.log(
                LogStatus.INFO,
                String.format("User {'%s'} get list all files", user.getUserName())
        );
        return storageFileRepository.findAllByUser(user);
    }

    private void checkFile(StorageFile file, String errorMsg) {
        if (file == null) {
            logger.log(LogStatus.ERROR, errorMsg);
            throw new InputDataException(errorMsg);
        }
    }


}