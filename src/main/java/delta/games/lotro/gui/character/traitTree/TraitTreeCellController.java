package delta.games.lotro.gui.character.traitTree;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.icons.IconWithText;
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
  private IconWithText _traitIcon;
  private ImageIcon _grayedTraitIcon;
  private int _rank;
  private boolean _enabled;

  /**
   * Constructor.
   * @param cellId Identifier of the managed cell.
   * @param trait Associated trait.
   */
  public TraitTreeCellController(String cellId, TraitDescription trait)
  {
    _cellId=cellId;
    _trait=trait;
    ImageIcon traitIcon=LotroIconsManager.getTraitIcon(trait.getIconId());
    _traitIcon=new IconWithText(traitIcon,"",Color.WHITE);
    Image grayImage=GrayFilter.createDisabledImage(traitIcon.getImage());
    _grayedTraitIcon=new ImageIcon(grayImage);
    _button=new JButton(_traitIcon);
    _button.setBorderPainted(false);
    _button.setMargin(new Insets(0,0,0,0));
    _button.setToolTipText(buildTraitTooltip());
    _rank=-1;
    _enabled=true;
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
   * Get the managed trait.
   * @return a trait.
   */
  public TraitDescription getTrait()
  {
    return _trait;
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
   * Set the rank to display.
   * @param rank Rank to set.
   */
  public void setRank(int rank)
  {
    if (rank!=_rank)
    {
      _rank=rank;
      String text=String.valueOf(rank);
      _traitIcon.setText(text);
      _button.repaint();
    }
  }

  /**
   * Set the 'enabled' state.
   * @param enabled State to set.
   */
  public void setEnabled(boolean enabled)
  {
    if (_enabled!=enabled)
    {
      _enabled=enabled;
      if (enabled)
      {
        _button.setIcon(_traitIcon);
      }
      else
      {
        _button.setIcon(_grayedTraitIcon);
      }
      _button.repaint();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _cellId=null;
    _trait=null;
    _button=null;
    _traitIcon=null;
    _grayedTraitIcon=null;
  }
}
