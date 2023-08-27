package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.service.DtUserService;
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

    @GetMapping("refresh")
    public BaseResponse refresh() {
        try {
            return dtUserService.refresh("");
        } catch (Exception e) {
            log.error("刷新部门异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新部门异常:" + e.getMessage());
        }
    }


}
