package cn.twopair.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartResult {
    private Integer userId;
    private String username;
    private Integer cakeId;
    private Integer num;
    private String cakeImgUrl;
    private String cakeName;
    private Double price;
}
