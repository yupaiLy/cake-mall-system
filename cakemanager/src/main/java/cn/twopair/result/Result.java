package cn.twopair.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Code code;
    private T data;
    private Integer data_num;

    /**
     * 用于前端获取数据或向前端响应数据的时候使用
     */
    public static <T> Result<T> ok(Code code, T data, Integer number) {
        return new Result<>(code, data, number);
    }

    /**
     * 用于前端获取数据或向前端响应数据的时候使用
     */
    public static <T> Result<T> warning(Code code, T data, Integer number) {
        return new Result<>(code, data, number);
    }

    /**
     * 不需要返回数据，只返回成功的code
     */
    public static Result success(String msg) {
        return new Result(Code.createCode(Code.SUCCESS, msg), "", 0);
    }

    /**
     * 不需要返回数据，只返回失败的code
     */
    public static Result error(String msg) {
        return new Result(Code.createCode(Code.ERROR, msg), "", 0);
    }

    /**
     * 不需要返回数据，只返回失败的code
     */
    public static Result warning(String msg) {
        return new Result(Code.createCode(Code.WARNING, msg), "", 0);
    }
}
