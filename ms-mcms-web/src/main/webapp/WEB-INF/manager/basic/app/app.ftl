<!DOCTYPE html>
<html>
<head>
    <title>应用设置</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="form" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
    </el-header>
    <el-main class="ms-container">
        <el-scrollbar class="ms-scrollbar" style="height: 100%;">
        <el-form ref="form" :model="form" :rules="rules" label-width="140px" size="mini">
            <el-row>
                <el-col span="12">
                    <el-form-item label="网站标题" prop="appName">
                        <template slot='label'>网站标题
                            <el-popover placement="top-start" title="提示" trigger="hover" >
                                <a href="http://doc.ms.mingsoft.net/plugs-cms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.name/}</a>
                                <i class="el-icon-question" slot="reference"></i>
                            </el-popover>
                        </template>
                        <el-input v-model="form.appName"
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入网站标题">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item  label="站点风格" prop="appStyle">
                        <el-select v-model="form.appStyle"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="false"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择站点风格">
                            <el-option v-for='item in appStyleOptions' :key="item" :value="item"
                                       :label="item"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item  label="" prop="appLogo">
                <template slot='label'>网站Logo
                    <el-popover placement="top-start" title="提示" trigger="hover" >
                        <a href="http://doc.ms.mingsoft.net/plugs-cms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.logo/}</a>
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                </template>
                <el-upload
                        :file-list="form.appLogo"
                        :action="ms.base+'/file/upload.do'"
                        :on-remove="appLogohandleRemove"
                        :style="{width:''}"
                        :limit="1"
                        :on-exceed="appLogohandleExceed"
                        :disabled="false"
                        :data="{uploadPath:'/appLogo','isRename':true,'appId':true}"
                        :on-success="appLogoSuccess"
                        accept="image/*"
                        list-type="picture-card">
                    <i class="el-icon-plus"></i>
                    <div slot="tip" class="el-upload__tip">支持jpg,png格式，最多上传1张图片</div>
                </el-upload>
            </el-form-item>
            <el-form-item label="关键字" prop="appKeyword">
                <template slot='label'>关键字
                    <el-popover placement="top-start" title="提示" trigger="hover" >
                        <a href="http://doc.ms.mingsoft.net/plugs-cms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.keyword/}</a>
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                </template>
                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appKeyword"
                        :style="{width: '100%'}"
                        placeholder="请输入关键字">
                </el-input>
            </el-form-item>
            <el-form-item label="描述" prop="appDescription">
                <template slot='label'>描述
                    <el-popover placement="top-start" title="提示" trigger="hover" >
                        <a href="http://doc.ms.mingsoft.net/plugs-cms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.descrip/}</a>
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                </template>
                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appDescription"
                        :style="{width: '100%'}"
                        placeholder="请输入描述">
                </el-input>
            </el-form-item>
            <el-form-item label="版权信息" prop="appCopyright">
                <template slot='label'>版权信息
                    <el-popover placement="top-start" title="提示" trigger="hover" >
                        <a href="http://doc.ms.mingsoft.net/plugs-cms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.copyright/}</a>
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                </template>
                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appCopyright"
                        :style="{width: '100%'}"
                        placeholder="请输入版权信息">
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
                    // 站点名称
                    appName: '',
                    // 移动站状态
                    appMobileStyle: '',
                    // 站点风格
                    appStyle: [],
                    // 网站Logo
                    appLogo: [],
                    // 关键字
                    appKeyword: '',
                    // 描述
                    appDescription: '',
                    // 版权信息
                    appCopyright: ''
                },
                appStyleOptions: [],
                rules: {
                    // 网站标题
                    appName: [{
                        "required": true,
                        "message": "网站标题必须填写"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "站点名称长度必须为10-150"
                    }],
                    appDescription: [{
                        "min": 0,
                        "max": 1000,
                        "message": "描述长度必须小于1000"
                    }],
                    appKeyword: [{
                        "min": 0,
                        "max": 1000,
                        "message": "关键字长度必须小于1000"
                    }],
                    appCopyright: [{
                        "min": 0,
                        "max": 1000,
                        "message": "版权信息长度必须小于1000"
                    }]
                }
            };
        },
        watch: {},
        computed: {},
        methods: {
            save: function () {
                var that = this;
                url = ms.manager + "/app/update.do";
                this.$refs.form.validate(function(valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        if(data.appLogo){
                            data.appLogo = JSON.stringify(data.appLogo);
                        }
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
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
            //获取当前应用表
            get: function (id) {
                var that = this;
                this.loading = true
                ms.http.get(ms.manager + "/app/"+id+"/get.do", {"id":id}).then(function (res) {
                    that.loading = false
                    if(res.result && res.data){
                        if(res.data.appLogo){
                            res.data.appLogo = JSON.parse(res.data.appLogo);
                            res.data.appLogo.forEach(function(value){
                                value.url= ms.base + value.path
                            })
                        }else{
                            res.data.appLogo=[]
                        }
                        that.form = res.data;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //上传超过限制
            appLogohandleExceed:function(files, fileList) {
                this.$notify({ title: '当前最多上传1张图片', type: 'warning' });},
            appLogohandleRemove:function(file, files) {
                var index = -1;
                index = this.form.appLogo.findIndex(function(e){return e == file} );
                if (index != -1) {
                    this.form.appLogo.splice(index, 1);
                }
            },
            disabledDate: function () {},
            //获取appStyle数据源
            appStyleOptionsGet: function () {
                var that = this;
                ms.http.get(ms.manager + '/template/queryAppTemplateSkin.do', {
                    pageSize: 99999
                }).then(function (data) {
                    that.appStyleOptions = data.data.fileName;
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //appLogo文件上传完成回调
            appLogoSuccess: function (response, file, fileList) {
                this.form.appLogo.push({url:file.url,name:file.name,path:response,uid:file.uid});
            },
            //上传超过限制
            appLogohandleExceed: function (files, fileList) {
                this.$notify({
                    title: '当前最多上传1个文件',
                    type: 'warning'
                });
            },
            appLogohandleRemove: function (file, files) {
                var index = -1;
                index = this.form.appLogo.findIndex(function (text) {
                    return text == file;
                });

                if (index != -1) {
                    this.form.appLogo.splice(index, 1);
                }
            }
        },
        created: function () {
            this.appStyleOptionsGet();
            this.form.id = -1;

            if (this.form.id) {
                this.get(this.form.id);
            } else {
                delete this.form.id;
            }
        }
    });
</script>
