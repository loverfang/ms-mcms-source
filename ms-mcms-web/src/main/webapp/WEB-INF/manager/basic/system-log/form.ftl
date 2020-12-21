<!DOCTYPE html>
<html>
<head>
    <title>系统日志</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="form" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button size="mini" icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
    </el-header>
    <el-main class="ms-container">
        <el-scrollbar class="ms-scrollbar" style="height: 100%;">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="标题" prop="title">
                        <el-input v-model="form.title"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入标题">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="IP" prop="ip">
                        <el-input v-model="form.ip"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入IP">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="请求方法" prop="method">
                        <el-input v-model="form.method"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入请求方法">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="请求方式" prop="requestMethod">
                        <el-select v-model="form.requestMethod"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="true"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择请求方式">
                            <el-option v-for='item in requestMethodOptions' :key="item.value" :value="item.value"
                                       :label="false?item.label:item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="请求地址" prop="url">
                        <el-input v-model="form.url"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入请求地址">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="请求状态" prop="status">
                        <el-select v-model="form.status"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="true"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择请求状态">
                            <el-option v-for='item in statusOptions' :key="item.value" :value="item.value"
                                       :label="true?item.label:item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="业务类型" prop="businessType">
                        <el-select v-model="form.businessType"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="true"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择业务类型">
                            <el-option v-for='item in businessTypeOptions' :key="item.value" :value="item.value"
                                       :label="true?item.label:item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="用户类型" prop="userType">
                        <el-select v-model="form.userType"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="true"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择用户类型">
                            <el-option v-for='item in userTypeOptions' :key="item.value" :value="item.value"
                                       :label="true?item.label:item.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="操作人员" prop="user">
                        <el-input v-model="form.user"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入操作人员">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="所在地区" prop="location">
                        <el-input v-model="form.location"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入所在地区">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col span="12">
                    <el-form-item label="请求时间" prop="createDate">
                        <el-input v-model="form.createDate"
                                  :readonly="true"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入请求时间">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col span="12">
                    <el-form-item label="请求参数" prop="param">
                        <el-input
                                type="textarea" :rows="10"
                                :readonly="true"
                                v-model="form.param"
                                :style="{width: '100%'}"
                                placeholder="请输入请求参数">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="返回参数" prop="result">
                        <el-input
                                type="textarea" :rows="10"
                                :readonly="true"
                                v-model="form.result"
                                :style="{width: '100%'}"
                                placeholder="请输入返回参数">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item label="错误消息" prop="errorMsg">
                <el-input
                        type="textarea" :rows="10"
                        :readonly="true"
                        v-model="form.errorMsg"
                        :style="{width: '100%'}"
                        placeholder="请输入错误消息">
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
        data: function () {
            return {
                saveDisabled: false,
                //表单数据
                form: {
                    // 标题
                    title: '',
                    // IP
                    ip: '',
                    // 请求方法
                    method: '',
                    // 请求方式
                    requestMethod: '',
                    // 请求地址
                    url: '',
                    // 请求状态
                    status: '',
                    // 业务类型
                    businessType: 'insert',
                    // 用户类型
                    userType: '',
                    // 操作人员
                    user: '',
                    // 所在地区
                    location: '',
                    // 请求时间
                    createDate: '',
                    // 请求参数
                    param: '',
                    // 返回参数
                    result: '',
                    // 错误消息
                    errorMsg: ''
                },
                requestMethodOptions: [{
                    "value": "get"
                }, {
                    "value": "post"
                }, {
                    "value": "put"
                }],
                statusOptions: [{
                    "value": "success",
                    "label": "成功"
                }, {
                    "value": "error",
                    "label": "失败"
                }],
                businessTypeOptions: [{
                    "value": "insert",
                    "label": "新增"
                }, {
                    "value": "delete",
                    "label": "删除"
                }, {
                    "value": "update",
                    "label": "修改"
                }, {
                    "value": "other",
                    "label": "其他"
                }],
                userTypeOptions: [{
                    "value": "other",
                    "label": "其他"
                }, {
                    "value": "manage",
                    "label": "管理员"
                }, {
                    "value": "people",
                    "label": "会员"
                }],
                rules: {}
            };
        },
        watch: {},
        computed: {},
        methods: {
            save: function () {
                var that = this;
                var url = ms.manager + "/basic/systemLog/save.do";

                if (that.form.id > 0) {
                    url = ms.manager + "/basic/systemLog/update.do";
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.data.id > 0) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                location.href = ms.manager + "/basic/systemLog/index.do";
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                            }

                            that.saveDisabled = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取当前系统日志
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/basic/systemLog/get.do", {
                    "id": id
                }).then(function (data) {
                    if (data.data.id) {
                        that.form = data.data;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function () {
            this.form.id = ms.util.getParameter("id");

            if (this.form.id) {
                this.get(this.form.id);
            }
        }
    });
</script>
