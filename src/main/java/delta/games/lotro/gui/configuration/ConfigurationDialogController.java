package delta.games.lotro.gui.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.config.DataConfiguration;
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
  // DAT files path
  private static final int DAT_PATH_SIZE=50;
  private JTextField _datPath;
  private JButton _datPathChooseButton;
  // Usage data path
  private static final int USER_DATA_PATH_SIZE=50;
  private JTextField _dataPath;
  private JButton _dataPathChooseButton;
  // Labels
  private LabelsConfigurationPanelController _labels;

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
    dialog.setTitle("Configuration..."); // I18n
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    // Build components
    JPanel datPanel=buildDatConfigurationPanel();
    JPanel dataPanel=buildDataConfigurationPanel();
    _labels=new LabelsConfigurationPanelController();
    // Init
    init();
    // Layout
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,0,5),0,0);
    ret.add(datPanel,c);
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    ret.add(dataPanel,c);
    c=new GridBagConstraints(0,2,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(_labels.getPanel(),c);
    return ret;
  }

  private JPanel buildDatConfigurationPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // DAT path
    _datPath=GuiFactory.buildTextField("");
    _datPath.setColumns(DAT_PATH_SIZE);
    _datPathChooseButton=GuiFactory.buildButton("Choose..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doChooseDATFilesPath();
      }
    };
    _datPathChooseButton.addActionListener(al);

    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Directory:"),c); // I18n
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_datPath,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_datPathChooseButton,c);

    panel.setBorder(GuiFactory.buildTitledBorder("LOTRO Client")); // I18n
    return panel;
  }

  private JPanel buildDataConfigurationPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // User data path
    _dataPath=GuiFactory.buildTextField("");
    _dataPath.setColumns(USER_DATA_PATH_SIZE);
    _dataPathChooseButton=GuiFactory.buildButton("Choose..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doChooseDataPath();
      }
    };
    _dataPathChooseButton.addActionListener(al);

    // Assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Directory:"),c); // I18n
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_dataPath,c);
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_dataPathChooseButton,c);

    panel.setBorder(GuiFactory.buildTitledBorder("User data")); // I18n
    return panel;
  }

  private void init()
  {
    // DAT
    DatConfiguration datConfiguration=_data.getDatConfiguration();
    String datPathStr=datConfiguration.getRootPath().getAbsolutePath();
    _datPath.setText(datPathStr);
    // Data
    DataConfiguration dataConfiguration=_data.getDataConfiguration();
    String dataPathStr=dataConfiguration.getRootPath().getAbsolutePath();
    _dataPath.setText(dataPathStr);
    // Labels
    _labels.setConfig(_data.getLabelsConfiguration());
  }

  private void doChooseDATFilesPath()
  {
    FileChooserController ctrl=new FileChooserController("ApplicationConfiguration", "Choose LOTRO client path..."); // I18n
    DatConfiguration datConfiguration=_data.getDatConfiguration();
    File path=datConfiguration.getRootPath();
    ctrl.getChooser().setCurrentDirectory(path);
    File datDir=ctrl.chooseDirectory(getWindow(),"OK"); // I18n
    if (datDir!=null)
    {
      _datPath.setText(datDir.getAbsolutePath());
    }
  }

  private void doChooseDataPath()
  {
    FileChooserController ctrl=new FileChooserController("ApplicationConfigurationUserData", "Choose user data path..."); // I18n
    DataConfiguration dataConfiguration=_data.getDataConfiguration();
    File path=dataConfiguration.getRootPath();
    ctrl.getChooser().setCurrentDirectory(path);
    File dataDir=ctrl.chooseDirectory(getWindow(),"OK"); // I18n
    if (dataDir!=null)
    {
      _dataPath.setText(dataDir.getAbsolutePath());
    }
  }

  @Override
  protected void okImpl()
  {
    // DAT
    String datPathStr=_datPath.getText();
    File datPath=new File(datPathStr);
    _data.getDatConfiguration().setRootPath(datPath);
    // Data
    String dataPathStr=_dataPath.getText();
    File dataPath=new File(dataPathStr);
    File oldDataPath=_data.getDataConfiguration().getRootPath();
    _data.getDataConfiguration().setRootPath(dataPath);
    // Labels
    _labels.saveTo(_data.getLabelsConfiguration());
    _data.saveConfiguration();
    if (!Objects.equals(dataPath,oldDataPath))
    {
      warnOnDataPathChange(oldDataPath,dataPath);
    }
    _data.configurationUpdated();
  }

  private void warnOnDataPathChange(File oldPath, File newPath)
  {
    String message="User data path has changed.\nPlease restart the application so that it takes effect!"; // I18n
    String title="Restart application"; // I18n
    GuiFactory.showInformationDialog(_dataPath,message,title);
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
    String errorMsg=checkDatConfiguration();
    if (errorMsg!=null)
    {
      return errorMsg;
    }
    return checkDataConfiguration();
  }

  private String checkDatConfiguration()
  {
    String errorMsg=null;
    // DAT
    String datPathStr=_datPath.getText();
    File datPath=new File(datPathStr);
    boolean ok=DatChecks.checkDatPath(datPath);
    if (!ok)
    {
      errorMsg="Invalid LOTRO installation directory!"; // I18n
    }
    return errorMsg;
  }

  private String checkDataConfiguration()
  {
    return null;
  }

  private void showErrorMessage(String errorMsg)
  {
    String title="Configuration"; // I18n
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
    _datPathChooseButton=null;
    _dataPath=null;
    _dataPathChooseButton=null;
    if (_labels!=null)
    {
      _labels.dispose();
      _labels=null;
    }
  }
}
