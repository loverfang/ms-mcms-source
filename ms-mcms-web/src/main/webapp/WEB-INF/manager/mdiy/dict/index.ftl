<!DOCTYPE html>
<html>
<head>
    <title>字典表</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
    <el-header class="ms-header" height="50px">
        <el-col :span="12">
            <@shiro.hasPermission name="mdiy:dict:save">
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">新增</el-button>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="mdiy:dict:del">
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"
                           :disabled="!selectionList.length">删除
                </el-button>
            </@shiro.hasPermission>
        </el-col>
    </el-header>
    <el-main class="ms-container" style="flex-direction: row">

        <div style="overflow: hidden; background-color: rgb(242, 246, 252);  display: flex;position: relative;margin-right: 10px;">
            <el-scrollbar>
                <el-aside width="160px" style="background-color: #f2f6fc;">

                        <el-menu
                                :default-active="defaultActive"
                                v-if="dictTypeOptions && dictTypeOptions.length">
                            <template v-for="(item,i) in dictTypeOptions">
                                <!--单个选项-->
                                <el-menu-item
                                        :index="i"
                                        @click="list(item)"
                                        v-text="item.dictType">
                                </el-menu-item>
                            </template>
                        </el-menu>
                </el-aside>
            </el-scrollbar>
        </div>

        <div style="display: flex;flex-direction: column;flex: 1;">
            <el-table v-loading="loading" ref="multipleTable" height="calc(100% - 68px)" class="ms-table-pagination"
                      border
                      :data="treeList" tooltip-effect="dark" @selection-change="handleSelectionChange">
                <template slot="empty">
                    {{emptyText}}
                </template>
                <el-table-column type="selection" :selectable="isChecked" width="40"></el-table-column>
                <el-table-column label="标签名" align="left" prop="dictLabel" show-overflow-tooltip>
                </el-table-column>
                <el-table-column label="数据值" width="150" align="left" prop="dictValue" show-overflow-tooltip>
                </el-table-column>
                <el-table-column label="排序" align="right" width="100" prop="dictSort">
                </el-table-column>
                <el-table-column label="来源" align="center" width="100" prop="del">
                    <template slot-scope="scope">
                        <template v-if="scope.row.del == 3">系统默认</template>
                        <template v-else>用户创建</template>
                    </template>
                </el-table-column>
                <@shiro.hasPermission name="mdiy:dict:update">
                    <el-table-column label="操作" fixed="right" align="center" width="180">
                        <template slot-scope="scope">
                            <@shiro.hasPermission name="mdiy:dict:update">
                                <el-link :underline="false" v-if="scope.row.del != 3" type="primary" size="medium"
                                         @click="save(scope.row.dictId)">编辑
                                </el-link>
                            </@shiro.hasPermission>
                            <@shiro.hasPermission name="mdiy:dict:del">
                                <el-link :underline="false" v-if="scope.row.del != 3" type="primary"
                                         @click="del([scope.row])">删除
                                </el-link>
                            </@shiro.hasPermission>
                        </template>
                    </el-table-column>
                </@shiro.hasPermission>
            </el-table>
            <el-pagination
                    background
                    :page-sizes="[5, 10, 20]"
                    layout="total, sizes, prev, pager, next, jumper"
                    :current-page.sync="currentPage"
                    :page-size="pageSize"
                    :total="total"
                    class="ms-pagination"
                    @current-change='currentChange'
                    @size-change="sizeChange">
            </el-pagination>
        </div>
    </el-main>
</div>
</body>

</html>
<script>
    var indexVue = new Vue({
        el: '#index',
        data: {
            dict: ms.dict,
            conditionList: [
                {action: 'and', field: 'dict_label', el: 'eq', model: 'dictLabel', name: '标签名', type: 'input'},
                {
                    action: 'and',
                    field: 'dict_type',
                    el: 'eq',
                    model: 'dictType',
                    name: '类型',
                    key: 'dictType',
                    title: 'dictType',
                    type: 'select',
                    multiple: false
                },
                {action: 'and', field: 'dict_value', el: 'eq', model: 'dictValue', name: '数据值', type: 'input'},
                {action: 'and', field: 'dict_sort', el: 'eq', model: 'dictSort', name: '排序', type: 'number'},
                {action: 'and', field: 'is_child', el: 'eq', model: 'isChild', name: '系统扩展', type: 'input'},
                {action: 'and', field: 'dict_remarks', el: 'eq', model: 'dictRemarks', name: '备注信息', type: 'textarea'},
                {action: 'and', field: 'create_date', el: 'eq', model: 'createDate', name: '创建时间', type: 'date'},
                {action: 'and', field: 'update_date', el: 'eq', model: 'updateDate', name: '修改时间', type: 'date'},
            ],
            conditions: [],
            treeList: [],
            //字典表列表
            selectionList: [],
            //字典表列表选中
            total: 0,
            //总记录数量
            pageSize: 10,
            //页面数量
            currentPage: 1,
            //初始页
            mananger: ms.manager,
            loading: true,
            emptyText: '',
            dictTypeOptions: [],
            selectDictOption: {},
            defaultActive: 0,
            //搜索表单
            form: {
                sqlWhere: null,
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
                dictRemarks: '',
                // 描述
                dictDescription: ''
            }
        },
        methods: {
            //查询列表
            list: function (dictType) {
                var that = this;
                if (dictType) {
                    that.form.dictType = dictType.dictType;
                    that.selectFiDicttypeOption = dictType;
                }
                that.loading = true;
                that.loadState = false;
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
                    ms.http.post(ms.manager + "/mdiy/dict/list.do", form.sqlWhere ? Object.assign({}, {sqlWhere: form.sqlWhere}, page)
                        : Object.assign({}, form, page)).then(
                        function (data) {
                            if (data.data.total <= 0) {
                                that.loading = false;
                                that.emptyText = '暂无数据';
                                that.treeList = [];
                            } else {
                                that.emptyText = '';
                                that.loading = false;
                                that.total = data.data.total;
                                that.treeList = data.data.rows;
                            }
                        }).catch(function (err) {
                        console.log(err);
                    });
                }, 500);
            },
            //字典表列表选中
            handleSelectionChange: function (val) {
                this.selectionList = val;
            },
            //删除
            del: function (row) {
                var that = this;
                that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(ms.manager + "/mdiy/dict/delete.do", row.length ? row : [row], {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(function (data) {
                        if (data.result) {
                            that.$notify({
                                type: 'success',
                                message: '删除成功!'
                            }); //删除成功，刷新列表

                            that.list();
                        } else {
                            that.$notify({
                                title: '失败',
                                message: data.msg,
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
            save: function (dictId) {
                var that = this;
                if (dictId) {
                    location.href = this.mananger + "/mdiy/dict/form.do?dictId=" + dictId;
                } else {
                    location.href = this.mananger + "/mdiy/dict/form.do?dictType=" + that.selectFiDicttypeOption.dictType;
                }
            },
            isChecked: function (row) {
                if (row.del == 3) {
                    return false;
                }

                return true;
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
            search: function (data) {
                this.form.sqlWhere = JSON.stringify(data);
                this.list();
            },
            getDictType: function (isChild) {
                var that = this;
                that.form.dictType = '';
                ms.http.get(ms.manager + "/mdiy/dict/dictType.do?pageSize=999", isChild ? {
                    "isChild": isChild
                } : null).then(function (data) {
                    if (data.data.rows) {
                        that.dictTypeOptions = data.data.rows;
                        if (that.dictTypeOptions.length > 0) {
                            that.selectDictOption = that.dictTypeOptions[0];
                        }
                        that.list(that.selectDictOption);
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //重置表单
            rest: function () {
                this.currentPage = 1;
                this.loading = true;
                this.form.sqlWhere = null;
                this.$refs.searchForm.resetFields();
                this.list();
            }
        },
        created: function () {
            if (history.hasOwnProperty("state") && history.state) {
                this.form = history.state.form;
                this.total = history.state.total;
                this.currentPage = history.state.page.pageNo;
                this.pageSize = history.state.page.pageSize;
            }
            this.getDictType();
        }
    });
</script>
<style>
</style>
