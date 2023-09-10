package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtKqGroup;
import nohi.demo.mp.dt.service.DtKqGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Tag(name = "dtGroup", description = "dt考勤组")
@RestController
@RequestMapping(value = "dtGroup")
@Slf4j
public class DtKqGroupController {

    @Autowired
    private DtKqGroupService dtKqGroupService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtKqGroupService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody DtKqGroup info) {
        return dtKqGroupService.listDepts(info);
    }

    @Operation(method = "refresh", summary = "刷新考勤组")
    @PostMapping("refresh")
    public BaseResponse refresh() {
        try {
            return dtKqGroupService.refresh();
        } catch (Exception e) {
            log.error("刷新考勤组异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新考勤组异常:" + e.getMessage());
        }
    }


}
