package com.yuqincar.domain.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class User extends BaseEntity implements Serializable {
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id")},
		inverseJoinColumns = { @JoinColumn(name = "role_id") })
	@Text("角色")
	private Set<Role> roles = new HashSet<Role>();
	
	@Text("部门")
	@ManyToOne
	@JoinColumn(nullable=false)
	private Department department;
	
	@Text("用户类型")
	@Column(nullable=false)
	private UserTypeEnum userType; //用户类型
	
	@Text("生日")
	@Temporal(TemporalType.DATE)
	private Date birth;
	
	@Text("登录名")
	@Column(nullable=false,unique=true)
	private String loginName;
	
	@Text("密码")
	@Column(nullable=false)
	private String password;
	
	@Text("姓名")
	@Column(nullable=false)
	private String name; 
	
	@Text("性别")
	private UserGenderEnum gender; 
	
	@Column(nullable=false, unique=true)
	@Text("手机")
	private String phoneNumber; 
	
	@Text("邮箱")
	private String email; 
	
	@Text("说明")
	private String description;
	
	@OneToOne
	@Text("驾照")
	private DriverLicense driverLicense;	//驾照。只有当userType==DRIVER时才有意义。
	
	@Text("用户状态")
	private UserStatusEnum status;	//用户状态
	
	@Text("APP设备类型")
	private UserAPPDeviceTypeEnum appDeviceType;
	
	@Text("APP标识Token")
	private String appDeviceToken;

	@Transient
	private static Set<String> BASE_PRIVILEGE_URLS;
	
	private Set<String> getBasePrivilegeUrls(){
		if(BASE_PRIVILEGE_URLS==null){
			BASE_PRIVILEGE_URLS=new HashSet<String>();
			BASE_PRIVILEGE_URLS.add("/home_index");
			BASE_PRIVILEGE_URLS.add("/login_logout");
			BASE_PRIVILEGE_URLS.add("/home_left");
			BASE_PRIVILEGE_URLS.add("/user_info");
			BASE_PRIVILEGE_URLS.add("/home_welcome");
			BASE_PRIVILEGE_URLS.add("/car_popup");
			BASE_PRIVILEGE_URLS.add("/customerOrganization_popup");
			BASE_PRIVILEGE_URLS.add("/user_popup");
			BASE_PRIVILEGE_URLS.add("/user_changePassword");
			BASE_PRIVILEGE_URLS.add("/user_changePhoneNumber");
			BASE_PRIVILEGE_URLS.add("/user_detail");
		}
		return BASE_PRIVILEGE_URLS;
	}
	
	public boolean hasPrivilegeByUrl(String privUrl) {
		// 获取访问的路径
		int pos = privUrl.indexOf("?");
		if (pos > -1) {
			privUrl = privUrl.substring(0, pos);
		}
		pos = privUrl.indexOf(".action");
		if (pos > -1) {
			privUrl = privUrl.substring(0, pos);
		}
				
		//如果是基本功能，就可以访问
		if(getBasePrivilegeUrls().contains(privUrl))
			return true;

		Collection<String> userPrivileges=(Collection<String>)ActionContext.getContext().getSession().get("userPrivileges");
		if (userPrivileges.contains(privUrl)) {
			return true;
		}else {
			System.out.println("Fail authorization, privUrl="+privUrl);
			return false;
		}
		
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserGenderEnum getGender() {
		return gender;
	}

	public void setGender(UserGenderEnum gender) {
		this.gender = gender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public UserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(UserTypeEnum userType) {
		this.userType = userType;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public DriverLicense getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(DriverLicense driverLicense) {
		this.driverLicense = driverLicense;
	}

	public UserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}

	public UserAPPDeviceTypeEnum getAppDeviceType() {
		return appDeviceType;
	}

	public void setAppDeviceType(UserAPPDeviceTypeEnum appDeviceType) {
		this.appDeviceType = appDeviceType;
	}

	public String getAppDeviceToken() {
		return appDeviceToken;
	}

	public void setAppDeviceToken(String appDeviceToken) {
		this.appDeviceToken = appDeviceToken;
	}

}
