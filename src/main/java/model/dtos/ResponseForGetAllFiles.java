package model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseForGetAllFiles {
    private String fileName;
    private Long fileSizeInBytes;
}