package id.co.ardata.megatrik.megatrikdriver.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class UserOrder{

	@SerializedName("os_player_id")
	private String osPlayerId;

	@SerializedName("technician_orders")
	private List<TechnicianOrdersItem> technicianOrders;

	@SerializedName("password")
	private String password;

	@SerializedName("no_hp")
	private String noHp;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("role_id")
	private int roleId;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("email_verified_at")
	private String emailVerifiedAt;

	@SerializedName("id")
	private int id;

	@SerializedName("remember_token")
	private String rememberToken;

	@SerializedName("email")
	private String email;

	public void setOsPlayerId(String osPlayerId){
		this.osPlayerId = osPlayerId;
	}

	public String getOsPlayerId(){
		return osPlayerId;
	}

	public void setTechnicianOrders(List<TechnicianOrdersItem> technicianOrders){
		this.technicianOrders = technicianOrders;
	}

	public List<TechnicianOrdersItem> getTechnicianOrders(){
		return technicianOrders;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setNoHp(String noHp){
		this.noHp = noHp;
	}

	public String getNoHp(){
		return noHp;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setRoleId(int roleId){
		this.roleId = roleId;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setEmailVerifiedAt(String emailVerifiedAt){
		this.emailVerifiedAt = emailVerifiedAt;
	}

	public String getEmailVerifiedAt(){
		return emailVerifiedAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRememberToken(String rememberToken){
		this.rememberToken = rememberToken;
	}

	public String getRememberToken(){
		return rememberToken;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"UserOrder{" + 
			"os_player_id = '" + osPlayerId + '\'' + 
			",technician_orders = '" + technicianOrders + '\'' + 
			",password = '" + password + '\'' + 
			",no_hp = '" + noHp + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",role_id = '" + roleId + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",email_verified_at = '" + emailVerifiedAt + '\'' + 
			",id = '" + id + '\'' + 
			",remember_token = '" + rememberToken + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}