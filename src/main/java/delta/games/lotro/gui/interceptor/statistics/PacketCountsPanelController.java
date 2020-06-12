package delta.games.lotro.gui.interceptor.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.interceptor.protocol.PacketsStatistics.PacketCounts;

/**
 * Controller for a panel to display some packet counts.
 * @author DAM
 */
public class PacketCountsPanelController
{
  // Data
  private PacketCounts _counts;
  // GUI
  private JPanel _panel;
  // - server
  private PacketCountsForSourcePanelController _server;
  // - client
  private PacketCountsForSourcePanelController _client;

  /**
   * Constructor.
   * @param counts Counts to show.
   */
  public PacketCountsPanelController(PacketCounts counts)
  {
    _counts=counts;
    _server=new PacketCountsForSourcePanelController(_counts.getServerPackets());
    _client=new PacketCountsForSourcePanelController(_counts.getClientPackets());
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

    // - server
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    JPanel serverPanel=_server.getPanel();
    serverPanel.setBorder(GuiFactory.buildTitledBorder("Server"));
    panel.add(serverPanel,c);
    c.gridx++;
    // - client
    JPanel clientPanel=_client.getPanel();
    clientPanel.setBorder(GuiFactory.buildTitledBorder("Client"));
    panel.add(clientPanel,c);
    _panel=panel;
    update();
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Server
    _server.update();
    // Client
    _client.update();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _counts=null;
    // UI/controllers
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // - server
    if (_server!=null)
    {
      _server.dispose();
      _server=null;
    }
    // - client
    if (_client!=null)
    {
      _client.dispose();
      _client=null;
    }
  }
}
