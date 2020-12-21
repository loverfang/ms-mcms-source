<!DOCTYPE html>
<html>
<head>
    <title>系统日志</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
    <ms-search ref="search" @search="search" :condition-data="conditionList" :conditions="conditions"></ms-search>
    <div class="ms-search">
        <el-row>
            <el-form :model="form" ref="searchForm" label-width="120px" size="mini">
                <el-row style="padding-right: 10px;">
                    <el-col :span="8">
                        <el-form-item label="标题" prop="title">
                            <el-input v-model="form.title"
                                      :style="{width:  '100%'}"
                                      placeholder="请输入标题">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="请求状态" prop="status">
                            <el-select v-model="form.status"
                                       :style="{width: '100%'}"
                                       :filterable="false"
                                       :multiple="false" :clearable="true"
                                       placeholder="请选择请求状态">
                                <el-option v-for='item in statusOptions' :key="item.value" :value="item.value"
                                           :label="true?item.label:item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="请求地址" prop="url">
                            <el-input v-model="form.url"
                                      :style="{width:  '100%'}"
                                      placeholder="请输入请求地址">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row style="padding-right: 10px;">
                    <el-col :span="8">
                        <el-form-item label="业务类型" prop="businessType">
                            <el-select v-model="form.businessType"
                                       :style="{width: '100%'}"
                                       :filterable="false"
                                       :multiple="false" :clearable="true"
                                       placeholder="请选择业务类型">
                                <el-option v-for='item in businessTypeOptions' :key="item.value" :value="item.value"
                                           :label="true?item.label:item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="操作人员" prop="user">
                            <el-input v-model="form.user"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入操作人员">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" style="text-align: right;">
                        <el-button type="primary" icon="el-icon-search" size="mini"
                                   @click="loading=true;currentPage=1;list()">查询
                        </el-button>
                        <el-button type="primary" icon="iconfont icon-shaixuan1" size="mini" @click="$refs.search.open()">筛选</el-button>
                        <el-button @click="rest" icon="el-icon-refresh" size="mini">重置</el-button>
                    </el-col>
                </el-row>
            </el-form>
        </el-row>
    </div>
    <el-main class="ms-container">
        <el-table v-loading="loading" ref="multipleTable" height="calc(100vh-68px)" class="ms-table-pagination" border :data="dataList"
                  tooltip-effect="dark" @selection-change="handleSelectionChange">
            <template slot="empty">
                {{emptyText}}
            </template>
            <el-table-column label="标题" width="180" align="left" prop="title" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="请求地址" align="left" prop="url" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="请求状态" align="center" width="90px" prop="status" :formatter="statusFormat" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="操作人员" align="left" width="120px" prop="user" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="请求时间" align="left" width="180px" prop="createDate">
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="scope">
                    <@shiro.hasPermission name="basic:systemLog:view">
                        <el-link type="primary" :underline="false" @click="save(scope.row.id)">查看</el-link>
                    </@shiro.hasPermission>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                background
                :page-sizes="[5, 10, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="total"
                class="ms-pagination"
                @current-change='currentChange'
                @size-change="sizeChange">
        </el-pagination>
    </el-main>
</div>
</body>

</html>
<script>
    var indexVue = new Vue({
        el: '#index',
        data: {
            conditionList:[
                {action:'and', field: 'title', el: 'eq', model: 'title', name: '标题', type: 'input'},
                {action:'and', field: 'url', el: 'eq', model: 'url', name: '请求地址', type: 'input'},
                {action:'and', field: 'user', el: 'eq', model: 'user', name: '操作人员', type: 'input'},
                {action:'and', field: 'status', el: 'eq', model: 'status', name: '请求状态',  key: 'value', title: 'value', type: 'select', multiple: false},
                {action:'and', field: 'create_date', el: 'gt', model: 'createDate', name: '请求时间', type: 'date'},
                {action:'and', field: 'update_date', el: 'gt', model: 'updateDate', name: '修改时间', type: 'date'},
            ],
            conditions:[],
            dataList: [],
            //系统日志列表
            selectionList: [],
            //系统日志列表选中
            total: 0,
            //总记录数量
            pageSize: 10,
            //页面数量
            currentPage: 1,
            //初始页
            mananger: ms.manager,
            loading: true,
            //加载状态
            emptyText: '',
            //提示文字
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
            //搜索表单
            form: {
                sqlWhere: null,
                // 标题
                title: null,
                // 请求状态
                status: null,
                //请求地址
                url: null,
                // 业务类型
                businessType: null,
                // 操作人员
                user: null
            }
        },
        methods: {
            //查询列表
            list: function () {
                var that = this;
                var page = {
                    pageNo: that.currentPage,
                    pageSize: that.pageSize
                };
                var form = JSON.parse(JSON.stringify(that.form));

                for (key in form) {
                    if (!form[key]) {
                        delete form[key];
                    }
                }

                history.replaceState({
                    form: form,
                    page: page,
                    total: that.total
                }, "");
                setTimeout(function () {
                    ms.http.post(ms.manager + "/basic/systemLog/list.do", form.sqlWhere?Object.assign({},{sqlWhere:form.sqlWhere}, page)
                        :Object.assign({},form, page)).then(function (data) {
                        if (data.data.total <= 0) {
                            that.loading = false;
                            that.emptyText = '暂无数据';
                            that.dataList = [];
                        } else {
                            that.emptyText = '';
                            that.loading = false;
                            that.total = data.data.total;
                            that.dataList = data.data.rows;
                        }
                    }).catch(function (err) {
                        console.log(err);
                    });
                }, 1000);
            },
            //系统日志列表选中
            handleSelectionChange: function (val) {
                this.selectionList = val;
            },
            //新增
            save: function (id) {
                if (id) {
                    location.href = this.mananger + "/basic/systemLog/form.do?id=" + id;
                } else {
                    location.href = this.mananger + "/basic/systemLog/form.do";
                }
            },
            //表格数据转换
            statusFormat: function (row, column, cellValue, index) {
                var value = "";

                if (cellValue) {
                    var data = this.statusOptions.find(function (value) {
                        return value.value == cellValue;
                    });

                    if (data && data.label) {
                        value = data.label;
                    }
                }

                return value;
            },
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
                this.currentPage = 1;
                this.loading = true;
                this.form.sqlWhere = null;
                this.$refs.searchForm.resetFields();
                this.list();
            },
            search:function(data){
                this.form.sqlWhere = JSON.stringify(data);
                this.list();
            },
        },
        created: function () {
            if (history.hasOwnProperty("state")&&history.state) {
                this.form = history.state.form;
                this.total = history.state.total;
                this.currentPage = history.state.page.pageNo;
                this.pageSize = history.state.page.pageSize;
            }

            this.list();
        },
    });
</script>
<style>
    #index .ms-search {
        padding: 20px 0 0;
    }
</style>
