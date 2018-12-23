package id.co.ardata.megatrik.megatrikdriver.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ErrorApiMsg{

	@SerializedName("error")
	private String error;

	@SerializedName("message")
	private String message;

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ErrorApiMsg{" + 
			"error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}