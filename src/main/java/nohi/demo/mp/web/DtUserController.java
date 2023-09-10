package nohi.demo.mp.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseRequest;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.service.DtUserService;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.dept.DeptQuery;
import nohi.demo.mp.dto.mp.user.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Tag(name = "dtUser", description = "dt用户")
@RestController
@RequestMapping(value = "dtUser")
@Slf4j
public class DtUserController {

    @Autowired
    private DtUserService dtUserService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtUserService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody DtUser info) {
        return dtUserService.listDepts(info);
    }

    @Operation(method = "lists", summary = "显示部门信息，统计部门人数")
    @PostMapping("lists")
    public BaseResponse<List<DtUser>> lists(@RequestBody BaseRequest<UserQuery> info) {
        List<DtUser> page = dtUserService.listDepts(info.getPage(), info.getData());

        BaseResponse<List<DtUser>> response = BaseResponse.suc("操作成功");
        response.setPage(info.getPage());
        response.setData(page);
        return response;
    }

    @PostMapping("refresh")
    public BaseResponse refresh() {
        try {
            return dtUserService.refresh("");
        } catch (Exception e) {
            log.error("刷新部门异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新部门异常:" + e.getMessage());
        }
    }


}
