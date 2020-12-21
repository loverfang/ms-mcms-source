package net.mingsoft.people.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

 /**
 * 用户基础信息表实体
 * @author 伍晶晶
 * @version 
 * 版本号：0.0<br/>
 * 创建日期：2017-8-23 10:10:22<br/>
 * 历史修订：<br/>
 */
public class PeopleUserEntity extends PeopleEntity {

	private static final long serialVersionUID = 1503454222227L;
	
	/**
	 * 用户ID关联people表的（people_id）
	 */
	private Integer puPeopleId; 
	/**
	 * 用户真实名称
	 */
	private String puRealName; 
	/**
	 * 用户地址
	 */
	private String puAddress;

	 /**
	  * 用户等级
	  */
	private String puLevel;

	 /**
	  * 等级名称
	  */
	private String puLevelName;
	/**
	 * 用户头像图标地址
	 */
	private String puIcon; 
	/**
	 * 用户昵称
	 */
	private String puNickname; 
	/**
	 * 用户性别(0.未知、1.男、2.女)
	 */
	private Integer puSex;
	/**
	 * 用户出生年月日
	 */
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	private Date puBirthday;
	/**
	 * 身份证
	 */
	private String puCard;

     /**
      * 城市选择
      */
     private Long provinceId;
     /**
      * 省
      */
     private String provinceName;
     /**
      * 城市
      */
     private String cityName;
     /**
      * 区
      */
     private String countyName;
     /**
      * 城市id
      */
     private Long cityId;
     /**
      * 区id
      */
     private Long countyId;
	
	
		
	/**
	 * 设置用户ID关联people表的（people_id）
	 */
	public void setPuPeopleId(Integer puPeopleId) {
		this.puPeopleId = puPeopleId;
	}

	/**
	 * 获取用户ID关联people表的（people_id）
	 */
	public Integer getPuPeopleId() {
		return this.puPeopleId;
	}
	
	/**
	 * 设置用户真实名称
	 */
	public void setPuRealName(String puRealName) {
		this.puRealName = puRealName;
	}

	/**
	 * 获取用户真实名称
	 */
	public String getPuRealName() {
		return this.puRealName;
	}
	
	/**
	 * 设置用户地址
	 */
	public void setPuAddress(String puAddress) {
		this.puAddress = puAddress;
	}

	/**
	 * 获取用户地址
	 */
	public String getPuAddress() {
		return this.puAddress;
	}

	 public String getPuLevel() {
		 return puLevel;
	 }

	 public void setPuLevel(String puLevel) {
		 this.puLevel = puLevel;
	 }

	 public String getPuLevelName() {
		 return puLevelName;
	 }

	 public void setPuLevelName(String puLevelName) {
		 this.puLevelName = puLevelName;
	 }

	 /**
	 * 设置用户头像图标地址
	 */
	public void setPuIcon(String puIcon) {
		this.puIcon = puIcon;
	}

	/**
	 * 获取用户头像图标地址
	 */
	public String getPuIcon() {
		return this.puIcon;
	}
	
	/**
	 * 设置用户昵称
	 */
	public void setPuNickname(String puNickname) {
		this.puNickname = puNickname;
	}

	/**
	 * 获取用户昵称
	 */
	public String getPuNickname() {
		return this.puNickname;
	}
	
	/**
	 * 设置用户性别(0.未知、1.男、2.女)
	 */
	public void setPuSex(Integer puSex) {
		this.puSex = puSex;
	}

	/**
	 * 获取用户性别(0.未知、1.男、2.女)
	 */
	public Integer getPuSex() {
		return this.puSex;
	}
	
	/**
	 * 设置用户出生年月日
	 */
	public void setPuBirthday(Date puBirthday) {
		this.puBirthday = puBirthday;
	}

	/**
	 * 获取用户出生年月日
	 */
	public Date getPuBirthday() {
		return this.puBirthday;
	}
	
	/**
	 * 设置身份证
	 */
	public void setPuCard(String puCard) {
		this.puCard = puCard;
	}

	/**
	 * 获取身份证
	 */
	public String getPuCard() {
		return this.puCard;
	}
     /**
      * 设置城市选择
      */
     public void setProvinceId(Long provinceId) {
         this.provinceId = provinceId;
     }

     /**
      * 获取城市选择
      */
     public Long getProvinceId() {
         return this.provinceId;
     }
     /**
      * 设置省
      */
     public void setProvinceName(String provinceName) {
         this.provinceName = provinceName;
     }

     /**
      * 获取省
      */
     public String getProvinceName() {
         return this.provinceName;
     }
     /**
      * 设置城市
      */
     public void setCityName(String cityName) {
         this.cityName = cityName;
     }

     /**
      * 获取城市
      */
     public String getCityName() {
         return this.cityName;
     }
     /**
      * 设置区
      */
     public void setCountyName(String countyName) {
         this.countyName = countyName;
     }

     /**
      * 获取区
      */
     public String getCountyName() {
         return this.countyName;
     }
     /**
      * 设置城市id
      */
     public void setCityId(Long cityId) {
         this.cityId = cityId;
     }

     /**
      * 获取城市id
      */
     public Long getCityId() {
         return this.cityId;
     }
     /**
      * 设置区id
      */
     public void setCountyId(Long countyId) {
         this.countyId = countyId;
     }

     /**
      * 获取区id
      */
     public Long getCountyId() {
         return this.countyId;
     }
 }