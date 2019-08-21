
package ext.opensource.generatecode.template;

import lombok.Data;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/
@Data
public class TemplateFile {
	private String fileName;
	private String newFileName;
	private String packageName;
	private boolean packageAsDir;
	private String processType;
	private boolean disable=false;
}
