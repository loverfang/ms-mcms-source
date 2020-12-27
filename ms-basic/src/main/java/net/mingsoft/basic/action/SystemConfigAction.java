package net.mingsoft.basic.action;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.biz.ISystemConfigBiz;
import net.mingsoft.basic.entity.SystemConfigEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "系统配置管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/config/")
public class SystemConfigAction extends BaseAction{

    @Autowired
    private ISystemConfigBiz systemConfigBiz;

    @ApiOperation(value = "查询配置信息列表")
    @GetMapping("/list")
    @RequiresPermissions("manage:config:list")
    public ResultData list(@RequestParam Map<String, Object> params) {
        // 开启分页
        BasicUtil.startPage();
        // 查询数据
        List<SystemConfigEntity> systemConfigEntityList = systemConfigBiz.queryList(params);
        // 封装展示数据
        return ResultData.build().success(new EUListBean(systemConfigEntityList,(int)BasicUtil.endPage(systemConfigEntityList).getTotal()));
    }

    @ApiOperation(value = "查询配置信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("manage:config:info")
    public ResultData info(@PathVariable("id") Integer id) {
        SystemConfigEntity systemConfigEntity = (SystemConfigEntity)systemConfigBiz.getEntity(id);
        return ResultData.build().success(systemConfigEntity);
    }

    @ApiOperation(value = "保存配置")
    @PostMapping("/save")
    @RequiresPermissions("manage:config:save")
    public ResultData save(@RequestBody SystemConfigEntity systemConfigEntity) {
        systemConfigBiz.saveEntity(systemConfigEntity);
        return ResultData.build().success(systemConfigEntity);
    }

    @ApiOperation(value = "修改配置")
    @PostMapping("/update")
    @RequiresPermissions("manage:config:update")
    public ResultData update(@RequestBody SystemConfigEntity systemConfigEntity) {
        //验证方法值得借鉴
        //ValidatorUtils.validateEntity(config);
        systemConfigBiz.updateEntity( systemConfigEntity );
        return ResultData.build().success(systemConfigEntity);
    }

    @ApiOperation(value = "删除配置")
    @PostMapping("/delete")
    @RequiresPermissions("manage:config:delete")
    public ResultData delete(@RequestBody int[] ids) {
        systemConfigBiz.delete(ids);
        return ResultData.build().success();
    }

}
