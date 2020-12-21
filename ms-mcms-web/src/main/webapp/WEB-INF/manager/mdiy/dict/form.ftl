<!DOCTYPE html>
<html>
<head>
	 <title>字典表</title>
        <#include "../../include/head-file.ftl">
</head>
<body>
	<div id="form" v-cloak>
		<el-header class="ms-header ms-tr" height="50px">
			<el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
			<el-button size="mini" icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
		</el-header>
		<el-main class="ms-container">
            <el-scrollbar class="ms-scrollbar" style="height: 100%;">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" size="mini">
                        <el-row
                                gutter="0"
                                justify="start" align="top">
                                <el-col span="12">
               <el-form-item  label="标签名" prop="dictLabel">
                    <el-input v-model="form.dictLabel"
                              :disabled="false"
                              :style="{width:  '100%'}"
                              :clearable="true"
                              placeholder="请输入标签名">
                    </el-input>
               </el-form-item>
                                </el-col>
                                <el-col span="12">
               <el-form-item  label="类型" prop="dictType">
                   <span slot='label'>类型
                    <el-popover placement="top-start" title="提示" trigger="hover" content="直接输入可以创建一条新的类型">
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                    </span>
                   <el-select v-model="form.dictType"
                              :style="{width: '100%'}"
                              :disabled="false"
                              filterable
                              allow-create
                              default-first-option
                              :multiple="false" :clearable="true"
                              placeholder="请选择类型">
                       <el-option v-for='item in dictTypeOptions' :key="item.dictType" :value="item.dictType"
                                  :label="item.dictType"></el-option>
                   </el-select>
               </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                gutter="0"
                                justify="start" align="top">
                                <el-col span="12">
               <el-form-item  label="数据值" prop="dictValue">
                    <el-input v-model="form.dictValue"
                              :disabled="false"
                              :style="{width:  '100%'}"
                              :clearable="true"
                              placeholder="请输入数据值">
                    </el-input>
               </el-form-item>
                                </el-col>
                                <el-col span="12">
                                    <el-form-item  label="排序" prop="dictSort">
                                        <el-input v-model="form.dictSort"
                                                  :disabled="false"
                                                  :style="{width:  '100%'}"
                                                  :clearable="true"
                                                  placeholder="请输入排序">
                                        </el-input>
                                    </el-form-item>
                                </el-col>
                        </el-row>
                <el-row >
                    <el-col span="12">
                        <el-form-item  label="子业务类型" prop="isChild">
                        <template slot='label'>系统扩展
                        <el-popover placement="top-start" title="提示" trigger="hover">
                            <template slot-scope="slot">
                                可扩展到多个业务模块使用，<br/>例如：业务A定义了一些字典数据，业务B也定义了字典数据，一般我们会选择两个业务中都需要开发字典管理功能<br/>就可以通过这个参数来过滤，避免业务A出现业务B中定义的字典数据，也减少了重复开发字典的工作<br/>
                                <el-link :underline="false"  target="_blank" type="primary" :href="location.origin+ms.base+'/mdiy/dict/list.do?isChild=订单'" >前端获取订单接口地址</el-link>
                            </template>
                            <i class="el-icon-question" slot="reference"></i>
                        </el-popover>
                        </template>
                            <el-input v-model="form.isChild"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入子业务数据类型">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

               <el-form-item  label="备注信息" prop="dictRemarks">
                    <el-input
                            type="textarea" :rows="10"
                            :disabled="false"
                            v-model="form.dictRemarks"
                            :style="{width: '100%'}"
                            placeholder="请输入备注信息">
                    </el-input>
               </el-form-item>
            </el-form>
            </el-scrollbar>
		</el-main>
	</div>
	</body>
	</html>
<script>
    var form = new Vue({
        el: '#form',
        data: function() {
            return {
                saveDisabled: false,
                //表单数据
                form: {
                    // 标签名
                    dictLabel: '',
                    // 类型
                    dictType: '',
                    // 数据值
                    dictValue: '',
                    // 子业务数据类型
                    isChild: '',
                    // 排序
                    dictSort: '',
                    // 备注信息
                    dictRemarks: ''
                },
                dictTypeOptions: [],
                rules: {
                    //标签名
                    dictLabel: [{
                        "required": true,
                        "message": "标签名必须填写"
                    }],
                    //类型
                    dictType: [{
                        "required": true,
                        "message": "标签名必须填写"
                    }],
                    // 数据值
                    dictValue: [{
                        "required": true,
                        "message": "标签名必须填写"
                    }, {
                        "min": 1,
                        "max": 100,
                        "message": "数据值长度必须为1-100"
                    }],
                    // 子业务数据类型
                    isChild: [{
                        "min": 1,
                        "max": 300,
                        "message": "子业务数据类型长度必须为1-300"
                    }],
                    // 排序
                    // dictSort: [{"required":true,"message":"排序必须填写"},{"min":1,"max":3,"message":"排序长度必须为1-3"}],
                    // 备注信息
                    dictRemarks: [{
                        "min": 1,
                        "max": 1000,
                        "message": "备注信息长度必须为1-1000"
                    }]
                }
            };
        },
        watch: {},
        computed: {},
        methods: {
            save: function () {
                var that = this;
                var url = ms.manager + "/mdiy/dict/save.do";

                if (that.form.dictId > 0) {
                    url = ms.manager + "/mdiy/dict/update.do";
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                location.href = ms.manager + "/mdiy/dict/index.do";
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
            //获取当前字典表
            get: function (dictId) {
                var that = this;
                ms.http.get(ms.manager + "/mdiy/dict/get.do", {
                    "dictId": dictId
                }).then(function (data) {
                    if (data.data.dictId) {
                        that.form = data.data;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
            getDictType: function (isChild) {
                var that = this;
                that.form.dictType = '';
                ms.http.get(ms.manager + "/mdiy/dict/dictType.do?pageSize=999", isChild ? {
                    "isChild": isChild
                } : null).then(function (data) {
                    if (data.data.rows) {
                        that.dictTypeOptions = data.data.rows;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function () {
            this.getDictType();
            this.form.dictId = ms.util.getParameter("dictId");
            this.form.dictType = ms.util.getParameter("dictType");
            if (this.form.dictId) {
                this.get(this.form.dictId);
            }
        }
    });
</script>
