package delta.games.lotro.gui.character.status.traits.skirmish;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.skirmish.SkirmishTraitsStatus;

/**
 * Panel to show slotted skirmish traits.
 * @author DAM
 */
public class SkirmishTraitsStatusPanelController extends AbstractPanelController
{
  // Controllers
  private SlottedSkirmishTraitsStatusPanelController _slotted;
  private KnownSkirmishTraitsStatusPanelController _known;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public SkirmishTraitsStatusPanelController(WindowController parent, SkirmishTraitsStatus status)
  {
    super(parent);
    _slotted=new SlottedSkirmishTraitsStatusPanelController(parent,status);
    _known=new KnownSkirmishTraitsStatusPanelController(parent,status);
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Slotted traits
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel slottedPanel=_slotted.getPanel();
    slottedPanel.setBorder(GuiFactory.buildTitledBorder("Slotted Traits"));
    panel.add(slottedPanel,c);
    Dimension minSize=slottedPanel.getPreferredSize();
    minSize.width+=10;
    slottedPanel.setMinimumSize(minSize);
    slottedPanel.setPreferredSize(minSize);

    // Known traits
    JPanel knownPanel=_known.getPanel();
    knownPanel.setBorder(GuiFactory.buildTitledBorder("Known Traits"));
    knownPanel.setPreferredSize(new Dimension(320,260));
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(knownPanel,c);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_slotted!=null)
    {
      _slotted.dispose();
      _slotted=null;
    }
    if (_known!=null)
    {
      _known.dispose();
      _known=null;
    }
  }
}
