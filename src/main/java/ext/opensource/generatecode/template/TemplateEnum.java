
package ext.opensource.generatecode.template;

/**
 * @author ben
 * @Title: TemplateType.java
 * @Description:
 **/
public class TemplateEnum {
    public enum TemplateType {
        FILE, CLASSLOADER
    }

    public enum FileProcessType {
        DBTABLE("DB_TABLE"), NORMAL("NORMAL");
       
        private String name;
        FileProcessType(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
       
    }
}
