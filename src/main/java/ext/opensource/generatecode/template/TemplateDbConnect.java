package ext.opensource.generatecode.template;

import lombok.Data;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Data
public class TemplateDbConnect {
	private String url;
	private String username;
	private String password;
	private String driver;
}