package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.memory.extraction.MemoryExtractionSession;
import delta.games.lotro.memory.extraction.extractors.MainExtractor;
import delta.games.lotro.memory.extraction.session.ImportConfiguration;
import delta.games.lotro.memory.extraction.session.ImportSession;
import delta.games.lotro.memory.extraction.session.status.ImportStatus;
import delta.games.lotro.memory.extraction.session.status.ImportStatusData;
import delta.games.lotro.memory.extraction.session.status.ImportStatusListener;
import delta.games.lotro.memory.io.MemoryAccess;
import delta.games.lotro.memory.io.jna.LotroProcess;
import delta.games.lotro.memory.io.jna.WinInterface;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;
import delta.games.lotro.utils.dat.DatInterface;

/**
 * Basic dialog to control the client import.
 * @author DAM
 */
public class ClientImportDialogController extends DefaultDialogController implements ImportStatusListener
{

  private static final Logger LOGGER=LoggerFactory.getLogger(ClientImportDialogController.class);

  /**
   * Identifier of this dialog.
   */
  public static final String IDENTIFIER="CLIENT_IMPORT";
  // UI
  // - Processes combo box
  private ComboBoxController<LotroProcess> _processComboBoxCtrl;
  // - start button
  private JButton _startButton;
  // Child controllers
  private ImportConfigurationPanelController _configCtrl;
  private ClientImportHowToPanelController _howToCtrl;
  private ImportResultDisplayController _displayCtrl;

  // To refresh the LOTRO processes list
  private ScheduledExecutorService _processListUpdater;

  // As the LOTRO memory importer code is using some global stuff, it's not safe to refresh the list of LOTRO
  // processes (with details from LOTRO memory) while doing a memory import, so we use a mutex to avoid doing both concurrently...
  private Semaphore _lotroMemoryMutex = new Semaphore(1);

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

  /**
   * Update the list of LOTRO processes currently running
   * @param processes Updated list of LOTRO processes
   */
  private void updateProcessComboBox(final List<LotroProcess> processes)
  {
    if (processes.size()==0)
    {
      _processComboBoxCtrl.removeAllItems();
      _processComboBoxCtrl.addEmptyItem(Labels.getLabel("clientimport.processeslist.notfound"));
      _processComboBoxCtrl.selectItem(null);
    }
    else
    {
      LotroProcess oldSelected = _processComboBoxCtrl.getSelectedItem();
      _processComboBoxCtrl.removeAllItems();
      _processComboBoxCtrl.addItems(processes);
      _processComboBoxCtrl.selectItem(oldSelected);
      if (_processComboBoxCtrl.getSelectedItem()==null)
      {
        _processComboBoxCtrl.selectItem(_processComboBoxCtrl.getItems().get(0));
      }
    }
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

    // Select LOTRO process combox box
    _processComboBoxCtrl=new ComboBoxController<LotroProcess>();
    _processComboBoxCtrl.addListener(new ItemSelectionListener<LotroProcess>() {
      @Override
      public void itemSelected(LotroProcess process) {
        // As 32bits support has been dropped, we disable the Start button for 32bits clients...
        _startButton.setEnabled(process!=null && process.is64bits);
      }
    });
    _processComboBoxCtrl.addEmptyItem(Labels.getLabel("clientimport.processeslist.finding"));
    _processListUpdater=Executors.newSingleThreadScheduledExecutor();
    _processListUpdater.scheduleWithFixedDelay(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          _lotroMemoryMutex.acquire();
        }
        catch (InterruptedException e)
        {
          Thread.currentThread().interrupt();
          return;
        }
        List<LotroProcess> processes=null;
        try
        {
          processes=new WinInterface().findAllLotroProcesses(ApplicationConfiguration.getInstance().getDatConfiguration());
        }
        catch (Throwable t)
        {
          LOGGER.error("Exception! See logs.",t);
        }
        finally
        {
          _lotroMemoryMutex.release();
        }
        final List<LotroProcess> updatedProcesses=processes;
        SwingUtilities.invokeLater(new Runnable()
        {
          @Override
          public void run()
          {
            if (_processComboBoxCtrl!=null && updatedProcesses!=null)
            {
              updateProcessComboBox(updatedProcesses);
            }
          }
        });
      }
    }, 0, 3, TimeUnit.SECONDS); // fetch at start + refresh processes list every 3 seconds

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_startButton,c);
    c.gridx++;
    c.weightx=1;
    c.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_processComboBoxCtrl.getComboBox(),c);
    return panel;
  }

  private void doStart()
  {
    try
    {
      _lotroMemoryMutex.acquire();
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
      return;
    }
    try
    {
      DatInterface datInterface=DatInterface.getInstance();
      DataFacade dataFacade=datInterface.getFacade();
      final MemoryAccess memoryAccess=buildMemoryAccess();
      final ImportConfiguration config=_configCtrl.getConfig();
      MemoryExtractionSession session=new MemoryExtractionSession(memoryAccess,dataFacade);
      ImportSession importSession=new ImportSession(config);
      importSession.getStatus().setListener(this);
      final MainExtractor extractor=new MainExtractor(session,importSession);
      Runnable r=new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            extractor.doIt();
          }
          finally
          {
            _lotroMemoryMutex.release();
          }
        }
      };
      Thread t=new Thread(r,"Import worker");
      t.start();
    }
    catch (Throwable t)
    {
      _lotroMemoryMutex.release();
      throw t;
    }
  }

  private MemoryAccess buildMemoryAccess()
  {
    WinInterface win=new WinInterface();
    boolean ok=win.start(_processComboBoxCtrl.getSelectedItem());
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
    _processListUpdater.shutdownNow();
    _processListUpdater=null;
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
    if (_processComboBoxCtrl!=null)
    {
      _processComboBoxCtrl.dispose();
      _processComboBoxCtrl=null;
    }
    super.dispose();
  }
}
