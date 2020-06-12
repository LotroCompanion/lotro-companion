package delta.games.lotro.gui.interceptor.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.interceptor.protocol.PacketsStatistics;

/**
 * Controller for a panel to display packets statistics.
 * @author DAM
 */
public class StatisticsPanelController
{
  // Data
  private PacketsStatistics _statistics;
  // GUI
  private JPanel _panel;
  // - raw
  private PacketCountsPanelController _raw;
  // - data
  private PacketCountsPanelController _data;
  // Timer
  private Timer _timer;

  /**
   * Constructor.
   * @param statistics Statistics to show.
   */
  public StatisticsPanelController(PacketsStatistics statistics)
  {
    _statistics=statistics;
    _raw=new PacketCountsPanelController(_statistics.getRawPacketsCounts());
    _data=new PacketCountsPanelController(_statistics.getDataPacketsCounts());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    buildPanel();
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // - raw
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    JPanel rawPanel=_raw.getPanel();
    rawPanel.setBorder(GuiFactory.buildTitledBorder("Packets"));
    panel.add(rawPanel,c);
    c.gridx++;
    // - data
    JPanel dataPanel=_data.getPanel();
    dataPanel.setBorder(GuiFactory.buildTitledBorder("Messages"));
    panel.add(dataPanel,c);
    _panel=panel;
    update();
    return panel;
  }

  /**
   * Start timer to refresh data.
   */
  public void start()
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        update();
      }
    };
    _timer=new Timer(2000,al);
    _timer.setRepeats(true);
    _timer.start();
  }

  /**
   * Stop timer to refresh data.
   */
  public void stop()
  {
    _timer.stop();
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Raw
    _raw.update();
    // Data
    _data.update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _statistics=null;
    // UI/controllers
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // - raw
    if (_raw!=null)
    {
      _raw.dispose();
      _raw=null;
    }
    // - data
    if (_data!=null)
    {
      _data.dispose();
      _data=null;
    }
  }
}
