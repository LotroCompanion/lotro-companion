package delta.games.lotro.gui.character.traitTree;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.icons.IconWithText;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.StatsComputer;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.StatDisplayUtils;

/**
 * Controller for the icon of an equipment slot.
 * @author DAM
 */
public class TraitTreeCellController
{
  private CharacterData _toon;
  private String _cellId;
  private TraitDescription _trait;
  private JButton _button;
  private IconWithText _traitIcon;
  private ImageIcon _grayedTraitIcon;
  private int _rank;
  private boolean _enabled;

  /**
   * Constructor.
   * @param toon Character data.
   * @param cellId Identifier of the managed cell.
   * @param trait Associated trait.
   */
  public TraitTreeCellController(CharacterData toon, String cellId, TraitDescription trait)
  {
    _toon=toon;
    _cellId=cellId;
    _button=new JButton();
    _button.setBorderPainted(false);
    _button.setMargin(new Insets(0,0,0,0));
    _button.setActionCommand(cellId);
    setTrait(trait);
    _rank=-1;
    _enabled=true;
  }

  private String buildTraitTooltip(int rank)
  {
    BasicStatsSet stats;
    if (rank>0)
    {
      stats=StatsComputer.getStats(_toon,_trait,rank);
    }
    else
    {
      stats=new BasicStatsSet();
    }
    String html=StatDisplayUtils.buildToolTip(_trait.getName(),stats);
    return html;
  }

  /**
   * Get the identifier of the managed cell.
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
   * Set the displayed trait.
   * @param trait
   */
  public void setTrait(TraitDescription trait)
  {
    if (_trait!=trait)
    {
      _trait=trait;
      _rank=-1;
      ImageIcon traitIcon=LotroIconsManager.getTraitIcon(trait.getIconId());
      _traitIcon=new IconWithText(traitIcon,"",Color.WHITE);
      Image grayImage=GrayFilter.createDisabledImage(traitIcon.getImage());
      _grayedTraitIcon=new ImageIcon(grayImage);
      _button.setIcon(_traitIcon);
      _button.setToolTipText(buildTraitTooltip(_rank));
      _enabled=true;
    }
  }

  /**
   * Get the current rank.
   * @return the current rank.
   */
  public int getRank()
  {
    return _rank;
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
      int maxRank=_trait.getTiersCount();
      String text=(rank==maxRank)?String.valueOf(rank):rank+"/"+maxRank;
      _traitIcon.setText(text);
      _button.setToolTipText(buildTraitTooltip(_rank));
      _button.repaint();
    }
  }

  /**
   * Indicates if this cell controller is enabled or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isEnabled()
  {
    return _enabled;
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
        _button.setToolTipText(buildTraitTooltip(_rank));
      }
      else
      {
        _button.setIcon(_grayedTraitIcon);
        _button.setToolTipText(buildTraitTooltip(0));
      }
      _button.repaint();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _toon=null;
    _cellId=null;
    _trait=null;
    _button=null;
    _traitIcon=null;
    _grayedTraitIcon=null;
  }
}
