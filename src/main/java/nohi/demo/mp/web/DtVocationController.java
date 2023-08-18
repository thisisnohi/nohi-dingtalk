package nohi.demo.mp.web;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.service.DtVocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "dtVocation", tags = "dtVocation", description = "假期相关操作")
@RestController
@RequestMapping(value = "dtVocation")
@Slf4j
public class DtVocationController {

    @Autowired
    private DtVocationTypeService service;

    @GetMapping("getAll")
    public Object getAll() {
        return service.findAll();
    }


    @GetMapping("refresh")
    public BaseResponse refresh() {
        try {
            return service.refresh();
        } catch (Exception e) {
            log.error("刷新部门异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新部门异常:" + e.getMessage());
        }
    }


}
