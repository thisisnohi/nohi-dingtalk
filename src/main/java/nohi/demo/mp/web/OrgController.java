package nohi.demo.mp.web;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.COrg;
import nohi.demo.mp.dt.service.COrgService;
import nohi.demo.mp.dto.org.QueryWdReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "org", tags = "org", description = "机构管理")
@RestController
@RequestMapping(value = "org")
@Slf4j
public class OrgController {

    @Autowired
    private COrgService cOrgService;

    @GetMapping("getAll")
    public Object getAll() {
        return cOrgService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody COrg info) {
        return cOrgService.list(info);
    }

    /**
     * 获取网点
     * @return
     */
    @PostMapping("listWd")
    public BaseResponse listWd(@RequestBody QueryWdReq req) {
        BaseResponse response = new BaseResponse<List<COrg>>();
        response.setResCode(BaseResponse.ResCode.SUC);
        try {
            COrg info = new COrg();
            info.setRegion(req.getRegion());
            info.setOrgLvl("4");
            List<COrg> list = cOrgService.list(info);
            response.setData(list);
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
        return response;
    }
}
