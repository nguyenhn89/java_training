package org.example.java_training.dto;

public class CategoryCountDTO {
    private Long categoryId;
    private Long count;

    public CategoryCountDTO(Long categoryId, Long count) {
        this.categoryId = categoryId;
        this.count = count;
    }

    public Long getCategoryId() { return categoryId; }
    public Long getCount() { return count; }
}
