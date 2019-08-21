package ext.opensource.generatecode.template;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.infrastructure.jdbc.JdbcColumn;
import org.infrastructure.jdbc.JdbcTable;
import org.infrastructure.jdbc.JdbcUtils;
import org.infrastructure.utils.DirectoryUtil;
import org.infrastructure.utils.FileUtil;
import org.infrastructure.utils.StringUtil;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import com.alibaba.fastjson.JSON;

import ext.opensource.generatecode.template.TemplateEnum.TemplateType;

/**
 * @author ben
 * @Title: basic
 * @Description:
 **/

public class TemplatesProcess {
    private TemplateType templateType;
    private TemplateParameter templateParameter;
    private String prefix;

    public TemplatesProcess(String prefix) {
        this(prefix, autoTemplateType(prefix));
    }

    public TemplatesProcess(String prefix, TemplateType templateType) {
        this.prefix = prefix;
        this.templateType = templateType;
        loadFromJson();

        System.out.println("path:" + templateParameter.getPath());
    }

    private static TemplateType autoTemplateType(String prefix) {
        TemplateType templateType = TemplateType.CLASSLOADER;
        if (DirectoryUtil.isAbsoluteDir(prefix)) {
            templateType = TemplateType.FILE;
        }
        return templateType;
    }

    private void loadFromJson() {
        String jsonStr = null;

        switch (templateType) {
            case CLASSLOADER:
                System.err.println("load class:");
                InputStream in = TemplatesProcess.class.getClassLoader()
                        .getResourceAsStream(this.prefix + "config.json");
                try {
                    jsonStr = FileUtil.readString(in, null);
                } finally {
                    FileUtil.closeIO(in);
                }
                break;
            case FILE:
                String path = DirectoryUtil.getDirPath(this.prefix) + "config.json";
                System.err.println("load file:" + path);
                jsonStr = FileUtil.getFileString(path, null);
                break;
        }

        if (!StringUtil.isEmptyOrNull(jsonStr)) {
            templateParameter = JSON.parseObject(jsonStr, TemplateParameter.class);
        } else {
            System.err.println("load def");
            templateParameter = TemplateParameter.getSampleInstance();
        }

        System.err.println("load:" + JSON.toJSONString(templateParameter));
    }

    private String processTemplateString(String templateStr, Map<String, Object> params) {
        if ((templateStr == null) || (templateStr.length() <= 0)) {
            return templateStr;
        }
        Context context = new Context();
        context.setVariables(params);
        TemplateEngine engine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        engine.setTemplateResolver(resolver);
        return engine.process(templateStr, context);
    }

    private void processTemplates(Map<String, Object> customVars, List<TemplateFile> fileList) {
        Context context = new Context();
        context.setVariables(templateParameter.getVarMap());
        context.setVariables(customVars);

        AbstractConfigurableTemplateResolver resolver = null;
        switch (templateType) {
            case CLASSLOADER:
                resolver = new ClassLoaderTemplateResolver();
                break;
            case FILE:
                resolver = new FileTemplateResolver();
                break;
        }

        // ServletContextTemplateResolver
        // UrlTemplateResolver

        if (resolver == null) {
            return;
        }

        resolver.setPrefix(this.prefix);
        resolver.setCacheable(false);// 设置不缓存
        resolver.setTemplateMode(TemplateMode.TEXT);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        for (TemplateFile fileObj : fileList) {
            if (fileObj.isDisable()) {
                continue;
            }
            String souFile = fileObj.getFileName();
            String desFile = processTemplateString(fileObj.getNewFileName(), customVars);

            String fileDir = DirectoryUtil.getDirPath(templateParameter.getPath());

            String packageName =
                    processTemplateString(fileObj.getPackageName(), templateParameter.getVarMap());
            boolean packageAsDir = fileObj.isPackageAsDir();

            if ((packageName != null) && (packageName.length() > 0)) {
                context.setVariable("_packageName", packageName);
                if (packageAsDir) {
                    fileDir = fileDir + DirectoryUtil.replaceStrByFileSeparator(packageName, ".");
                }
            } else {
                context.removeVariable("_packageName");
            }

            DirectoryUtil.createDir(fileDir, true);

            System.out.println("file: " + fileDir + desFile);
            OutputStreamWriter out;
            try {
                out = new OutputStreamWriter(new FileOutputStream(fileDir + desFile),
                        StandardCharsets.UTF_8);
                engine.process(souFile, context, out);
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @return
     */
    public List<JdbcTable> getAllDbTable() {
        TemplateDbConnect connectObj = templateParameter.getDbConnect();
        if (connectObj != null) {
            Connection conn = JdbcUtils.getConnection(connectObj.getDriver(), connectObj.getUrl(),
                    connectObj.getUsername(), connectObj.getPassword());
            try {
                return JdbcUtils.getTables(conn);
            } finally {
                JdbcUtils.closeConnection(conn);
            }
        }
        return null;
    }

    /**
     * 
     * @param tableNames
     */
    public void processByTableFields(String tableNames) {
        if (tableNames == null) {
            return;
        }
        TemplateDbConnect connectObj = templateParameter.getDbConnect();
        if (connectObj != null) {
            Connection conn = JdbcUtils.getConnection(connectObj.getDriver(), connectObj.getUrl(),
                    connectObj.getUsername(), connectObj.getPassword());
            try {

                String[] tnList = tableNames.split(",");
                for (int i = 0; i < tnList.length; i++) {
                    String tableName = tnList[i].trim();
                    if (tableName.length() < 1) {
                        continue;
                    }
                    List<JdbcTable> tableList = JdbcUtils.getTables(conn, tableName);

                    JdbcTable tableItem = tableList.get(0);

                    List<JdbcColumn> fieldList = JdbcUtils.getTablesColumnList(conn, tableName);

                    TemplatesDbVar templatesDbVar = new TemplatesDbVar();
                    templatesDbVar.putJdbcType(templateParameter.getJdbcTypeConvertMap());

                    templatesDbVar.putTableName(tableName, tableItem);
                    templatesDbVar.putTableColumn(fieldList);
                    processTemplates(templatesDbVar, templateParameter.getDbTableFileList());

                    System.err.println("fieldList:" + tableName + JSON.toJSONString(fieldList));
                }
            } finally {
                JdbcUtils.closeConnection(conn);
            }
        }
    }

    public void processByTable() {
        TemplateDbConnect connectObj = templateParameter.getDbConnect();
        if (connectObj != null) {
            Connection conn = JdbcUtils.getConnection(connectObj.getDriver(), connectObj.getUrl(),
                    connectObj.getUsername(), connectObj.getPassword());
            try {
                List<JdbcTable> tableList = JdbcUtils.getTables(conn);
                System.err.println("tableList:" + JSON.toJSONString(tableList));
            } finally {

                JdbcUtils.closeConnection(conn);
            }
        }
    }

    public void processByDefault() {
        this.processByTable();
        
        processDbFile(templateParameter.getDefaultDbTableName());
        processNormalFile(templateParameter.getDefaultNormalName());
    }

    public void processDbFile(String listStr) {
        if ((listStr == null) || (listStr.length() <= 0)) {
            return;
        }
        this.processByTableFields(listStr);
    }

    public void processNormalFile(String listStr) {
        if ((listStr == null) || (listStr.length() <= 0)) {
            return;
        }
        
        TemplatesVar customVar = new TemplatesVar();
        String[] tnList = listStr.split(",");
        for (int i = 0; i < tnList.length; i++) {
            customVar.clear();

            String funcationName = tnList[i].trim();
            if (funcationName.length() < 1) {
                continue;
            }

            customVar.putFunctonName(funcationName);
            processTemplates(customVar, templateParameter.getNormalFileList());
        }
    }
}
