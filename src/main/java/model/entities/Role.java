package model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "roles", schema = "cloud_service")
public class Role {
    @Id
    @Column(name = "name")
    private String name;
}
