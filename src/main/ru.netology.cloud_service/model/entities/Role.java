package model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "roles", schema = "cloud_service")
public class Role {
    @Id
    @Column(name = "name")
    private String name;
}
