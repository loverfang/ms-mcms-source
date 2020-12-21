/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.basic.action;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.biz.IRoleModelBiz;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.constant.e.ModelIsMenuEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.entity.ModelEntity;
import net.mingsoft.basic.entity.RoleModelEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模块控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2014-6-29<br/>
 * 历史修订：<br/>
 */
@Api(value = "菜单管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/model")
public class ModelAction extends BaseAction {

    /**
     * 注入模块业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    @Autowired
    private IManagerBiz managerBiz;
    /**
     * 角色模块关联业务层
     */
    @Autowired
    private IRoleModelBiz roleModelBiz;

    /**
     * 返回主界面index
     */
    @ApiOperation(value = "返回主界面index")
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request,ModelMap mode){
        ManagerSessionEntity managerSession = this.getManagerBySession();
        int currentRoleId = managerSession.getManagerRoleID();
        List<BaseEntity> parentModelList = modelBiz.queryModelByRoleId(currentRoleId);
        mode.addAttribute("parentModelList", JSONArray.toJSONString(parentModelList));
        return "/basic/model/index";
    }


    /**
     * 查询模块表列表
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId 模块自增长id<br/>
     * modelTitle 模块标题<br/>
     * modelCode 模块编码<br/>
     * modelModelid 模块的父模块id<br/>
     * modelUrl 模块连接地址<br/>
     * modelDatetime <br/>
     * modelIcon 模块图标<br/>
     * modelModelmanagerid 模块关联的关联员id<br/>
     * modelSort 模块的排序<br/>
     * modelIsmenu 模块是否是菜单<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>[<br/>
     * { <br/>
     * modelId: 模块自增长id<br/>
     * modelTitle: 模块标题<br/>
     * modelCode: 模块编码<br/>
     * modelModelid: 模块的父模块id<br/>
     * modelUrl: 模块连接地址<br/>
     * modelDatetime: <br/>
     * modelIcon: 模块图标<br/>
     * modelModelmanagerid: 模块关联的关联员id<br/>
     * modelSort: 模块的排序<br/>
     * modelIsmenu: 模块是否是菜单<br/>
     * }<br/>
     * ]</dd><br/>
     */
    @ApiOperation(value="菜单列表接口")
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
        ManagerSessionEntity managerSession = this.getManagerBySession();
        int currentRoleId = managerSession.getManagerRoleID();
        List<BaseEntity> modelList = modelBiz.queryModelByRoleId(currentRoleId);
        EUListBean _list = new EUListBean(modelList, modelList.size());
        return ResultData.build().success(_list);
    }

    @ApiOperation(value="菜单导入接口")
    @ApiImplicitParam(name = "menuStr", value = "菜单json", required = true,paramType="query")
    @PostMapping("/import")
    @ResponseBody
    public ResultData importMenu(String menuStr) {
        if(StringUtils.isBlank(menuStr)){
            return ResultData.build().error(getResString("err.empty", this.getResString("menu")));
        }
        try{
            modelBiz.jsonToModel(menuStr,getManagerBySession().getManagerRoleID());
        }catch (RuntimeException e){
            return ResultData.build().error(getResString("model.title.or.json"));
        }catch (Exception e){
            return ResultData.build().error(getResString("err.error", this.getResString("menu")));
        }
        return ResultData.build().success();
    }

    /**
     * 获取模块表
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId 模块自增长id<br/>
     * modelTitle 模块标题<br/>
     * modelCode 模块编码<br/>
     * modelModelid 模块的父模块id<br/>
     * modelUrl 模块连接地址<br/>
     * modelDatetime <br/>
     * modelIcon 模块图标<br/>
     * modelModelmanagerid 模块关联的关联员id<br/>
     * modelSort 模块的排序<br/>
     * modelIsmenu 模块是否是菜单<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * modelId: 模块自增长id<br/>
     * modelTitle: 模块标题<br/>
     * modelCode: 模块编码<br/>
     * modelModelid: 模块的父模块id<br/>
     * modelUrl: 模块连接地址<br/>
     * modelDatetime: <br/>
     * modelIcon: 模块图标<br/>
     * modelModelmanagerid: 模块关联的关联员id<br/>
     * modelSort: 模块的排序<br/>
     * modelIsmenu: 模块是否是菜单<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "获取模块表")
    @ApiImplicitParam(name = "modelId", value = "模块的编号", required = true,paramType="query")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(modelEntity.getModelId()<=0) {
            return ResultData.build().error(getResString("err.error", this.getResString("model.id")));
        }
        //根据父模块id查寻模块
        ModelEntity _model = (ModelEntity)modelBiz.getEntity(modelEntity.getModelId());
        if(_model != null){
            Map<String, ModelEntity> mode = new HashMap<String, ModelEntity>();
            if(_model.getModelModelId() != null){
                ModelEntity parentModel = (ModelEntity) modelBiz.getEntity(_model.getModelModelId());
                mode.put("parentModel", parentModel);
            }
            mode.put("model", _model);
            return ResultData.build().success(mode);
        }
        return ResultData.build().success(_model);
    }

    /**
     * 保存模块表实体
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId 模块自增长id<br/>
     * modelTitle 模块标题<br/>
     * modelCode 模块编码<br/>
     * modelModelid 模块的父模块id<br/>
     * modelUrl 模块连接地址<br/>
     * modelDatetime <br/>
     * modelIcon 模块图标<br/>
     * modelModelmanagerid 模块关联的关联员id<br/>
     * modelSort 模块的排序<br/>
     * modelIsmenu 模块是否是菜单<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * modelId: 模块自增长id<br/>
     * modelTitle: 模块标题<br/>
     * modelCode: 模块编码<br/>
     * modelModelid: 模块的父模块id<br/>
     * modelUrl: 模块连接地址<br/>
     * modelDatetime: <br/>
     * modelIcon: 模块图标<br/>
     * modelModelmanagerid: 模块关联的关联员id<br/>
     * modelSort: 模块的排序<br/>
     * modelIsmenu: 模块是否是菜单<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "保存模块表实体")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelTitle", value = "模块的标题", required = true,paramType="query"),
            @ApiImplicitParam(name = "modelCode", value = "模块编码", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelModelId", value = "模块父id", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelUrl", value = "链接地址", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelDatetime", value = "发布时间", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelIcon", value = "模块图标", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelManagerId", value = "模块管理员Id", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelSort", value = "模块排序", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelIsMenu", value = "是否是菜单", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "菜单类型", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelParentIds", value = "父级编号集合", required = false,paramType="query"),
    })
    @LogAnn(title = "保存模块表实体",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("model:save")
    public ResultData save(@ModelAttribute @ApiIgnore ModelEntity model, HttpServletResponse response, HttpServletRequest request) {
        //模块标题验证
        if(StringUtil.isBlank(model.getModelTitle())){
            return ResultData.build().error(getResString("err.empty", this.getResString("model.title")));
        }
        if(!StringUtil.checkLength(model.getModelTitle()+"", 1, 10)){
            return ResultData.build().error(getResString("err.length", this.getResString("model.title"), "1", "10"));
        }
        //判断菜单名称不能相同
        if(model.getModelIsMenu() == ModelIsMenuEnum.MODEL_MEUN.toInt()){
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setModelTitle(model.getModelTitle());
            modelEntity.setModelIsMenu(ModelIsMenuEnum.MODEL_MEUN.toInt());
            if(ObjectUtil.isNotEmpty(modelBiz.getEntity(modelEntity))){
                return ResultData.build().error(getResString("err.exist",this.getResString("model.title")));
            }
        }
        //获取管理员id并赋值给模块的id
        model.setModelId(getManagerId());
        // 获取模块保存时间
        model.setModelDatetime(new Timestamp(System.currentTimeMillis()));
        //判断图标是否为空，不为空去掉,图标地址中含有的“|”
        //空值判断
        if(!StringUtil.isBlank(model.getModelIcon())) {
            model.setModelIcon( model.getModelIcon().replace("|", ""));
        }
        //重复判断，modelCode不能重复
        if(StringUtils.isNotBlank(model.getModelCode())){
            ModelEntity _model = modelBiz.getEntityByModelCode(model.getModelCode());
            if (!StringUtil.isBlank(_model)){
                return ResultData.build().error(getResString("err.exist",this.getResString("modelCode")));
            }
        }
        modelBiz.saveEntity(model);
        //保存成功后给当前管理就就加上对应的权限
        if(model.getModelId() > 0){
            ManagerSessionEntity managerSession = this.getManagerBySession();
            List<RoleModelEntity> roleModels = new ArrayList<>();
            RoleModelEntity rolemodel = new RoleModelEntity();
            rolemodel.setModelId(model.getModelId());
            rolemodel.setRoleId(managerSession.getManagerRoleID());
            roleModels.add(rolemodel);
            roleModelBiz.saveEntity(roleModels);
        }
        if (StringUtil.isBlank(model.getModelCode())){
            //将模块编号设置为模块编码
            model.setModelCode(model.getModelId()+"");
        }
        modelBiz.updateEntity(model);
        //返回模块id到页面
        return ResultData.build().success(String.valueOf(model.getModelId()));
    }

    /**
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId:多个modelId直接用逗号隔开,例如modelId=1,2,3,4
     * 批量删除模块表
     *            <dt><span class="strong">返回</span></dt><br/>
     *            <dd>{code:"错误编码",<br/>
     *            result:"true｜false",<br/>
     *            resultMsg:"错误信息"<br/>
     *            }</dd>
     */
    @ApiOperation(value = "批量删除模块表")
    @ApiImplicitParam(name = "ids", value = "模块编号，多个以逗号隔开", required = false,paramType="query")
    @LogAnn(title = "批量删除模块表",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("model:del")
    public ResultData delete(HttpServletResponse response, HttpServletRequest request) {
        int[] ids = BasicUtil.getInts("ids", ",");
        modelBiz.delete(ids);
        return ResultData.build().success();
    }

    /**
     * 更新模块表信息模块表
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId 模块自增长id<br/>
     * modelTitle 模块标题<br/>
     * modelCode 模块编码<br/>
     * modelModelid 模块的父模块id<br/>
     * modelUrl 模块连接地址<br/>
     * modelDatetime <br/>
     * modelIcon 模块图标<br/>
     * modelModelmanagerid 模块关联的关联员id<br/>
     * modelSort 模块的排序<br/>
     * modelIsmenu 模块是否是菜单<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * modelId: 模块自增长id<br/>
     * modelTitle: 模块标题<br/>
     * modelCode: 模块编码<br/>
     * modelModelid: 模块的父模块id<br/>
     * modelUrl: 模块连接地址<br/>
     * modelDatetime: <br/>
     * modelIcon: 模块图标<br/>
     * modelModelmanagerid: 模块关联的关联员id<br/>
     * modelSort: 模块的排序<br/>
     * modelIsmenu: 模块是否是菜单<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "更新模块表信息模块表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模块的编号", required = true,paramType="query"),
            @ApiImplicitParam(name = "modelTitle", value = "模块的标题", required = true,paramType="query"),
            @ApiImplicitParam(name = "modelCode", value = "模块编码", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelModelId", value = "模块父id", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelUrl", value = "链接地址", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelDatetime", value = "发布时间", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelIcon", value = "模块图标", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelManagerId", value = "模块管理员Id", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelSort", value = "模块排序", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelIsMenu", value = "是否是菜单", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "菜单类型", required = false,paramType="query"),
            @ApiImplicitParam(name = "modelParentIds", value = "父级编号集合", required = false,paramType="query"),
    })
    @LogAnn(title = "更新模块表信息模块表",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("model:update")
    @ResponseBody
    public ResultData update(@ModelAttribute @ApiIgnore ModelEntity model, HttpServletResponse response,
                       HttpServletRequest request) {
        //模块标题验证
        if(StringUtil.isBlank(model.getModelTitle())){
            return ResultData.build().error(getResString("err.empty", this.getResString("model.title")));
        }
        if(!StringUtil.checkLength(model.getModelTitle()+"", 1, 10)){
            return ResultData.build().error(getResString("err.length", this.getResString("model.title"), "1", "10"));
        }
        //判断菜单名称不能相同
        if(model.getModelIsMenu() == ModelIsMenuEnum.MODEL_MEUN.toInt()){
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setModelTitle(model.getModelTitle());
            modelEntity.setModelIsMenu(ModelIsMenuEnum.MODEL_MEUN.toInt());
            ModelEntity _modelEntity = (ModelEntity) modelBiz.getEntity(modelEntity);
            if(ObjectUtil.isNotEmpty(_modelEntity) && _modelEntity.getModelId() != model.getModelId()){
                return ResultData.build().error(getResString("err.exist",this.getResString("model.title")));
            }
        }
        //判断当前修改的菜单是否是三级菜单
        ModelEntity _model = (ModelEntity) modelBiz.getEntity(model.getModelId());
        if(_model.getModelIsMenu() == 1 && model.getModelIsMenu() == 0){
            return ResultData.build().error(this.getResString("model.is.menu"));
        }
        //判断图标是否为空，不为空去掉,图标地址中含有的“|”
        //空值判断
        if(!StringUtil.isBlank(model.getModelIcon())) {
            model.setModelIcon( model.getModelIcon().replace("|", ""));
        }
        //重复判断，modelCode不能重复
        ModelEntity modelEntity = modelBiz.getEntityByModelCode(model.getModelCode());
        if (!StringUtil.isBlank(modelEntity) && modelEntity.getModelId() != model.getModelId()){
            return ResultData.build().error(getResString("err.exist",this.getResString("modelCode")));
        }
        modelBiz.updateEntity(model);
        return ResultData.build().success(String.valueOf(model.getModelId()));
    }

    /**
     * 根据管理员ID查询模块集合
     * @param managerId 管理员id
     * @param request 请求对象
     * @param response 响应对象
     */
    @ApiOperation(value = "根据管理员ID查询模块集合")
    @ApiImplicitParam(name = "managerId", value = "管理员id", required = true,paramType="path")
    @GetMapping("/{managerId}/queryModelByRoleId")
    @ResponseBody
    public ResultData queryModelByRoleId(@PathVariable @ApiIgnore int managerId,HttpServletRequest request, HttpServletResponse response) {
        ManagerEntity manager =(ManagerEntity) managerBiz.getEntity(managerId);
        if(manager==null){
            return ResultData.build().error();
        }
        List<BaseEntity> modelList = new ArrayList<BaseEntity>();
        modelList = modelBiz.queryModelByRoleId(manager.getManagerRoleID());
        return ResultData.build().success(modelList);
    }

    /**
     * 查询模块表列表
     * @param model 模块表实体
     * <i>model参数包含字段信息参考：</i><br/>
     * modelId 模块自增长id<br/>
     * modelTitle 模块标题<br/>
     * modelCode 模块编码<br/>
     * modelModelid 模块的父模块id<br/>
     * modelUrl 模块连接地址<br/>
     * modelDatetime <br/>
     * modelIcon 模块图标<br/>
     * modelModelmanagerid 模块关联的关联员id<br/>
     * modelSort 模块的排序<br/>
     * modelIsmenu 模块是否是菜单<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>[<br/>
     * { <br/>
     * modelId: 模块自增长id<br/>
     * modelTitle: 模块标题<br/>
     * modelCode: 模块编码<br/>
     * modelModelid: 模块的父模块id<br/>
     * modelUrl: 模块连接地址<br/>
     * modelDatetime: <br/>
     * modelIcon: 模块图标<br/>
     * modelModelmanagerid: 模块关联的关联员id<br/>
     * modelSort: 模块的排序<br/>
     * modelIsmenu: 模块是否是菜单<br/>
     * }<br/>
     * ]</dd><br/>
     */
    @ApiOperation(value = "查询模块表列表")
    @ApiImplicitParam(name = "roleId", value = "角色编号", required = true,paramType="query")
    @GetMapping("/modelList")
    @ResponseBody
    public ResultData modelList(@ModelAttribute @ApiIgnore ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        int roleId = BasicUtil.getInt("roleId");
        ManagerSessionEntity managerSession = this.getManagerBySession();
        int currentRoleId = managerSession.getManagerRoleID();
        boolean updateFalg = true;
        //新增角色roleId为0，默认当前管理员的roleId
        if(roleId == 0){
            updateFalg = false;
            roleId = currentRoleId;
        }
        List<BaseEntity> modelList = modelBiz.queryModelByRoleId(currentRoleId);
        List<ModelEntity> _modelList = new ArrayList<>();
        List<RoleModelEntity> roleModelList = new ArrayList<>();
        if(roleId>0){
            roleModelList = roleModelBiz.queryByRoleId(roleId);
        }
        List<ModelEntity> childModelList = new ArrayList<>();
        //将菜单和功能区分开
        for(BaseEntity base : modelList){
            ModelEntity _model = (ModelEntity) base;
            if(_model.getModelIsMenu() == 1){
                _model.setModelChildList(new ArrayList<ModelEntity>());
                _modelList.add(_model);
            }else if(_model.getModelIsMenu() == 0){
                childModelList.add(_model);
            }
        }
        //菜单和功能一一匹配
        for(ModelEntity _modelEntity : _modelList){
            for(ModelEntity childModel : childModelList){
                if(childModel.getModelModelId() == _modelEntity.getModelId()){
                    _modelEntity.getModelChildList().add(childModel);
                    //选中状态
                    for(RoleModelEntity roleModelEntity : roleModelList){
                        if(roleModelEntity.getModelId() == childModel.getModelId() && updateFalg){
                            childModel.setChick(1);
                        }
                    }

                }
            }
        }
        EUListBean _list = new EUListBean(_modelList, _modelList.size());
        return ResultData.build().success(_list);
    }
}
