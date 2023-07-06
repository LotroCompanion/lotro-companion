package delta.games.lotro.gui.character.cosmetics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.cosmetics.Outfit;
import delta.games.lotro.character.cosmetics.OutfitsManager;

/**
 * Controller for a panel to display the outfits of a character.
 * @author DAM
 */
public class OutfitsDisplayPanelController
{
  // Controllers
  private WindowController _parent;
  private List<OutfitPanelController> _outfitPanels;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public OutfitsDisplayPanelController(WindowController parent)
  {
    _parent=parent;
    _outfitPanels=new ArrayList<OutfitPanelController>();
    _panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update the displayed contents.
   * @param outfitsMgr Data to show.
   */
  public void updateContents(OutfitsManager outfitsMgr)
  {
    _panel.removeAll();
    disposeOutfitPanels();
    int x=0;
    for(Integer index : outfitsMgr.getOutfitIndexes())
    {
      Outfit outfit=outfitsMgr.getOutfit(index.intValue());
      OutfitPanelController outfitPanelCtrl=new OutfitPanelController(_parent,outfit);
      _outfitPanels.add(outfitPanelCtrl);
      JPanel outfitPanel=outfitPanelCtrl.getPanel();
      String title=getTitleForOutfit(index.intValue());
      outfitPanel.setBorder(GuiFactory.buildTitledBorder(title));
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(outfitPanel,c);
      x++;
    }
  }

  private String getTitleForOutfit(int index)
  {
    if (index==0) return "Equipment"; // I18n
    return "Outfit #"+index; // I18n
  }

  private void disposeOutfitPanels()
  {
    for(OutfitPanelController ctrl : _outfitPanels)
    {
      ctrl.dispose();
    }
    _outfitPanels.clear();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    if (_outfitPanels!=null)
    {
      disposeOutfitPanels();
      _outfitPanels=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
