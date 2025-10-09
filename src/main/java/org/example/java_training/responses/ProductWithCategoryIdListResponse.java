package org.example.java_training.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;
import org.example.java_training.dto.ListProductWithCategory;

import java.io.Serializable;
import java.util.List;

@Value
@Schema(name = "ProductWithCategoryIdListResponse", description = "Product with Category")
public class ProductWithCategoryIdListResponse implements Serializable {
    private static final long serialVersionUID = -2830370621037495869L;

    @Schema(description = "Product ad list response", name = "productsList")
    @JsonProperty("productsList")
    List<ListProductWithCategory> productsListCategory;
}
