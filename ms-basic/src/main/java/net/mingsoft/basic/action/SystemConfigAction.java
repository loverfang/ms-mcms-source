package net.mingsoft.basic.action;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

public class SystemConfigAction extends BaseAction{
    /**
     * 所有配置列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    public ResultResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysConfigService.queryPage(params);
        return ResultResponse.ok().put("page", page);
    }

    /**
     * 配置信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    public ResultResponse info(@PathVariable("id") Long id) {
        SysConfigEntity config = sysConfigService.getById(id);
        return ResultResponse.ok().put("config", config);
    }

    /**
     * 保存配置
     */
    @SysLog("保存配置")
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    public ResultResponse save(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);
        sysConfigService.saveConfig(config);
        return ResultResponse.ok();
    }

    /**
     * 修改配置
     */
    @SysLog("修改配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    public ResultResponse update(@RequestBody SysConfigEntity config) {
        ValidatorUtils.validateEntity(config);
        sysConfigService.update(config);
        return ResultResponse.ok();
    }

    /**
     * 删除配置
     */
    @SysLog("删除配置")
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    public ResultResponse delete(@RequestBody Long[] ids) {
        sysConfigService.deleteBatch(ids);
        return ResultResponse.ok();
    }
}
