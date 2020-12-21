<!DOCTYPE html>
<html>
<head>
	<title>模板管理</title>
		<#include "../../include/head-file.ftl">
	<script src="${base}/static/plugins/codemirror/5.48.4/codemirror.js"></script>
	<link href="${base}/static/plugins/codemirror/5.48.4/codemirror.css" rel="stylesheet">
	<script src="${base}/static/plugins/codemirror/5.48.4/mode/css/css.js"></script>
	<script src="${base}/static/plugins/vue-codemirror/vue-codemirror.js"></script>
	<script src="${base}/static/plugins/codemirror/5.48.4/addon/scroll/annotatescrollbar.js"></script>
	<script src="${base}/static/plugins/codemirror/5.48.4/mode/xml/xml.js"></script>
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
							accept=".htm,.ftl,.html,.jpg,.gif,.png,.css,.js,.ico,.swf,.less"
							:disabled="false"
							:on-success="fileUploadSuccess"
							:data="{uploadPath:templateData.uploadPath,uploadFloderPath:true,'rename':false}">
						<el-tooltip effect="dark" content="建议上传5M以下htm/html/css/js/jpg/gif/png/swf文件" placement="left-end">
						<el-button size="small" type="primary">点击上传</el-button>
						</el-tooltip>
					</el-upload>
				</el-col>
				<el-col span=22>
					<el-button size="mini" plain onclick="" style="float: right" @click="back"><i class="iconfont icon-fanhui"></i>返回</el-button>
				</el-col>
			</el-header>
		<el-main class="ms-container">
			<el-table height="calc(100vh - 68px)" v-loading="loading" ref="multipleTable" border :data="templateData.fileNameList" tooltip-effect="dark">
				<template slot="empty">
					{{emptyText}}
				</template>
                 <el-table-column label="模板名称" align="left" prop="name">
					 <template slot-scope="scope">
						 <div style="margin-left: 5px;display: flex;align-items: center;justify-content: flex-start;">
							 <svg class="icon" aria-hidden="true">
								 <use :xlink:href="scope.row.icon"></use>
							 </svg>
							 <span style="margin-left: 5px">{{scope.row.name}}</span>
						 </div>

					 </template>
                 </el-table-column>
                 <el-table-column label="类型" width="100" align="center" prop="folderType">
                 </el-table-column>
				<el-table-column label="操作" width="180" align="center">
					<template slot-scope="scope">
						<el-link type="primary" :underline="false" v-if="scope.row.folderType == '文件夹'" @click="view(scope.row.folderName,scope.row.folderType)">查看</el-link>
						<el-link type="primary" :underline="false" v-if="scope.row.folderType == '文件'" @click="view(scope.row.folderName,scope.row.folderType)">编辑</el-link>
						<@shiro.hasPermission name="template:del">
						<el-link type="primary" :underline="false" v-if="scope.row.folderType != '文件夹'" @click="del(scope.row.folderName)">删除</el-link>
						</@shiro.hasPermission>
					</template>
				</el-table-column>
			</el-table>
         </el-main>
	</div>
</body>
<#include "/basic/template/edit.ftl">
</html>
<script>
	Vue.use(VueCodemirror);
	var indexVue = new Vue({
		el: '#index',
		data: {
			//数据stak
			dataStack: [],
			manager: ms.manager,
			loading: true,
			//加载状态
			emptyText: '',
			//提示文字
			fileList: [],
			templateData: {
				fileNameList: [],
				uploadPath: null,
				path: null,
				websiteId: null
			}
		},
		methods: {
			//查询列表
			list: function (skinFolderName) {
				var that = this;
				that.loading = true;
				setTimeout(function () {
					console.log(skinFolderName)
					ms.http.get(ms.manager + "/template/showChildFileAndFolder.do", {
						skinFolderName: skinFolderName
					}).then(function (res) {
						var data = {
							fileNameList: [],
							uploadFileUrl: null
						};
						res.data.fileNameList.forEach(function (item) {
							var type = "文件夹";
							var icon = "#icon-wenjianjia2";

							if (item.indexOf(".") >= 0) {
								type = "文件";
								icon = "#icon-wenjian2";
							}

							var suffix = item.substring(item.lastIndexOf(".") + 1);

							if (suffix == "jpg" || suffix == "gif" || suffix == "png") {
								type = "图片";
								icon = "#icon-tupian1";
							}

							data.fileNameList.push({
								name: item.substring(item.lastIndexOf("\\") + 1),
								icon: icon,
								folderName: item,
								folderType: type
							});
						}); //保存当前路径以及相关信息

						data.path = skinFolderName;
						data.uploadPath = res.data.uploadFileUrl;
						data.websiteId = res.data.websiteId;
						that.templateData = data;
						that.dataStack.push(data);
					}).catch(function (err) {
						console.log(err);
					}).finally(function () {
						that.loading = false;
					});
				}, 500);
			},
			//删除
			del: function (name) {
				var that = this;
				that.$confirm('确定删除该文件吗?', '删除文件', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(ms.manager + "/template/deleteTemplateFile.do", {
						fileName: name
					}).then(function (res) {
						if (res.result) {
							that.$notify({
								type: 'success',
								message: '删除成功!'
							}); //删除成功，刷新列表,弹出并重新加载

							that.list(that.dataStack.pop().path);
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
			view: function (name, type) {
				var path = "templets/" + this.templateData.websiteId + "/" + name;

				if (type == "文件夹") {
					this.list(path);
				} else if (type == "文件") {
					form.open(path);
				}
			},
			back: function () {
				this.dataStack.pop();

				if (this.dataStack.length == 0) {
					window.location.href = ms.manager + "/template/index.do";
				} else {
					this.templateData = this.dataStack[this.dataStack.length - 1];
				}
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
			search: function (data) {
				this.form.sqlWhere = JSON.stringify(data);
				this.list();
			},
			//重置表单
			rest: function () {
				this.form.sqlWhere = null;
				this.$refs.searchForm.resetFields();
				this.list();
			},
			//fileUpload文件上传完成回调
			fileUploadSuccess: function (response, file, fileList) {
				this.list(this.dataStack.pop().path);
				this.fileList.push({
					url: file.url,
					name: file.name,
					path: response,
					uid: file.uid
				});
			}
		},
		mounted: function () {
			this.template = ms.util.getParameter("template");

			if (this.template) {
				this.list(this.template);
			}
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
