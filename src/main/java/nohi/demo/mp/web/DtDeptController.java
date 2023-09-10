package nohi.demo.mp.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseRequest;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.mp.dt.service.DtDeptService;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.dept.DeptQuery;
import nohi.demo.mp.dto.mp.dept.DeptTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Tag(name = "dtDept", description = "dt部门服务")
@RestController
@RequestMapping(value = "dtDetp")
@Slf4j
public class DtDeptController {

    @Autowired
    private DtDeptService dtDeptService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtDeptService.findAll();
    }

    @Operation(method = "lists", summary = "显示部门信息，统计部门人数")
    @PostMapping("lists")
    public BaseResponse<List<DeptInfo>> lists(@RequestBody BaseRequest<DeptQuery> info) {
        List<DeptInfo> data = dtDeptService.dtDeptList(info.getPage(), info.getData());

        BaseResponse<List<DeptInfo>> response = BaseResponse.suc("操作成功");
        response.setPage(info.getPage());
        response.setData(data);
        return response;
    }

    @Operation(method = "tree", summary = "显示部门树")
    @PostMapping("tree")
    public BaseResponse<List<DeptTree>> tree(@RequestBody BaseRequest<DeptQuery> info) {
        List<DeptTree> list = dtDeptService.deptTree(info.getData());

        BaseResponse<List<DeptTree>> response = BaseResponse.suc("操作成功");
        response.setPage(info.getPage());
        response.setData(list);
        return response;
    }

    @PostMapping("list")
    public List list(@RequestBody DtDept info) {
        return dtDeptService.listDepts(info);
    }

    @Operation(method = "refresh", summary = "刷新部门")
    @PostMapping("refresh")
    public BaseResponse refresh() {
        try {
            dtDeptService.refreshDepts("");
        } catch (Exception e) {
            log.error("刷新部门异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新部门异常:" + e.getMessage());
        }
        return BaseResponse.suc("操作成功");
    }

}
