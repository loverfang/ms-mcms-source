<el-dialog id="form" :title="(addEditForm.modelIsMenu == 1) ? '菜单编辑' : '按钮编辑'" :visible.sync="dialogVisible" width="50%" v-cloak>
    <el-form ref="addEditForm" :model="addEditForm" :rules="rules" label-width="110px" size="mini">
        <el-form-item  label="标题" prop="modelTitle">
            <el-input v-model="addEditForm.modelTitle" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item  label="父级菜单" prop="modelModelId">
            <ms-tree-select ref="treeselect"
                    :props="props"
                    :options="modelList"
                    :value="addEditForm.modelModelId"
                    :clearable="isClearable"
                    :accordion="isAccordion"
                    @get-value="getValue($event)"></ms-tree-select>
        </el-form-item>
        <el-form-item prop="modelIsMenu">
            <template slot='label'>是否为菜单
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        导航连接为菜单，页面中的功能按钮为非菜单
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>        
            <el-radio-group v-model="addEditForm.modelIsMenu">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item  label="图标" prop="modelIcon" v-if="addEditForm.modelIsMenu == 1">
            <ms-icon v-model="addEditForm.modelIcon"></ms-icon>
        </el-form-item>
        <el-form-item prop="modelUrl">
            <template slot='label'>{{(addEditForm.modelIsMenu==1) ? '链接地址' : '权限标识'}}
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        <span v-if="addEditForm.modelIsMenu==1">
                            导航地址，如“model/index.do”
                        </span>
                        <span v-else>
                            按钮的权限标识，推荐格式：业务名:update,业务名:del,业务名:view，具体使用参考：<a href="http://doc.ms.mingsoft.net/dev-guide/" target="_blank">开发手册</a>
                        </span>
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model="addEditForm.modelUrl" :placeholder="(addEditForm.modelIsMenu==1) ? '请输入链接地址' : '请输入权限标识'"></el-input>
        </el-form-item>
<#--        <el-form-item  label="模块编码" prop="modelCode">-->
<#--            <el-input v-model="addEditForm.modelCode" placeholder="请输入模块编码"></el-input>-->
<#--        </el-form-item>-->
        <el-form-item prop="modelSort">
            <template slot='label'>排序
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        菜单列表会根据倒序排列，左侧导航菜单也会根据倒序排序
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model.number="addEditForm.modelSort" maxlength="11" placeholder="请输入排序"></el-input>
        </el-form-item>
        <el-form-item  prop="isChild">
            <template slot='label'>系统扩展
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        可扩展到多个业务模块使用，<br/>
                        例如：业务A定义了一些菜单，业务B也定义了菜单，一般我们会选择两个业务中都需要开发菜单管理功能<br/>
                        就可以通过这个参数来过滤，避免业务A出现业务B中定义的菜单数据，也减少了重复开发菜单的工作<br/>
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model="addEditForm.isChild"
                      :disabled="false"
                      :style="{width:  '100%'}"
                      :clearable="true"
                      placeholder="请输入系统扩展">
            </el-input>
        </el-form-item>
    </el-form>
    <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                isClearable: false,
                // 可清空（可选）
                isAccordion: true,
                // 可收起（可选）
                modelTitle: '',
                props: {
                    // 配置项（必选）
                    value: 'modelId',
                    label: 'modelTitle',
                    children: 'children' // disabled:true

                },
                modelList: [],
                //菜单数据
                modeldata: [],
                saveDisabled: false,
                dialogVisible: false,
                //表单数据
                addEditForm: {
                    modelId: 0,
                    modelModelId: 0,
                    modelTitle: '',
                    modelIcon: '',
                    modelUrl: '',
                    isChild: '',
                    // modelCode:'',
                    modelSort: 0,
                    modelIsMenu: 1
                },
                rules: {
                    modelTitle: [{
                        required: true,
                        message: '请输入标题',
                        trigger: 'blur'
                    }, {
                        min: 1,
                        max: 20,
                        message: '长度不能超过20个字符',
                        trigger: 'change'
                    }],
                    modelUrl: [{
                        min: 0,
                        max: 100,
                        message: '长度不能超过100个字符',
                        trigger: 'change'
                    }],
                    // modelCode:[{min: 0, max: 20, message: '长度不能超过20个字符', trigger: 'change'}],
                    modelSort: [{
                        type: 'number',
                        message: '排序必须为数字值'
                    }],
                    isChild: [{
                        min: 0,
                        max: 100,
                        message: '长度不能超过100个字符',
                        trigger: 'change'
                    }]
                }
            };
        },
        watch: {
            'dialogVisible': function (n, o) {
                if (!n) {
                    this.$refs.addEditForm.resetFields();
                }
            },
            'modeldata': function (n, o) {
                if (n) {
                    this.modelList.push({
                        modelTitle: '顶级菜单',
                        modelId: 0,
                        children: this.modeldata
                    });
                }
            }
        },
        methods: {
            open: function (id) {
                this.addEditForm.modelId = 0;
                this.addEditForm.modelModelId = '';

                if (id > 0) {
                    this.get(id);
                }

                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/model/save.do";

                if (that.addEditForm.modelId > 0) {
                    url = ms.manager + "/model/update.do";
                } //按钮没有图标


                if (that.addEditForm.modelIsMenu == 0) {
                    that.addEditForm.modelIcon = '';
                }

                that.$refs.addEditForm.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;

                        if (!that.addEditForm.modelModelId) {
                            delete that.addEditForm.modelModelId;
                        }

                        var data = JSON.parse(JSON.stringify(that.addEditForm));
                        delete data.modelChildList;
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                that.dialogVisible = false;
                                window.location.href = ms.manager + "/model/index.do";
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                                that.saveDisabled = false;
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            getValue: function (data) {
                if (data.node.modelIsMenu == 0) {
                    this.$notify({
                        title: '提示',
                        message: '不能将功能按钮添加为菜单',
                        type: 'info'
                    });
                } else {
                    this.addEditForm.modelModelId = data.node.modelId;
                    this.$refs.treeselect.valueId = data.node[this.props.value];
                    this.$refs.treeselect.valueTitle = data.node[this.props.label];
                    data.dom.blur();
                }
            },
            //获取当前任务
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/model/get.do", {
                    modelId: id
                }).then(function (data) {
                    if (data.result) {
                        that.addEditForm = data.data.model;
                        delete that.addEditForm.modelDatetime;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function () {}
    });
</script>
<style>
    #form .el-select{
        width: 100%;
    }
</style>
