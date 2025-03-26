package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloud_service.model.entities.StorageFile;
import ru.netology.cloud_service.model.entities.User;

import java.util.List;

@Repository
public interface StorageFileRepository extends JpaRepository<StorageFile, String> {
    void deleteByUserAndFileName(User user, String fileName);
    StorageFile findByUserAndFileName(User user, String fileName);

    List<StorageFile> findAllByUser(User user);

    @Modifying
    @Query("UPDATE StorageFile f SET f.fileName = :newFileName " +
            "WHERE f.fileName = :fileName AND f.user = :user")
    void editFileNameByUser(
            @Param("user") User user,
            @Param("fileName") String fileName,
            @Param("newFileName") String newFileName
    );
}
