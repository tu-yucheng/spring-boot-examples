package cn.tuyucheng.taketoday.swagger2bootmvc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class User {

	@ApiModelProperty(value = "first name of the user", name = "firstName", dataType = "String", example = "Vatsal")
	String firstName;

	public User() {
		super();
	}

	public User(final String firstName) {
		super();
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}