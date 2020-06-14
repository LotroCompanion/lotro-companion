package delta.games.lotro.gui.interceptor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.UserConfig;
import delta.games.lotro.dat.data.DatConfiguration;
import delta.games.lotro.gui.configuration.ConfigurationDialogController;
import delta.games.lotro.gui.interceptor.statistics.StatisticsWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogListener;
import delta.games.lotro.interceptor.data.monitoring.filters.InterceptionLogFilter;
import delta.games.lotro.interceptor.input.InterceptorStateListener;
import delta.games.lotro.interceptor.protocol.PacketsStatistics;

/**
 * Basic dialog to control the network capture.
 * @author DAM
 */
public class InterceptorDialogController extends DefaultDialogController implements InterceptionLogListener,InterceptorStateListener
{
  /**
   * Identifier of this dialog.
   */
  public static final String IDENTIFIER="INTERCEPTOR";
  /**
   * Interceptor.
   */
  private InterceptorController _interceptorController;
  /**
   * Table UI controller.
   */
  private InterceptionLogTableController _tableController;
  /**
   * Filter UI controller.
   */
  private InterceptionLogFilterController _filterController;
  /**
   * Filter.
   */
  private InterceptionLogFilter _filter;
  /**
   * Start/stop button.
   */
  private JButton _startStopButton;
  /**
   * Button state.
   */
  private boolean _started=false;
  /**
   * Settings button.
   */
  private JButton _settingsButton;
  /**
   * Child controllers.
   */
  private WindowsManager _childControllers;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public InterceptorDialogController(WindowController parent)
  {
    super(parent);
    _childControllers=new WindowsManager();
    _interceptorController=new InterceptorController(this);
    _interceptorController.getSession().getLog().setListener(this);
    _filter=new InterceptionLogFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setModal(false);
    dialog.setTitle("Synchronizer");
    dialog.setResizable(true);
    dialog.setPreferredSize(new Dimension(800,500));
    dialog.setMinimumSize(new Dimension(600,300));
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
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel northPanel=buildNorthPanel();
    ret.add(northPanel,BorderLayout.NORTH);
    JPanel centerPanel=buildLogPanel();
    ret.add(centerPanel,BorderLayout.CENTER);
    return ret;
  }

  private JPanel buildLogPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("Interceptor");
    InterceptionLog log=_interceptorController.getLog();
    _tableController=new InterceptionLogTableController(prefs,_filter,log);
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    // Filter
    _filterController=new InterceptionLogFilterController(_filter,_tableController, log);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(scroll,c);
    // Border
    panel.setBorder(GuiFactory.buildTitledBorder("Log"));
    return panel;
  }

  private JPanel buildNorthPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Buttons
    JPanel buttonsPanel=buildButtonsPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    return panel;
  }

  private JPanel buildButtonsPanel()
  {
    // Start/stop
    _startStopButton=GuiFactory.buildButton("Start");
    ActionListener alStart=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doStartStop();
      }
    };
    _startStopButton.addActionListener(alStart);
    // Settings
    _settingsButton=GuiFactory.buildButton("Settings...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doSettings();
      }
    };
    _settingsButton.addActionListener(al);
    // Statistics
    JButton statisticsButton=GuiFactory.buildButton("Statistics...");
    ActionListener al2=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doStatistics();
      }
    };
    statisticsButton.addActionListener(al2);

    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(_startStopButton);
    panel.add(_settingsButton);
    panel.add(statisticsButton);
    return panel;
  }

  private void doStartStop()
  {
    if (_started)
    {
      _interceptorController.stop();
    }
    else
    {
      _interceptorController.start();
    }
    // Set to enabled waiting for its state to be updated by the listener
    _startStopButton.setEnabled(false);
  }

  @Override
  public void logUpdated()
  {
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        _filterController.refresh();
        _tableController.refresh();
      }
    };
    SwingUtilities.invokeLater(r);
  }

  @Override
  public void interceptorStarted()
  {
    updateButtonsState(true);
  }

  @Override
  public void interceptorEnded()
  {
    updateButtonsState(false);
  }

  private void updateButtonsState(final boolean running)
  {
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        _started=running;
        if (running)
        {
          _startStopButton.setText("Stop");
          _settingsButton.setEnabled(false);
        }
        else
        {
          _startStopButton.setText("Start");
          _settingsButton.setEnabled(true);
        }
        _startStopButton.setEnabled(true);
      }
    };
    SwingUtilities.invokeLater(r);
  }

  private void doSettings()
  {
    DatConfiguration configuration=UserConfig.getInstance().getDatConfiguration();
    ConfigurationDialogController dialog=new ConfigurationDialogController(this,configuration);
    dialog.getDialog().setLocationRelativeTo(this.getDialog());
    dialog.show(true);
  }

  private void doStatistics()
  {
    StatisticsWindowController statisticsController=getStatisticsController();
    if (statisticsController==null)
    {
      PacketsStatistics statistics=_interceptorController.getSession().getPacketsStatistics();
      statisticsController=new StatisticsWindowController(this,statistics);
      _childControllers.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(this.getWindow());
    }
    statisticsController.bringToFront();
  }

  private StatisticsWindowController getStatisticsController()
  {
    WindowController controller=_childControllers.getWindow(StatisticsWindowController.IDENTIFIER);
    return (StatisticsWindowController)controller;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    _filter=null;
    if (_interceptorController!=null)
    {
      _interceptorController.dispose();
      _tableController=null;
    }
    if (_childControllers!=null)
    {
      _childControllers.disposeAll();
      _childControllers=null;
    }
  }
}
