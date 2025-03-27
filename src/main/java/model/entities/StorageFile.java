package model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "FILES", schema = "cloud_service")
public class StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime dateAndTime;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private byte[] fileContent;

    @ManyToOne
    private User user;
}
