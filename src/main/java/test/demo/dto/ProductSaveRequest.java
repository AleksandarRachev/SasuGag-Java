package test.demo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveRequest {

//    @NotBlank(message = "Name must not be empty")
    private String name;
//
//    @NotNull(message = "Price must not be empty")
//    @Min(value = 0, message = "Price must be positive")
//    private Integer price;

    private String uid;
    private byte[] image;

}
