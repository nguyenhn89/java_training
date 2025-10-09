package org.example.java_training.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ListProductWithCategory", description = "Ad list product with category")
public class ListProductWithCategory implements Serializable {

    private static final long serialVersionUID = -3669879344597561207L;

    @Schema(description = "The id of Product", example = "100005276")
    @JsonProperty("id")
    private Long Id;

    @Schema(description = "The name of Product", example = "product name")
    @JsonProperty("product_name")
    private String product_name;

    @Schema(description = "price of Product", example = "1000")
    @JsonProperty("price")
    private Long price;

    @Schema(description = "The name of Category", example = "category name")
    @JsonProperty("category_name")
    private String category_name;
}
