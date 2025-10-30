package org.example.java_training.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ListElementProductDTO", description = "Ad list element for product")
public class ListElementProductDTO implements Serializable {
    private static final long serialVersionUID = -3669879344597561207L;

    @Schema(description = "The id of Product", example = "100005276")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "The name of Product", example = "product name")
    @JsonProperty("name")
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @Schema(description = "price of Product", example = "1000")
    @JsonProperty("price")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(description = "content of Product", example = "product content")
    @JsonProperty("content")
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;

    @Schema(description = "Category ID of Product", example = "1")
    @JsonProperty("category_id")
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
