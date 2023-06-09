package cn.tuyucheng.taketoday.swagger2bootmvc.controller;

import cn.tuyucheng.taketoday.swagger2bootmvc.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

	public UserController() {
		super();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/createUser", produces = "application/json; charset=UTF-8")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(value = "Create user", notes = "This method creates a new user")
	public User createUser(@ApiParam(
		name = "firstName",
		type = "String",
		value = "First Name of the user",
		example = "Vatsal",
		required = true) @RequestParam String firstName) {
		return new User(firstName);
	}
}