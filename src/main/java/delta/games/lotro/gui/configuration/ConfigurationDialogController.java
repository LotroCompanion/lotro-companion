package delta.games.lotro.gui.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.dat.archive.DatChecks;
import delta.games.lotro.dat.data.DatConfiguration;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;
import delta.games.lotro.utils.gui.filechooser.FileChooserController;

/**
 * Controller for the "configuration" dialog.
 * @author DAM
 */
public class ConfigurationDialogController extends DefaultFormDialogController<ApplicationConfiguration>
{
  private static final int DAT_PATH_SIZE=50;
  private JTextField _datPath;
  private JButton _chooseButton;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param configuration Configuration to edit.
   */
  public ConfigurationDialogController(WindowController parentController, ApplicationConfiguration configuration)
  {
    super(parentController,configuration);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Configuration...");
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    // Build
    JPanel dataPanel=buildDatConfigurationPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Configuration");
    dataPanel.setBorder(pathsBorder);
    // Init
    init();
    return dataPanel;
  }

  private JPanel buildDatConfigurationPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // DAT path
    _datPath=GuiFactory.buildTextField("");
    _datPath.setColumns(DAT_PATH_SIZE);
    _chooseButton=GuiFactory.buildButton("Choose...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doChoose();
      }
    };
    _chooseButton.addActionListener(al);

    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("LOTRO client path:"),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_datPath,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_chooseButton,c);
    return panel;
  }

  private void init()
  {
    DatConfiguration datConfiguration=_data.getDatConfiguration();
    String datPathStr=datConfiguration.getRootPath().getAbsolutePath();
    _datPath.setText(datPathStr);
  }

  private void doChoose()
  {
    FileChooserController ctrl=new FileChooserController("ApplicationConfiguration", "Choose LOTRO client path...");
    DatConfiguration datConfiguration=_data.getDatConfiguration();
    File path=datConfiguration.getRootPath();
    ctrl.getChooser().setCurrentDirectory(path);
    File datDir=ctrl.chooseDirectory(getWindow(),"OK");
    if (datDir!=null)
    {
      _datPath.setText(datDir.getAbsolutePath());
    }
  }

  @Override
  protected void okImpl()
  {
    String datPathStr=_datPath.getText();
    File datPath=new File(datPathStr);
    _data.getDatConfiguration().setRootPath(datPath);
    _data.saveConfiguration();
    _data.configurationUpdated();
  }

  @Override
  protected boolean checkInput()
  {
    String errorMsg=checkData();
    if (errorMsg==null)
    {
      return true;
    }
    showErrorMessage(errorMsg);
    return false;
  }

  private String checkData()
  {
    String errorMsg=null;
    String datPathStr=_datPath.getText();
    File datPath=new File(datPathStr);
    boolean ok=DatChecks.checkDatPath(datPath);
    if (!ok)
    {
      errorMsg="Invalid LOTRO installation directory!";
    }
    return errorMsg;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Configuration";
    JDialog dialog=getDialog();
    GuiFactory.showErrorDialog(dialog,errorMsg,title);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _datPath=null;
    _chooseButton=null;
  }
}
