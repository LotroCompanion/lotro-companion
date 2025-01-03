package delta.games.lotro.gui.common.effects.form;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.EffectsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigationParameters;

/**
 * Factory for effect-related panels.
 * @author DAM
 */
public class EffectPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public EffectPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.EFFECT_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      Integer effectLevel=pageId.getIntParameter(NavigationParameters.EFFECT_LEVEL_PARAMETER);
      ret=buildEffectPanel(id,effectLevel);
    }
    return ret;
  }

  private EffectDisplayPanelController buildEffectPanel(int effectID, Integer effectLevel)
  {
    EffectsManager effectsMgr=EffectsManager.getInstance();
    Effect effect=effectsMgr.getEffectById(effectID);
    if (effect!=null)
    {
      int level=(effectLevel!=null)?effectLevel.intValue():1;
      EffectDisplayPanelController panel=new EffectDisplayPanelController(_parent,effect,level);
      return panel;
    }
    return null;
  }
}
