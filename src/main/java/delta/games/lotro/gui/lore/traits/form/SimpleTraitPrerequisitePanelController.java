package delta.games.lotro.gui.lore.traits.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.prerequisites.SimpleTraitPrerequisite;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to show a simple trait prerequisite.
 * @author DAM
 */
public class SimpleTraitPrerequisitePanelController extends AbstractTraitPrerequisitePanelController
{
  // Controllers
  private IconLinkLabelGadgetsController _gadgets;
  // Data
  private SimpleTraitPrerequisite _prerequisite;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param prerequisite Prerequisite to show.
   */
  public SimpleTraitPrerequisitePanelController(WindowController parent, SimpleTraitPrerequisite prerequisite)
  {
    super(parent);
    _prerequisite=prerequisite;
    _gadgets=buildGadgets(parent);
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private IconLinkLabelGadgetsController buildGadgets(WindowController parent)
  {
    IconLinkLabelGadgetsController ret=null;
    Proxy<TraitDescription> proxy=_prerequisite.getTraitProxy();
    if (proxy!=null)
    {
      TraitDescription trait=proxy.getObject();
      ret=GadgetsControllersFactory.build(parent,trait);
    }
    return ret;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    JButton icon=_gadgets.getIcon().getIcon();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(icon,c);
    JLabel label=_gadgets.getLink().getLabel();
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(label,c);
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_gadgets!=null)
    {
      _gadgets.dispose();
      _gadgets=null;
    }
  }
}
