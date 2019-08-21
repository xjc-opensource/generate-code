
package ext.opensource.generatecode.template;

import java.util.HashMap;
import org.infrastructure.utils.StringUtil;

/**
 * @author ben
 * @Title: TemplatesVar.java
 * @Description:
 **/

public class TemplatesVar extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public void putFunctonName(String name) {
        this.put("functionName", StringUtil.getCamelCaseString(name,true));
        this.put("functiontCamelName", StringUtil.getCamelCaseString(name));
    }

}
