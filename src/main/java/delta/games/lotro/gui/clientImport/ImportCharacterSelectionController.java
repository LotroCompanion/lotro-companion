package delta.games.lotro.gui.clientImport;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.memory.io.LotroProcessesManager;
import delta.games.lotro.memory.io.jna.LotroProcess;
import delta.games.lotro.utils.dat.DatInterface;

/**
 * Controller for the character selection panel:
 * <ul>
 * <li>LotRO process selection combo-box,
 * <li>Refresh button + associated thread,
 * <li>a reference to the Start button in the parent controller.
 * </ul>
 * @author DAM
 */
public class ImportCharacterSelectionController extends AbstractPanelController
{
  // UI
  // - Start button
  private JButton _startButton;
  // - Refresh button
  private JButton _refreshButton;
  // - Processes combo box
  private ComboBoxController<LotroProcess> _processComboBoxCtrl;
  // Services
  private LotroProcessesManager _processesMgr;
  // Refresh management
  private Thread _refreshThread;

  /**
   * Constructor.
   * @param startButton Managed 'start' button.
   */
  public ImportCharacterSelectionController(JButton startButton)
  {
    _startButton=startButton;
    DatInterface datInterface=DatInterface.getInstance();
    DataFacade dataFacade=datInterface.getFacade();
    _processesMgr=new LotroProcessesManager(dataFacade);
    setPanel(buildPanel());
    refresh();
  }

  private JPanel buildPanel()
  {
    // Refresh button
    _refreshButton=GuiFactory.buildButton("Refresh");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        refresh();
      }
    };
    _refreshButton.addActionListener(al);
    // Process chooser
    _processComboBoxCtrl=new ComboBoxController<LotroProcess>();
    _processComboBoxCtrl.addListener(new ItemSelectionListener<LotroProcess>()
    {
      @Override
      public void itemSelected(LotroProcess process)
      {
        _startButton.setEnabled((process!=null));
      }
    });

    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(_processComboBoxCtrl.getComboBox());
    ret.add(_refreshButton);
    return ret;
  }

  /**
   * Update the list of LOTRO processes currently running
   * @param processes Updated list of LOTRO processes
   */
  private void updateProcessComboBox(List<LotroProcess> processes)
  {
    if (processes.isEmpty())
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
    LotroProcess process=_processComboBoxCtrl.getSelectedItem();
    boolean enabled=(process!=null);
    _startButton.setEnabled(enabled);
  }

  /**
   * Get the selected process.
   * @return A process (may be <code>null</code>).
   */
  public LotroProcess getSelectedProcess()
  {
    return _processComboBoxCtrl.getSelectedItem();
  }

  private void refresh()
  {
    lockRefresh(true);
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        updateThread();
      }
    };
    _refreshThread=new Thread(r,"Process selector update thread");
    _refreshThread.start();
  }

  private void updateThread()
  {
    try
    {
      List<LotroProcess> processes=_processesMgr.fetchDetailedProcesses();
      updateUi(processes);
    }
    catch(Exception e)
    {
      updateUi(null);
    }
    _refreshThread=null;
  }

  private void updateUi(List<LotroProcess> processes)
  {
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        setEnabled(true);
        updateProcessComboBox(processes);
      }
    };
    SwingUtilities.invokeLater(r);
  }

  private void lockRefresh(boolean lock)
  {
    if (_startButton!=null)
    {
      _startButton.setEnabled(!lock);
    }
    setEnabled(!lock);
  }

  /**
   * Enable/disable the managed UI.
   * @param enabled State to set.
   */
  public void setEnabled(boolean enabled)
  {
    if (_refreshButton!=null)
    {
      _refreshButton.setEnabled(enabled);
    }
    if (_processComboBoxCtrl!=null)
    {
      _processComboBoxCtrl.getComboBox().setEnabled(enabled);
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_processComboBoxCtrl!=null)
    {
      _processComboBoxCtrl.dispose();
      _processComboBoxCtrl=null;
    }
    // UI
    _startButton=null;
    _refreshButton=null;
    // Refresh
    _refreshThread=null;
  }
}
