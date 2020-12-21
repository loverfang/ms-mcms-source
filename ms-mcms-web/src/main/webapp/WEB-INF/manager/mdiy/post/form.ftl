<!DOCTYPE html>
<html>
<head>
	<title>自定义表单</title>
	<#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
	<el-header class="ms-header" height="50px">
		<el-col :span="12">
			<@shiro.hasPermission name="mdiy:model:del">
				<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>
			</@shiro.hasPermission>
		</el-col>
		<el-col span="12" class="ms-tr">
			<el-button size="mini"   plain onclick="javascript:history.go(-1)"><i class="iconfont icon-fanhui"></i>返回</el-button>
		</el-col>
	</el-header>
	<el-main class="ms-container">
		<el-table v-loading="loading" ref="multipleTable" height="calc(100vh-68px)" class="ms-table-pagination" border :data="treeList" tooltip-effect="dark" @selection-change="handleSelectionChange">
			<template slot="empty">
				{{emptyText}}
			</template>
			<el-table-column type="selection" width="40"></el-table-column>
			<el-table-column v-for="(field,index) in tableField" :label="field.name" :prop="field.key" v-if="field.type != 'imgupload'">
			</el-table-column>
			<el-table-column v-for="(field,index) in tableField" :label="field.name" :prop="field.key" v-if="field.type == 'imgupload'">
			<template slot-scope="scope">
				<div class="block" v-for="src in scope.row[field.key]">
					<el-image
							style="width: 50px; height: 50px;float: left;margin-right: 5px; "
							:src="ms.base+src"
							:preview-src-list="[ms.base+src]">
					</el-image>
				</div>
			</template>
			</el-table-column>
			<#--				<@shiro.hasPermission name="mdiy:model:update">-->
			<el-table-column label="操作" align="center" width="180">
				<template slot-scope="scope">
					<el-link type="primary" :underline="false"  @click="del(scope.row)">删除</el-link>
				</template>
			</el-table-column>
			<#--				</@shiro.hasPermission>-->
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
			formUrl: '',
			modelId: '',
			tableField: [],
			treeList: [],
			//自定义模型列表
			selectionList: [],
			//自定义模型列表选中
			total: 0,
			//总记录数量
			pageSize: 10,
			//页面数量
			currentPage: 1,
			//初始页
			mananger: ms.manager,
			modelTypeOptions: [],
			dialogViewVisible: false,
			dialogImportVisible: false,
			loading: true,
			emptyText: '',
			form: {}
		},
		watch: {
			'dialogImportVisible': function (n, o) {
				if (!n) {
					this.$refs.form.resetFields();
					this.form.id = 0;
				}
			}
		},
		methods: {
			//获取表格字段
			getTableField: function (url) {
				var that = this;
				ms.http.get(ms.base + "/mdiy/post/" + url + "/field.do").then(function (data) {
					if (data.length > 0) {
						that.tableField = data;
					}
				}).catch(function (err) {
					console.log(err);
				});
			},
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
					ms.http.get(ms.base + "/mdiy/post/" + that.formUrl + "/queryData.do", Object.assign({}, that.form, page)).then(function (data) {
						if (data.total <= 0) {
							that.loading = false;
							that.emptyText = '暂无数据';
							that.treeList = [];
						} else {
							data.rows.forEach(function (item) {
								Object.keys(item).forEach(function (field) {
									try {
										if (item[field] != "" && JSON.parse(item[field]).length > 0) {
											var picture = [];
											JSON.parse(item[field]).forEach(function (img) {
												picture.push(img);
											});
											item[field] = picture;
										}
									} catch (e) {}
								});
							});
							that.emptyText = '';
							that.loading = false;
							that.total = data.total;
							that.treeList = data.rows;
						}
					}).catch(function (err) {
						console.log(err);
					});
				}, 500);
			},
			//自定义模型列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			//删除
			del: function (row) {
				var that = this;
				var ids = [];
				if (row.length > 0) {
					row.forEach(function (item, index) {
						ids.push(item.id);
					});
				} else {
					ids.push(row.id);
				}
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(ms.manager + "/mdiy/post/data/delete.do", {
						modelId: that.modelId,
						ids: ids.join(',')
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
				this.$refs.searchForm.resetFields();
				this.list();
			}
		},
		created: function () {
			this.formUrl = ms.util.getParameter("formUrl");
			this.modelId = ms.util.getParameter("modelId");
			this.getTableField(this.formUrl);

			if (history.hasOwnProperty("state")&&history.state) {
				this.form = history.state.form;
				this.total = history.state.total;
				this.currentPage = history.state.page.pageNo;
				this.pageSize = history.state.page.pageSize;
			}

			this.list();
		}
	});
</script>
<style>
	#index .iconfont{
		font-size: 12px;
		margin-right: 5px;
	}
</style>
