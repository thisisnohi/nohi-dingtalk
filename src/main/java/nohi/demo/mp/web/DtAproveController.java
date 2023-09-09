package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseRequest;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtAprProcInst;
import nohi.demo.mp.dt.service.DtAprProcInstService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Tag(name = "审批", description = "审批")
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

    @Operation(method = "syncProcInfo", summary = "同步流程实例数据")
    @GetMapping("syncProcInfo")
    public BaseResponse syncProcInfo(String procInstId) {
        return dtAprProcInstService.syncProcInfo(procInstId);
    }


    @Operation(method = "syncProcInfo", summary = "同步流程实例数据")
    @PostMapping("syncProcInfos")
    public BaseResponse syncProcInfos(@RequestBody BaseRequest<KqQueryReq> info) {
        // 同步
        dtAprProcInstService .syncProcInstByData(info.getData());
        return BaseResponse.suc("同步成功");
    }
}
