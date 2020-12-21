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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.constant.e.CookieConstEnum;
import net.mingsoft.basic.entity.AppEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @ClassName: TemplateAction
 * @Description: TODO(模板控制层)
 * @author 铭飞开源团队
 * @date 2020年7月2日
 *
 */
@Api("获取有关模版文件夹或模版文件信息")
@Controller("/basicTemplate")
@RequestMapping("/${ms.manager.path}/template")
public class TemplateAction extends BaseAction {

	/**
	 * 站点业务层
	 */
	@Autowired
	private IAppBiz appBiz;

	@Value("${ms.upload.template}")
	private String uploadTemplatePath;

	@Value("${ms.upload.denied}")
	private String uploadFileDenied;

	private static String TEMPLATE = "templets";


	/**
	 * 查询模版风格供站点选择
	 *
	 * @param request
	 *            请求
	 * @return 模版文件集合
	 */
	@ApiOperation(value = "查询模版风格供站点选择")
	@GetMapping("/queryAppTemplateSkin")
	@ResponseBody
	public ResultData queryAppTemplateSkin(HttpServletRequest request) {
		List<String> folderNameList = this.queryTemplateFile();
		Map map = new HashMap();
		if (folderNameList != null) {
			map.put("fileName", folderNameList);
		}
		return ResultData.build().success(map);
	}

	/**
	 * 返回主界面index
	 */
	@GetMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return "/basic/template/index";
	}

	/**
	 * 返回模板list
	 */
	@GetMapping("/list")
	public String list(HttpServletResponse response,HttpServletRequest request){
		return "/basic/template/list";
	}

	/**
	 * 返回模板编辑页面
	 */
	@GetMapping("/edit")
	public String edit(HttpServletResponse response,HttpServletRequest request){
		return "/basic/template/edit";
	}

	/**
	 * 查询模版文件供栏目选择
	 *
	 * @param request
	 *            请求
	 * @return 返回列表集合
	 */
	@ApiOperation(value = "查询模版文件供栏目选择")
	@GetMapping("/queryTemplateFileForColumn")
	@ResponseBody
	public ResultData queryTemplateFileForColumn(HttpServletRequest request) {
		ManagerSessionEntity managerSession = getManagerBySession();
		String path = BasicUtil.getRealTemplatePath( TEMPLATE + File.separator
				+ managerSession.getBasicId() + File.separator);
		int websiteId = managerSession.getBasicId();
		AppEntity website = (AppEntity) appBiz.getEntity(websiteId);
		//如果没有选择风格，则返回空
		if(StringUtils.isBlank(website.getAppStyle())){
			return ResultData.build().success(new ArrayList<>());
		}
		path = path + File.separator + website.getAppStyle();
		LOG.debug("tempPath:" + path);
		List<String> listName = new ArrayList<String>();
		files(listName, new File(path), website.getAppStyle());
		return ResultData.build().success(listName);
	}

	private void files(List list, File fileDir, String style) {
		if (fileDir.isDirectory()) {
			File files[] = fileDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File currFile = files[i];
				if (currFile.isFile()) {
					String ex = currFile.getName();
					if (ex.endsWith("htm") || ex.endsWith("html")) {
						String _pathName = new String();
						_pathName = files(currFile, style, _pathName);
						list.add(_pathName + currFile.getName());
					}
				} else if (currFile.isDirectory()) {
					files(list, currFile, style);
				}
			}
		}
	}

	private String files(File file, String style, String pathName) {
		if (!file.getParentFile().getName().equals(style)) {
			pathName = file.getParentFile().getName() + "/" + pathName;
			pathName = files(file.getParentFile(), style, pathName);
		}
		return pathName;
	}

	/**
	 * 点击模版管理，获取所有的模版文件名
	 *
	 * @param response
	 *            响应
	 * @param request
	 *            请求
	 * @return 返回模版文件名集合
	 */
	@ApiOperation(value = "点击模版管理，获取所有的模版文件名")
	@ApiImplicitParam(name = "pageNo", value = "pageNo", required = true,paramType="query")
	@GetMapping("/queryTemplateSkin")
	@ResponseBody
	protected ResultData queryTemplateSkin(HttpServletResponse response, HttpServletRequest request) {
		String pageNo = request.getParameter("pageNo");
		if (!NumberUtils.isNumber(pageNo)) {
			pageNo = "1";
		}
		ManagerSessionEntity managerSession = getManagerBySession();
		List<String> folderNameList = this.queryTemplateFile();
		Map<String,Object> map = new HashMap<>(3);
		map.put("folderNameList", folderNameList);
		map.put("websiteId", managerSession.getBasicId());
		int recordCount = 0;
		if (folderNameList!=null) {
			recordCount = folderNameList.size();
		}
		map.put("pageNo",pageNo);
		BasicUtil.setCookie( response, CookieConstEnum.PAGENO_COOKIE, pageNo);
		return ResultData.build().success(map);
	}
	/**
	 * 解压zip模版文件
	 *
	 *            文件路径
	 * @throws ZipException
	 * @throws IOException
	 */
	@ApiOperation(value ="解压zip模版文件")
	@ApiImplicitParam(name = "fileUrl", value = "文件路径", required = true,paramType="query")
	@LogAnn(title = "解压zip模版文件",businessType= BusinessTypeEnum.OTHER)
	@GetMapping("/unZip")
	@ResponseBody
	@RequiresPermissions("template:upload")
	public ResultData unZip(@ApiIgnore ModelMap model, HttpServletRequest request) throws  IOException {
		String entryName = "";
		String fileUrl = request.getParameter("fileUrl");
		//校验路径
		if (fileUrl!=null&&(fileUrl.contains("../")||fileUrl.contains("..\\"))){
			ResultData.build().error();
		}

		// 创建文件对象
		File file = new File(BasicUtil.getRealTemplatePath(fileUrl));
		// 创建zip文件对象
		unZip(file, new File(BasicUtil.getRealTemplatePath(fileUrl.substring(0, fileUrl.length() - file.getName().length()))), Charset.forName("GBK"));

		FileUtils.forceDelete(file);
		return ResultData.build().success(entryName);
	}

	/**
	 * 删除模版
	 *
	 *            模版名称
	 * @param request
	 *            响应
	 */
	@ApiOperation(value = "删除模版")
	@ApiImplicitParam(name = "fileName", value = "模版名称", required = true,paramType="query")
	@LogAnn(title = "删除模版",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("template:del")
	public ResultData delete(HttpServletRequest request) {
		String fileName = request.getParameter("fileName");
		String path = BasicUtil.getRealTemplatePath( TEMPLATE + File.separator
				+ BasicUtil.getAppId() + File.separator + fileName);
		try {
			FileUtils.deleteQuietly(new File(path));
			return ResultData.build().success();
		} catch (Exception e) {
			return ResultData.build().error();
		}
	}

	/**
	 * 获取模版路径
	 *
	 * @param request
	 *            请求
	 * @param fileNameUrl
	 *            文件路径
	 * @return 返回路径
	 */
	private String getTemplateUrl(HttpServletRequest request, String fileNameUrl) {
		ManagerSessionEntity managerSession = getManagerBySession();
		String templetsUrl = BasicUtil.getRealTemplatePath( TEMPLATE + File.separator
				+ managerSession.getBasicId() + File.separator + fileNameUrl);
		return templetsUrl;
	}

	/**
	 * 显示子文件和子文件夹
	 *
	 * @param response
	 *            响应
	 * @param request
	 *            请求
	 * @return 返回文件名集合
	 */
	@ApiOperation(value = "显示子文件和子文件夹")
	@ApiImplicitParam(name = "skinFolderName", value = "skinFolderName", required = true,paramType="query")
	@GetMapping("/showChildFileAndFolder")
	@ResponseBody
	public ResultData showChildFileAndFolder(HttpServletResponse response, HttpServletRequest request) {
		ManagerSessionEntity managerSession = getManagerBySession();
		List<String> folderNameList = null;
		String skinFolderName = request.getParameter("skinFolderName");

		//非法路径过滤
		if(skinFolderName!=null&&(skinFolderName.contains("../")||skinFolderName.contains("..\\"))){
			return ResultData.build().error("非法路径");
		}

		skinFolderName=skinFolderName.substring(skinFolderName.lastIndexOf("templets"));
		File files[] = new File(BasicUtil.getRealTemplatePath( skinFolderName)).listFiles();
		Map<String,Object> map = new HashMap<>();
		if (files != null) {
			folderNameList = new ArrayList<String>();
			List<String> fileNameList = new ArrayList<String>();
			for (int i = 0; i < files.length; i++) {
				File currFile = files[i];
				String filter = BasicUtil.getRealTemplatePath(
						TEMPLATE + File.separator + BasicUtil.getAppId());
				LOG.debug("过滤路径" + filter);
				String temp = currFile.getPath();
				//以当前系统分隔符作判断，将不是当前系统的分隔符替换为当前系统的

				temp=temp.replace(File.separator.equals("\\")?"/":"\\", File.separator).replace(filter, "");
				if (currFile.isDirectory()) {
					folderNameList.add(temp);
				} else {
					fileNameList.add(temp);
				}
			}
			folderNameList.addAll(fileNameList);
			map.put("fileNameList", folderNameList);
		}
		String uploadFileUrl = skinFolderName;
		map.put("uploadFileUrl", uploadFileUrl);
		map.put("websiteId", managerSession.getBasicId());
		return ResultData.build().success(map);
	}

	/**
	 * 读取模版文件内容
	 *
	 * @param model
	 * @param request
	 *            请求
	 * @return 返回文件内容
	 */
	@ApiOperation(value = "读取模版文件内容")
	@ApiImplicitParam(name = "fileName", value = "文件名称", required = true,paramType="query")
	@GetMapping("/readFileContent")
	@ResponseBody
	@RequiresPermissions("template:update")
	public ResultData readFileContent(@ApiIgnore ModelMap model, HttpServletRequest request) {
		String fileName = request.getParameter("fileName");
		fileName=fileName.substring(fileName.lastIndexOf("templets"));

		//非法路径过滤
		if(fileName!=null&&(fileName.contains("../")||fileName.contains("..\\"))){
			return ResultData.build().error("非法路径");
		}

		Map<String,Object> map = new HashMap<>();
		if (!StringUtils.isEmpty(fileName)) {
			map.put("fileContent", FileReader.create(new File(BasicUtil.getRealTemplatePath( fileName))).readString());
		}

		map.put("name", new File(BasicUtil.getRealTemplatePath(fileName)).getName());
		map.put("fileName", fileName);
		map.put("fileNamePrefix", fileName.substring(0, fileName.lastIndexOf(File.separator) + 1));
		return ResultData.build().success(map);
	}

	/**
	 * 删除模版文件
	 *
	 *            文件名称
	 * @param request
	 *            请求
	 */
	@ApiOperation(value = "删除模版文件")
	@ApiImplicitParam(name = "fileName", value = "文件名称", required = true,paramType="query")
	@LogAnn(title = "删除模版文件",businessType= BusinessTypeEnum.DELETE)
	@PostMapping("/deleteTemplateFile")
	@ResponseBody
	public ResultData deleteTemplateFile(HttpServletRequest request) {
		ManagerSessionEntity managerSession = getManagerBySession();
		String fileName = request.getParameter("fileName");
		fileName=TEMPLATE + File.separator
				+ managerSession.getBasicId() + File.separator + fileName;
		fileName=fileName.substring(fileName.lastIndexOf("templets"));
		FileUtil.del(BasicUtil.getRealTemplatePath(fileName ));
		return ResultData.build().success();
	}

	/**
	 * 写入模版文件内容
	 *
	 * @param model
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @throws IOException
	 */
	@ApiOperation(value = "写入模版文件内容")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fileName", value = "文件名称", required = true,paramType="query"),
		@ApiImplicitParam(name = "oldFileName", value = "旧文件名称", required = true,paramType="query"),
		@ApiImplicitParam(name = "fileContent", value = "文件内容", required = true,paramType="query"),
	})
	@LogAnn(title = "写入模版文件内容",businessType= BusinessTypeEnum.UPDATE)
	@PostMapping("/writeFileContent")
	@ResponseBody
	public ResultData writeFileContent(@ApiIgnore ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String fileName = request.getParameter("fileName");
		//校验后缀文件名
		if (!checkFileType(fileName)){
			return ResultData.build().error();
		}

		String oldFileName = request.getParameter("oldFileName");
		String fileContent = request.getParameter("fileContent");
		if (!StringUtils.isEmpty(fileName)) {
			// 文件路径
			String templets = BasicUtil.getRealTemplatePath(fileName);
			LOG.debug(templets);
			FileWriter.create(new File(templets)).write(fileContent);
			if (!fileName.equals(oldFileName)) {
				// 得到一个待命名文件对象
				File newName = new File(templets);
				// 获取新名称文件的文件对象
				File oldName = new File(BasicUtil.getRealTemplatePath( oldFileName));
				// 进行重命名
				oldName.renameTo(newName);
				FileUtil.del(BasicUtil.getRealTemplatePath( oldFileName));
			}
			return ResultData.build().success();
		}
		return ResultData.build().error();
	}

	/**
	 * 查询模版文件集合
	 *
	 * @return 模版文件集合
	 */
	private List<String> queryTemplateFile() {
		ManagerSessionEntity managerSession = getManagerBySession();
		List<String> folderNameList = null;
		if (!isSystemManager()) {
			String templets = BasicUtil.getRealTemplatePath( TEMPLATE + File.separator
					+ managerSession.getBasicId() + File.separator);
			File file = new File(templets);
			String[] str = file.list();
			if (str!=null) {
				folderNameList = new ArrayList<String>();
				for (int i = 0; i < str.length; i++) {
					// 避免不为文件夹的文件显示
					if (str[i].indexOf(".") < 0) {
						folderNameList.add(str[i]);
					}
				}
			}
		}
		return folderNameList;
	}

	/**
	 * 解压zip文件,并过滤文件后缀名不符合要求的文件
	 * @param file  	被解压文件
	 * @param outFile	输入文件目录
	 * @param charset	字符编码集
	 * @return
	 */
	protected File unZip(File file, File outFile, Charset charset){
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file, charset);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		try {
			final Enumeration<ZipEntry> em = (Enumeration<ZipEntry>) zipFile.entries();
			ZipEntry zipEntry;
			File outItemFile;
			while (em.hasMoreElements()) {
				zipEntry = em.nextElement();
				String name = zipEntry.getName();
				// FileUtil.file会检查slip漏洞，漏洞说明见http://blog.nsfocus.net/zip-slip-2/
				outItemFile = buildFile(outFile, name);
				if (zipEntry.isDirectory()) {
					// 创建对应目录
					//noinspection ResultOfMethodCallIgnored
					outItemFile.mkdirs();
				} else {
					// 写出文件
					write(zipFile, zipEntry, outItemFile);
				}
			}
			return outFile;
		}finally {
			IoUtil.close(zipFile);
		}
	}

	/**
	 * 构建文件,文件目录
	 * @param outFile	输出文件
	 * @param fileName	文件名
	 * @return
	 */
	private File buildFile(File outFile, String fileName) {
		if (!FileUtil.isWindows()
			// 检查文件名中是否包含"/"，不考虑以"/"结尾的情况
				&& fileName.lastIndexOf(CharUtil.SLASH, fileName.length() - 2) > 0) {
			// 在Linux下多层目录创建存在问题，/会被当成文件名的一部分，此处做处理
			// 使用/拆分路径（zip中无\），级联创建父目录
			final String[] pathParts = StrUtil.splitToArray(fileName, CharUtil.SLASH);
			for (int i = 0; i < pathParts.length - 1; i++) {
				//由于路径拆分，slip不检查，在最后一步检查
				outFile = new File(outFile, pathParts[i]);
			}
			//noinspection ResultOfMethodCallIgnored
			outFile.mkdirs();
			// 最后一个部分如果非空，作为文件名
			fileName = pathParts[pathParts.length - 1];
		}
		return FileUtil.file(outFile, fileName);
	}

	private void write(ZipFile zipFile, ZipEntry zipEntry, File outItemFile) throws IORuntimeException {
		InputStream in = null;
		try {
			//校验文件后缀名
			if (!checkFileType(zipEntry.getName())){
				return;
			}
			in = zipFile.getInputStream(zipEntry);
			FileUtil.writeFromStream(in, outItemFile);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 校验文件后缀名是否符合要求
	 * @param fileName	文件名
	 * @return false 不合法 true 符合
	 */
	protected boolean checkFileType(String fileName){
		//校验后缀文件名
		String[] errorType = uploadFileDenied.split(",");
		String fileType=fileName.substring(fileName.lastIndexOf("."));
		for (String type : errorType) {
			//校验禁止上传的文件后缀名（忽略大小写）
			if((fileType).equalsIgnoreCase(type)){
				LOG.info("文件类型被拒绝:{}",fileName);
				return false;
			}
		}
		return true;
	}
}
