package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.memory.extraction.CharExtractor;
import delta.games.lotro.memory.extraction.MemoryExtractionSession;
import delta.games.lotro.memory.facade.data.ImportConfiguration;
import delta.games.lotro.memory.facade.data.ImportStatus;
import delta.games.lotro.memory.facade.data.ImportStatusData;
import delta.games.lotro.memory.facade.data.ImportStatusListener;
import delta.games.lotro.memory.io.MemoryAccess;
import delta.games.lotro.memory.io.jna.WinInterface;
import delta.games.lotro.utils.dat.DatInterface;

/**
 * Basic dialog to control the client import.
 * @author DAM
 */
public class ClientImportDialogController extends DefaultDialogController implements ImportStatusListener
{
  /**
   * Identifier of this dialog.
   */
  public static final String IDENTIFIER="CLIENT_IMPORT";
  // UI
  // - start button
  private JButton _startButton;
  // Child controllers
  private ImportConfigurationPanelController _configCtrl;
  private ClientImportHowToPanelController _howToCtrl;
  private ImportResultDisplayController _displayCtrl;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public ClientImportDialogController(WindowController parent)
  {
    super(parent);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setModal(false);
    dialog.setTitle("Import from local client"); // I18n
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  /**
   * Get the window identifier for a given toon.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // How-to panel
    _howToCtrl=new ClientImportHowToPanelController();
    JComponent howTo=_howToCtrl.getHowToGadget();
    howTo.setBorder(GuiFactory.buildTitledBorder("How To?")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(howTo,c);
    // Configuration panel
    _configCtrl=new ImportConfigurationPanelController();
    JPanel configurationPanel=_configCtrl.getPanel();
    configurationPanel.setBorder(GuiFactory.buildTitledBorder("Configuration")); // I18n
    c=new GridBagConstraints(0,1,1,2,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(configurationPanel,c);
    // Commands panel
    JPanel commandPanel=buildCommandsPanel();
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(commandPanel,c);
    // Display panel
    _displayCtrl=new ImportResultDisplayController();
    JPanel displayPanel=_displayCtrl.getPanel();
    displayPanel.setBorder(GuiFactory.buildTitledBorder("Results")); // I18n
    c=new GridBagConstraints(1,2,1,0,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(displayPanel,c);
    return ret;
  }

  private JPanel buildCommandsPanel()
  {
    // Start button
    _startButton=GuiFactory.buildButton("Start"); // I18n
    ActionListener alStart=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doStart();
      }
    };
    _startButton.addActionListener(alStart);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_startButton,c);
    return panel;
  }

  private void doStart()
  {
    DatInterface datInterface=DatInterface.getInstance();
    DataFacade dataFacade=datInterface.getFacade();
    final MemoryAccess memoryAccess=buildMemoryAccess();
    final ImportConfiguration config=_configCtrl.getConfig();
    MemoryExtractionSession session=new MemoryExtractionSession(memoryAccess,dataFacade,config,this);
    final CharExtractor extractor=new CharExtractor(session);
    Runnable r=new Runnable()
    {
      public void run()
      {
        extractor.doIt();
      }
    };
    Thread t=new Thread(r,"Import worker");
    t.start();
  }

  private MemoryAccess buildMemoryAccess()
  {
    WinInterface win=new WinInterface();
    boolean ok=win.start();
    if (!ok)
    {
      return null;
    }
    return win;
  }

  @Override
  public void statusUpdate(final ImportStatusData data)
  {
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        updateUi(data);
      }
    };
    SwingUtilities.invokeLater(r);
  }

  private void updateUi(ImportStatusData data)
  {
    // Update results display
    _displayCtrl.updateUi(data);
    // Start button
    boolean enabled=getStartButtonState(data);
    _startButton.setEnabled(enabled);
    _configCtrl.setUiState(enabled);
  }

  private boolean getStartButtonState(ImportStatusData data)
  {
    ImportStatus importStatus=data.getImportStatus();
    if (importStatus==ImportStatus.RUNNING)
    {
      return false;
    }
    return true;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    _startButton=null;
    if (_displayCtrl!=null)
    {
      _displayCtrl.dispose();
      _displayCtrl=null;
    }
    if (_howToCtrl!=null)
    {
      _howToCtrl.dispose();
      _howToCtrl=null;
    }
    if (_configCtrl!=null)
    {
      _configCtrl.dispose();
      _configCtrl=null;
    }
    super.dispose();
  }
}
