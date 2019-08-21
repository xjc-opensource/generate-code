
package ext.opensource.generatecode.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ext.opensource.generatecode.template.TemplateEnum.FileProcessType;

import org.infrastructure.utils.DirectoryUtil;
import lombok.Data;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

@Data
public class TemplateParameter {
	private String path;
	private int ver;
	private String defaultDbTableName;

	private String defaultNormalName;
	private List<TemplateFile> fileList;
	
	private TemplateDbConnect dbConnect = new TemplateDbConnect();
	private Map<String, Object> varMap = new HashMap<>();
	private Map<String, Object> jdbcTypeConvertMap = new HashMap<>();
	
	public List<TemplateFile> getNormalFileList() {
	    return getFileListByPrcessType(FileProcessType.NORMAL); 
	}
	
	public List<TemplateFile> getDbTableFileList() {
	       return getFileListByPrcessType(FileProcessType.DBTABLE);
	}
	
	private List<TemplateFile> getFileListByPrcessType(FileProcessType processType) {
	    if ((fileList == null) || (processType == null)) {
	        return null;
	    }
	    String processTypeName = processType.getName();
	    List<TemplateFile> result = new ArrayList<>();
	    for (TemplateFile fileObj : fileList) {
	        if ((fileObj != null) && (processTypeName.equals(fileObj.getProcessType()))) {
	            result.add(fileObj);
	        }
	    }
	    return result;
	}
	
	public static TemplateParameter getSampleInstance() {
		TemplateParameter instance = new TemplateParameter();
		List<TemplateFile> list = new ArrayList<>();
		
		TemplateFile fileObj = new TemplateFile();
		fileObj.setProcessType(FileProcessType.DBTABLE.getName());
		fileObj.setFileName("jpa_entity.ttl");
		fileObj.setNewFileName("${db_tableCodeName}Entity.ttl");
		fileObj.setPackageName("ext.opensource.example.entity");
		fileObj.setPackageAsDir(true);
	     fileObj.setDisable(false);
		
		list.add(fileObj);
		instance.setPath(DirectoryUtil.getCurrentAbsolutePath());
		instance.setFileList(list);
		instance.setVer(1);
		instance.setDefaultDbTableName("");
		
		instance.getDbConnect().setDriver("com.mysql.jdbc.Driver");
		instance.getDbConnect().setUrl("jdbc:mysql://127.0.0.1:3306/demo?characterEncoding=utf-8");
		instance.getDbConnect().setUsername("root");
		instance.getDbConnect().setPassword("123456");
			
		instance.getVarMap().put("var_author", "code");
		instance.getJdbcTypeConvertMap().put("CHAR", "String");
		return instance;
	}
}
