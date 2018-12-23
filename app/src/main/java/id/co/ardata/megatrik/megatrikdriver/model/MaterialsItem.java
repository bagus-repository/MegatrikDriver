package id.co.ardata.megatrik.megatrikdriver.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class MaterialsItem{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("material_list_id")
	private int materialListId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public MateriallistsItem getMateriallistsItem() {
		return materiallistsItem;
	}

	public void setMateriallistsItem(MateriallistsItem materiallistsItem) {
		this.materiallistsItem = materiallistsItem;
	}

	@SerializedName("materiallist")
	private MateriallistsItem materiallistsItem;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMaterialListId(int materialListId){
		this.materialListId = materialListId;
	}

	public int getMaterialListId(){
		return materialListId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"MaterialsItem{" + 
			"quantity = '" + quantity + '\'' + 
			",updated_at = '" + updatedAt + '\'' +
			",material_list_id = '" + materialListId + '\'' +
			",created_at = '" + createdAt + '\'' +
			",materiallist = '" + materiallistsItem + '\'' +
			",id = '" + id + '\'' +
			"}";
		}
}