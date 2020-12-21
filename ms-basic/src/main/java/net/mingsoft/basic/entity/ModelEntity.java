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

package net.mingsoft.basic.entity;

import net.mingsoft.base.constant.e.BaseEnum;
import net.mingsoft.base.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * 模块实体
 * @author ms dev group
 * @version 
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public class ModelEntity extends BaseEntity {

    /**
     * 模块的编号
     */
    private int modelId;

    /**
     * 模块的标题
     */
    private String modelTitle;

    /**
     * 发布时间
     */
    private Timestamp modelDatetime;

    /**
     * 模块父id
     */
    private Integer modelModelId;

    /**
     * 链接地址
     */
    private String modelUrl;

    /**
     * 模块编码
     */
    private String modelCode;
    /**
	 * 菜单类型
	 */
    private String isChild;

    /**
     * 模块图标
     */
    private String modelIcon = null;
    
    /**
     *模块管理员Id
     */
    private int modelManagerId;
    /**
     * 模块排序
     * @return
     */
    private int modelSort;
    /**
     * 子功能集合，不参加表结构
     */
    private List<ModelEntity> modelChildList;
    /**
     * 是否是菜单
     */
    private Integer modelIsMenu;
    /**
     * 选中状态，不参加表结构
     */
    private int chick;
    /**
     * 子集数量，不参加表结构
     */
    private int childNum;
    /**
     * 树的深度，不参加表结构
     */
    private int depth;
    /**
     * 父级编号集合
     * @return
     */
    private String modelParentIds;
    
    /**
     * 
     * 获取子集数量
     * @return
     */
    public int getChildNum() {
		return childNum;
	}

    /**
     * 
     * @param childNum
     */
	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}

	/**
	 * 
	 * 获取层级
	 * @return
	 */
	public int getDepth() {
		if(StringUtils.isNotEmpty(modelParentIds)){
			return depth = modelParentIds.split(",").length;
		}else {
			return depth;
		}
		
	}

	/**
	 * 
	 * 设置层级
	 * @param depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Integer getModelIsMenu() {
		return modelIsMenu;
	}
    
	public void setModelIsMenu(Integer modelIsMenu) {
		this.modelIsMenu = modelIsMenu;
	}
	
    
    /**
     * 获取modelCode
     * @return modelCode
     */
    public String getModelCode() {
        return modelCode;
    }

    /**
     * 设置modelCode
     * @param modelCode
     */
    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    /**
     * 获取modelIcon
     * @return modelIcon
     */
    public String getModelIcon() {
        return modelIcon;
    }

    /**
     * 设置modelIcon
     * @param modelIcon
     */
    public void setModelIcon(String modelIcon) {
        this.modelIcon = modelIcon;
    }

    /**
     * 获取modelModelId
     * @return modelModelId
     */
    public Integer getModelModelId() {
        return modelModelId;
    }

    /**
     * 设置modelModelId
     * @param modelModelId
     */
    public void setModelModelId(Integer modelModelId) {
        this.modelModelId = modelModelId;
    }

    /**
     * 获取modelUrl
     * @return modelUrl
     */
    public String getModelUrl() {
        return modelUrl;
    }

    /**
     * 设置modelUrl
     * @param modelUrl
     */
    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    /**
     * 获取modelId
     * @return modelId
     */
    public int getModelId() {
        return modelId;
    }

    /**
     * 设置modelId
     * @param modelId
     */
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取modelDatetime
     * @return modelDatetime
     */
    public Timestamp getModelDatetime() {
        return modelDatetime;
    }

    /**
     * 设置modelDatetime
     * @param modelDatetime
     */
    public void setModelDatetime(Timestamp modelDatetime) {
        this.modelDatetime = modelDatetime;
    }

    /**
     * 获取modelTitle
     * @return modelTitle
     */
    public String getModelTitle() {
        return modelTitle;
    }

    /**
     * 设置modelTitle
     * @param modelTitle 
     */
    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }
    
    /**
     * 获取管理员id 
     * @return 返回管理员ID
     */
	public int getModelManagerId() {
		return modelManagerId;
	}
	
	/**
	 * 设置管理员id
	 * @param modelManagerId 管理员ID
	 */
	public void setModelManagerId(int modelManagerId) {
		this.modelManagerId = modelManagerId;
	}

	public int getModelSort() {
		return modelSort;
	}

	public void setModelSort(int modelSort) {
		this.modelSort = modelSort;
	}

	public List<ModelEntity> getModelChildList() {
		return modelChildList;
	}

	public void setModelChildList(List<ModelEntity> modelChildList) {
		this.modelChildList = modelChildList;
	}

	public int getChick() {
		return chick;
	}

	public void setChick(int chick) {
		this.chick = chick;
	}
	
	
	public String getModelParentIds() {
		return modelParentIds;
	}

	public void setModelParentIds(String modelParentIds) {
		this.modelParentIds = modelParentIds;
	}

	/**
	 * 获取菜单类型
	 */
	public String getIsChild() {
		return isChild;
	}
	/**
	 * 设置菜单类型
	 */
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}


	public enum IsMenu implements BaseEnum{
		NO(0),
		YES(1);
		private int id;
		IsMenu(int id){
			this.id = id;
		}
		@Override
		public int toInt() {
			// TODO Auto-generated method stub
			return this.id;
		}
	}
	
}
