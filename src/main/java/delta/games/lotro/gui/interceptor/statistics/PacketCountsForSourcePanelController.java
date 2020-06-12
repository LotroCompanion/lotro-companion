package delta.games.lotro.gui.interceptor.statistics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.form.LabeledComponent;
import delta.games.lotro.interceptor.protocol.PacketsStatistics.PacketCountsForSource;

/**
 * Controller for a panel to display the packet counts for a single source.
 * @author DAM
 */
public class PacketCountsForSourcePanelController
{
  // Data
  private PacketCountsForSource _counts;
  // GUI
  private JPanel _panel;
  // - OK
  private LabeledComponent<JLabel> _ok;
  // - Ignored
  private LabeledComponent<JLabel> _ignored;
  // - Failed
  private LabeledComponent<JLabel> _failed;

  /**
   * Constructor.
   * @param counts Counts to show.
   */
  public PacketCountsForSourcePanelController(PacketCountsForSource counts)
  {
    _counts=counts;
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
    initGadgets();
    buildPanel();
    return _panel;
  }

  private void initGadgets()
  {
    // - Instance ID
    _ok=new LabeledComponent<JLabel>("OK:",GuiFactory.buildLabel(""));
    // Validity date
    _ignored=new LabeledComponent<JLabel>("Ignored:",GuiFactory.buildLabel(""));
    // Birth name
    _failed=new LabeledComponent<JLabel>("Failed:",GuiFactory.buildLabel(""));
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // OK
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(_ok.getLabel(),c);
    GridBagConstraints c2=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(_ok.getComponent(),c2);
    c.gridy++;c2.gridy++;
    // Ignored
    panel.add(_failed.getLabel(),c);
    panel.add(_failed.getComponent(),c2);
    c.gridy++;c2.gridy++;
    // Failed
    panel.add(_ignored.getLabel(),c);
    panel.add(_ignored.getComponent(),c2);
    c.gridy++;c2.gridy++;
    _panel=panel;
    update();
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    // OK
    _ok.getComponent().setText(String.valueOf(_counts.getOk()));
    // Ignored
    _ignored.getComponent().setText(String.valueOf(_counts.getIgnored()));
    // Failed
    _failed.getComponent().setText(String.valueOf(_counts.getFailed()));
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
    // - OK
    if (_ok!=null)
    {
      _ok.dispose();
      _ok=null;
    }
    // - Ignored
    if (_ignored!=null)
    {
      _ignored.dispose();
      _ignored=null;
    }
    // - Failed
    if (_failed!=null)
    {
      _failed.dispose();
      _failed=null;
    }
  }
}
