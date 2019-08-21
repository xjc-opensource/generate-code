
package ext.opensource.generatecode.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.infrastructure.jdbc.JdbcColumn;
import org.infrastructure.jdbc.JdbcTable;
import org.infrastructure.jdbc.JdbcTypeConvert;
import org.infrastructure.utils.StringUtil;


/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TemplatesDbVar extends TemplatesVar {

    private static final long serialVersionUID = 1L;
    private JdbcTypeConvert jdbcTypeConvert = new JdbcTypeConvert();

    public void putJdbcType(Map<String, Object> map) {
        jdbcTypeConvert.putAll(map);
    }

    private String deletePrefix(String inputString) {
        if (inputString == null) {
            return null;
        }
        inputString = inputString.toLowerCase();
        // 将表名t_ 去掉
        if (inputString.startsWith("t_") || inputString.startsWith("v_")) {
            inputString = inputString.substring(2);
        }
        return inputString;
    }

    public void putTableName(String tableName, JdbcTable tableItem) {
        String remarks = tableName;
        String dbTableName = null;
        if (tableItem != null) {
            if (tableItem.getTableRemarks() != null) {
                remarks = tableItem.getTableRemarks();
                dbTableName = tableItem.getTableName();
            }
        }
        this.put("tableName", tableName);
        this.put("tableDbName", dbTableName);
        this.put("tableRemarks", remarks);
        this.putFunctonName(deletePrefix(tableName));
    }

    public void putTableColumn(List<JdbcColumn> list) {
        List<Map<String, Object>> varList = new ArrayList<>();
        for (JdbcColumn item : list) {
            Map<String, Object> itemMap = new HashMap<>();
            String name = item.getColumnName();
            String remarks = name;
            if ((item.getColumnRemarks() != null) && (item.getColumnRemarks().length() > 0)) {
                remarks = item.getColumnRemarks();
            }
            itemMap.put("dbName", name);
            itemMap.put("codeName", StringUtil.getCamelCaseString(name));
            itemMap.put("remarks", remarks);
            itemMap.put("javaType", jdbcTypeConvert.getJavaTypeString(item.getColumnType()));
            itemMap.put("autoIncr", item.isAutoIncr());
            varList.add(itemMap);
        }
        this.put("tableFields", varList);
    }

}
