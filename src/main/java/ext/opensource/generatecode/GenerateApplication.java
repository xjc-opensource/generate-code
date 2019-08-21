package ext.opensource.generatecode;

import ext.opensource.generatecode.template.TemplatesProcess;

/**
 * @author ben
 * @Title: GenerateApplicaton.java
 * @Description:
 **/

public class GenerateApplication {
	public static void main(String[] args) {
		String prefix = "templates/";
		//prefix = "E:\\dev\\ext-opensource-web\\code-templates\\";
		TemplatesProcess tmpCfg = new TemplatesProcess(prefix);
		tmpCfg.processByDefault();
	}
}
