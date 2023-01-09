package cn.twopair.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderViewResult {

    private String code;
    private String name;
    private String address;
    private String phone;
    private Double total;
    private String status;
    private String orderTime;
}
