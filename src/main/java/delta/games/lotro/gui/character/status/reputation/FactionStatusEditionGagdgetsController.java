package delta.games.lotro.gui.character.status.reputation;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AbstractAreaController;
import delta.common.ui.swing.area.AreaController;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a set of gadgets to edit a reputation status.
 * @author DAM
 */
public class FactionStatusEditionGagdgetsController extends AbstractAreaController
{
  private Faction _faction;
  private JLabel _label;
  private JProgressBar _bar;
  private JButton _minus;
  private JButton _plus;
  private JButton _edit;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param faction Faction to use.
   */
  public FactionStatusEditionGagdgetsController(AreaController parent, Faction faction)
  {
    super(parent);
    _faction=faction;
    // Label
    String rawName=_faction.getName();
    String name=ContextRendering.render(this,rawName);
    _label=GuiFactory.buildLabel(name);
    // Bar
    FactionLevel[] levels=_faction.getLevels();
    int min=levels[0].getTier();
    int max=levels[levels.length-1].getTier();
    _bar=new JProgressBar(SwingConstants.HORIZONTAL,min,max);
    _bar.setBackground(GuiFactory.getBackgroundColor());
    _bar.setBorderPainted(true);
    _bar.setStringPainted(true);
    _bar.setPreferredSize(new Dimension(200,25));
    // Buttons
    _minus=GuiFactory.buildIconButton("/resources/gui/icons/button_minus.png");
    _plus=GuiFactory.buildIconButton("/resources/gui/icons/button_plus.png");
    _edit=GuiFactory.buildButton("Edit...");
  }

  /**
   * Get the managed faction.
   * @return the managed faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Get the managed label.
   * @return the managed label.
   */
  public JLabel getLabel()
  {
    return _label;
  }

  /**
   * Get the managed progress bar.
   * @return the managed progress bar.
   */
  public JProgressBar getBar()
  {
    return _bar;
  }

  /**
   * Get the minus button.
   * @return the minus button.
   */
  public JButton getMinusButton()
  {
    return _minus;
  }

  /**
   * Get the plus button.
   * @return the plus button.
   */
  public JButton getPlusButton()
  {
    return _plus;
  }

  /**
   * Get the edit button.
   * @return the edit button.
   */
  public JButton getEditButton()
  {
    return _edit;
  }

  /**
   * Set the faction status.
   * @param status Status to set.
   */
  public void setFactionStatus(FactionStatus status)
  {
    FactionLevel level=(status!=null)?status.getFactionLevel():null;
    if (level==null)
    {
      _bar.setBackground(Color.WHITE);
      _bar.setForeground(Color.GRAY);
      _bar.setString("-");
      _bar.setValue(0);
      _bar.setMinimum(0);
      _bar.setMaximum(0);
    }
    else
    {
      Faction faction=status.getFaction();
      Integer reputation=status.getReputation();
      int reputationValue=(reputation!=null)?reputation.intValue():level.getRequiredReputation();
      FactionLevel initialLevel=faction.getLevelByTier(faction.getInitialTier());
      int initialReputationValue=initialLevel.getRequiredReputation();
      int min=0;
      int max=0;
      int value=reputationValue;
      if (reputationValue<initialReputationValue)
      {
        FactionLevel minLevel=faction.getLevelByTier(faction.getLowestTier());
        min=minLevel.getRequiredReputation();
        max=initialLevel.getRequiredReputation();
        _bar.setBackground(Color.RED);
        _bar.setForeground(Color.WHITE);
      }
      else
      {
        min=initialReputationValue;
        FactionLevel maxLevel=faction.getLevelByTier(faction.getHighestTier());
        max=maxLevel.getRequiredReputation();
        _bar.setBackground(Color.WHITE);
        _bar.setForeground(Color.BLUE);
      }
      _bar.setMinimum(min);
      _bar.setMaximum(max);
      _bar.setValue(value);
      String tierName=level.getName();
      String label=ContextRendering.render(this,tierName);
      int diffReputation=reputationValue-level.getRequiredReputation();
      if (diffReputation>0)
      {
        label=label+" +"+diffReputation;
      }
      _bar.setString(label);
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _faction=null;
    _label=null;
    _bar=null;
    _plus=null;
    _minus=null;
    _edit=null;
  }
}
