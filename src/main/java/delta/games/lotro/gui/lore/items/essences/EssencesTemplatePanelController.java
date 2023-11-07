package delta.games.lotro.gui.lore.items.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.essences.EssencesSlotsSetup;

/**
 * Controller for a panel to display an essences slot setup.
 * @author DAM
 */
public class EssencesTemplatePanelController extends AbstractPanelController
{
  private List<JButton> _icons;

  /**
   * Constructor.
   * @param setup Essences slots setup.
   */
  public EssencesTemplatePanelController(EssencesSlotsSetup setup)
  {
    _icons=new ArrayList<JButton>();
    int socketsCount=setup.getSocketsCount();
    for(int i=0;i<socketsCount;i++)
    {
      SocketType type=setup.getSlotType(i);
      Icon icon=LotroIconsManager.getEmptySocketIcon(type.getCode());
      JButton iconButton=GuiFactory.buildIconButton();
      iconButton.setIcon(icon);
      _icons.add(iconButton);
      String tooltip=type.getLabel();
      iconButton.setToolTipText(tooltip);
    }
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Essence slots:"),c);
    for(int i=0;i<_icons.size();i++)
    {
      int left=(i==0)?5:0;
      c=new GridBagConstraints(i+1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,left,5,5),0,0);
      ret.add(_icons.get(i),c);
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _icons.clear();
  }
}
