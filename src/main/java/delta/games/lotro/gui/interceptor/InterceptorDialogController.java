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
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.UserConfig;
import delta.games.lotro.dat.data.DatConfiguration;
import delta.games.lotro.gui.configuration.ConfigurationDialogController;
import delta.games.lotro.gui.interceptor.statistics.StatisticsPanelController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogListener;
import delta.games.lotro.interceptor.data.monitoring.filters.InterceptionLogFilter;
import delta.games.lotro.interceptor.protocol.PacketsStatistics;

/**
 * Basic dialog to control the network capture.
 * @author DAM
 */
public class InterceptorDialogController extends DefaultDialogController implements InterceptionLogListener
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
   * Statistics.
   */
  private StatisticsPanelController _statisticsController;
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
   * Constructor.
   * @param parent Parent window controller.
   */
  public InterceptorDialogController(WindowController parent)
  {
    super(parent);
    _interceptorController=new InterceptorController(this);
    _filter=new InterceptionLogFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setModal(false);
    dialog.setTitle("Synchronizer");
    dialog.setResizable(true);
    dialog.setPreferredSize(new Dimension(800,300));
    dialog.setMinimumSize(new Dimension(400,200));
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
    JPanel centerPanel=buildTablePanel();
    ret.add(centerPanel,BorderLayout.CENTER);
    return ret;
  }

  private JPanel buildTablePanel()
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
    return panel;
  }

  private JPanel buildNorthPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Buttons
    JPanel buttonsPanel=buildButtonsPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    // Statistics
    PacketsStatistics statistics=_interceptorController.getSession().getPacketsStatistics();
    _statisticsController=new StatisticsPanelController(statistics);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_statisticsController.getPanel(),c);
    // Settings
    JButton settings=GuiFactory.buildButton("Settings...");
    c=new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(settings,c);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doSettings();
      }
    };
    settings.addActionListener(al);
    return panel;
  }

  private JPanel buildButtonsPanel()
  {
    // Start
    JButton startButton=GuiFactory.buildButton("Start");
    ActionListener alStart=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doStart();
      }
    };
    startButton.addActionListener(alStart);
    // Stop
    JButton stopButton=GuiFactory.buildButton("Stop");
    ActionListener alStop=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doStop();
      }
    };
    stopButton.addActionListener(alStop);

    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(startButton);
    panel.add(stopButton);
    return panel;
  }

  private void doStart()
  {
    _interceptorController.start();
    _statisticsController.start();
  }

  private void doStop()
  {
    _interceptorController.stop();
    _statisticsController.stop();
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

  private void doSettings()
  {
    DatConfiguration configuration=UserConfig.getInstance().getDatConfiguration();
    ConfigurationDialogController dialog=new ConfigurationDialogController(this,configuration);
    dialog.getDialog().setLocationRelativeTo(this.getDialog());
    dialog.show(true);
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
  }
}
