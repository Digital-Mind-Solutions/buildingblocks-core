package org.digitalmind.buildingblocks.core.jpaauditor.entity.extension;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Slf4j
public class FileMetadata {
    private String fileName;
    private String contentType;
    private Long fileSize;

    public boolean isEmpty() {
        return fileSize == null || fileSize <= 0;
    }

}
