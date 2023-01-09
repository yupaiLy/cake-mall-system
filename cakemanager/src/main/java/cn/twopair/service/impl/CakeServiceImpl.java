package cn.twopair.service.impl;

import cn.twopair.mapper.CakeMapper;
import cn.twopair.mapper.CategoryMapper;
import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Category;
import cn.twopair.result.CakeChartPictureOfRosesResult;
import cn.twopair.result.CatChartPictureOfRosesResult;
import cn.twopair.result.Code;
import cn.twopair.result.Result;
import cn.twopair.service.CakeService;
import cn.twopair.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CakeServiceImpl implements CakeService {
    private final CakeMapper cakeMapper;
    private final CategoryMapper categoryMapper;

    public CakeServiceImpl(CakeMapper cakeMapper, CategoryMapper categoryMapper) {
        this.cakeMapper = cakeMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public String add(Cake cake) {
        if (cakeMapper.selectByName(cake.getName()) == null) {
            int row = cakeMapper.insert(cake);
            if (row > 0) {
                return JsonTool.createJsonData(Result.success("添加成功!"));
            }
            return JsonTool.createJsonData(Result.error("添加失败"));
        }
        return JsonTool.createJsonData(Result.error("蛋糕名称已存在，添加失败"));
    }

    @Override
    public String deleteByIds(List<Integer> ids) {
        int cnt = cakeMapper.deleteByIds(ids);
        if (cnt == ids.size()) {
            return JsonTool.createJsonData(Result.success("删除成功!"));
        }
        //回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return JsonTool.createJsonData(Result.error("删除失败！请稍后再试"));
    }

    @Override
    public String updateCake(Cake cake) {
        int cnt = cakeMapper.update(cake);
        if (cnt > 0) {
            return JsonTool.createJsonData(Result.success("修改成功!"));
        }

        //回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return JsonTool.createJsonData(Result.error("修改失败！请稍后再试"));
    }

    @Override
    public String conditionQuery(Cake cake, int currentPage, int pageSize) {
        List<Cake> cakes = cakeMapper.conditionQuery(cake, (currentPage - 1) * pageSize, pageSize);
        // 数据库总条数
        int tot = cakeMapper.conditionQueryData(cake).size();

        // 防止点击最后一夜再条件查询出现空情况
        if (tot < (currentPage - 1) * pageSize) {
            cakes = cakeMapper.conditionQuery(cake, 0, pageSize);
            // 状态转换
            converseStatus(cakes);
            return JsonTool.createJsonData(Result.warning(Code.createCode(Code.WARNING, "查询成功!"), cakes, tot));
        }
        converseStatus(cakes);
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), cakes, tot));
    }

    @Override
    public String getAllCakeNum() {
        try {
            List<Cake> cakes = cakeMapper.selectAll();
            List<CakeChartPictureOfRosesResult> ret = new ArrayList<>();
            for (Cake cake : cakes) {
                ret.add(new CakeChartPictureOfRosesResult(cake.getNumber(), cake.getName()));
            }
            return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), ret, ret.size()));
        } catch (Exception e) {
            return JsonTool.createJsonData(Result.error("查询失败！请稍后再试"));
        }
    }

    @Override
    public String getHotCake() {
        try {
            List<Cake> cakes = cakeMapper.selectOrderByNum(0, 5);
            for (Cake cake : cakes) {
                String type = categoryMapper.selectById(Integer.valueOf(cake.getCid())).getName();
                cake.setCid(type);
            }
            return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询热销成功!"), cakes, cakes.size()));

        } catch (Exception e) {
            return JsonTool.createJsonData(Result.error("查询热销失败！请稍后再试"));
        }
    }

    private void converseStatus(List<Cake> cakes) {
        for (Cake cake : cakes) {
            switch (cake.getStatus()) {
                case "1" -> cake.setStatus("上架");
                case "0" -> cake.setStatus("下架");
            }
            String name = categoryMapper.selectById(Integer.valueOf(cake.getCid())).getName();
            cake.setCid(name);
        }
    }
}
