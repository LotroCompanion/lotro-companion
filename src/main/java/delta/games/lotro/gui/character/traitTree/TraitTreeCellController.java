package delta.games.lotro.gui.character.traitTree;

import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for the icon of an equipment slot.
 * @author DAM
 */
public class TraitTreeCellController
{
  private String _cellId;
  private TraitDescription _trait;
  private JButton _button;

  /**
   * Constructor.
   * @param cellId Identifier of the managed cell.
   * @param trait Associated trait.
   */
  public TraitTreeCellController(String cellId, TraitDescription trait)
  {
    _cellId=cellId;
    _trait=trait;
    ImageIcon icon=LotroIconsManager.getTraitIcon(trait.getIconId());
    //Image grayImage=GrayFilter.createDisabledImage(icon.getImage());
    //ImageIcon grayedIcon=new ImageIcon(grayImage);
    _button=new JButton(icon);
    _button.setBorderPainted(false);
    _button.setMargin(new Insets(0,0,0,0));
    _button.setToolTipText(buildTraitTooltip());
  }

  private String buildTraitTooltip()
  {
    StringBuilder sb=new StringBuilder();
    String name=_trait.getName();
    sb.append("Trait: ").append(name);
    return sb.toString().trim();
  }

  /**
   * Get the identififer of the managed cell.
   * @return a cell identifier.
   */
  public String getCellId()
  {
    return _cellId;
  }

  /**
   * Get the managed button.
   * @return the managed button.
   */
  public JButton getButton()
  {
    return _button;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _cellId=null;
    _trait=null;
    _button=null;
  }
}
