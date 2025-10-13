package org.example.java_training.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Schema(name = "ListUserDTO", description = "Ad list element for users")
public class UserDTO implements Serializable {

    @Schema(description = "The id of User", example = "100005276")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "The name of User", example = "name user")
    @JsonProperty("user_name")
    private String userName;

    @Schema(description = "The name of Role", example = "role name")
    @JsonProperty("role")
    private String role;
}
