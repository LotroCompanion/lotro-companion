package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for the UI gadgets of a title reward.
 * @author DAM
 */
public class TitleRewardGadgetsController extends RewardGadgetsController
{
  private HyperLinkController _titleLink;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param title Title.
   */
  public TitleRewardGadgetsController(WindowController parent, TitleDescription title)
  {
    super(parent);
    // Label
    _label=new LabelWithHalo();
    _label.setForeground(Color.WHITE);
    // Link
    _titleLink=TitleUiUtils.buildTitleLink(parent,title,_label);
    // Icon
    _icon=GuiFactory.buildTransparentIconlabel(32);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_titleLink!=null)
    {
      _titleLink.dispose();
      _titleLink=null;
    }
  }
}
