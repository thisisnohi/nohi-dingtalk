package nohi.demo.mp.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtAprProcInst;
import nohi.demo.mp.dt.service.DtAprProcInstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "dtAprove", tags = "dtAprove", description = "审批")
@RestController
@RequestMapping(value = "dtAprove")
@Slf4j
public class DtAproveController {

    @Autowired
    private DtAprProcInstService dtAprProcInstService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtAprProcInstService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody DtAprProcInst info) {
        return dtAprProcInstService.list(info);
    }

    @ApiOperation(value = "同步流程实例数据", notes = "")
    @GetMapping("syncProcInfo")
    public BaseResponse syncProcInfo(String procInstId) {
        return dtAprProcInstService.syncProcInfo(procInstId);
    }


    @ApiOperation(value = "同步流程实例数据", notes = "")
    @GetMapping("syncProcInfos")
    public BaseResponse syncProcInfos(String procInstId) {
        return dtAprProcInstService.syncProcInfo(procInstId);
    }
}
