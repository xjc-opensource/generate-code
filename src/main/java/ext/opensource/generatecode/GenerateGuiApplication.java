
package ext.opensource.generatecode;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import ext.opensource.generatecode.template.TemplatesProcess;

import org.infrastructure.jdbc.JdbcTable;

import javax.swing.JList;
import java.awt.Panel;
import java.awt.List;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Window.Type;
import javax.swing.JTextPane;

/**
 * @author ben
 * @Title: GenerateGuiApplication.java
 * @Description:
 **/

public class GenerateGuiApplication {

    private JFrame frmGenerateCode;

    private TemplatesProcess tmpsCfg;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GenerateGuiApplication window = new GenerateGuiApplication();
                    window.frmGenerateCode.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public GenerateGuiApplication() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frmGenerateCode = new JFrame();
        frmGenerateCode.setResizable(false);
        frmGenerateCode.setType(Type.UTILITY);
        frmGenerateCode.setTitle("Generate Code");
        frmGenerateCode.setBounds(100, 100, 354, 300);
        frmGenerateCode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JComboBox<String> cbTableName = new JComboBox();
        cbTableName.setEditable(true);
        cbTableName.setBounds(107, 11, 143, 24);

        JLabel lblNewLabel = new JLabel("TableName:");
        lblNewLabel.setBounds(25, 14, 80, 18);

        JButton btnExec = new JButton("exec");
        btnExec.setBounds(100, 138, 136, 27);
        frmGenerateCode.getContentPane().setLayout(null);
        frmGenerateCode.getContentPane().add(lblNewLabel);
        frmGenerateCode.getContentPane().add(cbTableName);
        frmGenerateCode.getContentPane().add(btnExec);

        TextField txtNormal = new TextField();
        txtNormal.setBounds(107, 77, 145, 25);
        frmGenerateCode.getContentPane().add(txtNormal);

        tmpsCfg = new TemplatesProcess("templates/");
        //txtNormal.setText(tmpsCfg.getTemplateParameter().getDefaultNormalName());
        
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnExit.setBounds(100, 194, 136, 27);
        frmGenerateCode.getContentPane().add(btnExit);
        
        JLabel lblNormal = new JLabel("Normal:");
        lblNormal.setBounds(25, 77, 80, 18);
        frmGenerateCode.getContentPane().add(lblNormal);

        java.util.List<JdbcTable> tableList = tmpsCfg.getAllDbTable();
        cbTableName.addItem("");
        if (tableList != null) {
            for (JdbcTable item : tableList) {
                cbTableName.addItem(item.getTableName());
            }
        }

        btnExec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tmpsCfg.processByTableFields(cbTableName.getSelectedItem().toString());
                tmpsCfg.processNormalFile(txtNormal.getText());
            }
        });
        
        frmGenerateCode.setLocationRelativeTo(null);
    }
}
