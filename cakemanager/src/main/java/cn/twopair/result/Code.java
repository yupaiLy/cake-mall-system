package cn.twopair.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Code {
    public static Integer SUCCESS = 20000;
    public static Integer ERROR = 20001;
    public static Integer WARNING = 30000;

    private Integer code;
    private String msg;

    public static Code createCode(Integer code, String msg) {
        return new Code(code, msg);
    }
}
