<!DOCTYPE html>
<html>
<head>
    <title>模板管理</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" class="ms-index" v-cloak class="ms-index">
    <el-header class="ms-header" height="50px">
        <el-col span="2">
            <el-upload
                    size="mini"
                    :file-list="fileList"
                    :show-file-list="false"
                    :action="ms.manager+'/file/uploadTemplate.do'"
                    :style="{width:''}"
                    accept=".zip"
                    :disabled="false"
                    :on-success="fileUploadSuccess"
                    :data="{uploadPath:uploadPath,uploadFloderPath:true}">
                <el-tooltip effect="dark" content="只允许上传zip文件！" placement="left-end">
                <el-button size="small" icon="el-icon-upload2" type="primary">点击上传</el-button>
                </el-tooltip>
            </el-upload>
        </el-col>
    </el-header>
    <el-main class="ms-container">
        <el-table height="calc(100vh - 68px)" v-loading="loading" ref="multipleTable" border :data="dataList"
                  tooltip-effect="dark" @selection-change="handleSelectionChange">
            <template slot="empty">
                {{emptyText}}
            </template>
            <el-table-column label="模板名称" align="left" prop="folderName">
                <template slot-scope="scope">
                    <div style="margin-left: 5px;display: flex;align-items: center;justify-content: flex-start;">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-wenjianjia2"></use>
                    </svg>
                    <span style="margin-left: 5px">{{scope.row.folderName}}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="类型" width="100" align="center" prop="folderType">
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="scope">
                    <el-link type="primary" :underline="false" @click="view(scope.row.folderName)">查看</el-link>
                    <@shiro.hasPermission name="template:del">
                        <el-link type="primary" :underline="false" @click="del(scope.row.folderName)">删除</el-link>
                    </@shiro.hasPermission>
                </template>
            </el-table-column>
        </el-table>
    </el-main>
</div>
</body>

</html>
<script>
    var indexVue = new Vue({
        el: '#index',
        data: {
            dataList: [],
            //模板管理列表
            selectionList: [],
            //模板管理列表选中
            manager: ms.manager,
            loading: true,
            //加载状态
            emptyText: '',
            //提示文字
            uploadPath: '',
            fileList: [],
            websiteId: null
        },
        methods: {
            //查询列表
            list: function () {
                var that = this;
                that.loading = true;
                setTimeout(function () {
                    ms.http.get(ms.manager + "/template/queryTemplateSkin.do").then(function (res) {
                        that.dataList = [];
                        if(res.data.folderNameList!=null){
                            res.data.folderNameList.forEach(function (item) {
                                var type = "文件夹";
                                that.dataList.push({
                                    folderName: item,
                                    folderType: type
                                });
                            });
                        }
                        that.uploadPath = "templets/" + res.data.websiteId + "/";
                        that.websiteId = res.data.websiteId;
                    }).catch(function (err) {
                        console.log(err);
                    }).finally(function () {
                        that.loading = false;
                    });
                }, 500);
            },
            //模板管理列表选中
            handleSelectionChange: function (val) {
                this.selectionList = val;
            },
            //删除
            del: function (name) {
                var that = this;
                that.$confirm('确定删除该模板吗？', '删除模板', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(ms.manager + "/template/delete.do", {
                        fileName: name
                    }).then(function (res) {
                        if (res.result) {
                            that.$notify({
                                type: 'success',
                                message: '删除成功!'
                            }); //删除成功，刷新列表

                            that.list();
                        } else {
                            that.$notify({
                                title: '失败',
                                message: res.msg,
                                type: 'warning'
                            });
                        }
                    });
                }).catch(function () {
                    that.$notify({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            //新增
            view: function (name) {
                window.location.href = ms.manager + "/template/list.do?template=" + "templets/" + this.websiteId + "/" + name;
            },
            //表格数据转换
            //pageSize改变时会触发
            sizeChange: function (pagesize) {
                this.loading = true;
                this.pageSize = pagesize;
                this.list();
            },
            //currentPage改变时会触发
            currentChange: function (currentPage) {
                this.loading = true;
                this.currentPage = currentPage;
                this.list();
            },
            //重置表单
            rest: function () {
                this.list();
            },
            //fileUpload文件上传完成回调
            fileUploadSuccess: function (response, file, fileList) {
                var that = this;
                ms.http.get(ms.manager + "/template/unZip.do", {

                    fileUrl: response
                }).then(function () {
                    debugger
                    that.list();
                });
                debugger

                this.fileList.push({
                    url: file.url,
                    name: file.name,
                    path: response,
                    uid: file.uid
                });
            }
        },
        mounted: function () {
            this.list();
        }
    });
</script>
<style>
    #index .ms-container {
        height: calc(100vh - 78px);
    }
    .icon {
        width: 2em;
        height: 2em;
        vertical-align: -0.15em;
        fill: currentColor;
        overflow: hidden;
    }
</style>
