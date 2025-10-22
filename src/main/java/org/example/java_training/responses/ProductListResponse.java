package org.example.java_training.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.example.java_training.domain.AbstractAuditingEntity;
import org.example.java_training.dto.ListElementProductDTO;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Value
@Schema(name = "ProductListResponse", description = "Paginated list of products")
public class ProductListResponse implements Serializable {

    private static final long serialVersionUID = -2830370621037495869L;

    @Schema(description = "List of products", name = "productsList")
    @JsonProperty("productsList")
    List<ListElementProductDTO> productsList;

    @Schema(description = "Total number of items")
    long totalItems;

    @Schema(description = "Total number of pages")
    int totalPages;

    @Schema(description = "Current page number")
    int currentPage;

    @Schema(description = "Page size")
    int pageSize;
}
