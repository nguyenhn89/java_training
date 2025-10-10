package org.example.java_training.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Schema(name = "ListElementProductDTO", description = "Ad list element for product")
public class ListElementProductDTO implements Serializable {
    private static final long serialVersionUID = -3669879344597561207L;

    @Schema(description = "The id of Product", example = "100005276")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "The name of Product", example = "product name")
    @JsonProperty("name")
    private String name;

    @Schema(description = "price of Product", example = "1000")
    @JsonProperty("price")
    private BigDecimal price;

}
