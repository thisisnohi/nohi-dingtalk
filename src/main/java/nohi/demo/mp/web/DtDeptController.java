package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.mp.dt.service.DtDeptService;
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

    @PostMapping("list")
    public List list(@RequestBody DtDept info) {
        return dtDeptService.listDepts(info);
    }

    @Operation(method = "refresh", summary = "刷新部门")
    @GetMapping("refresh")
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
