package delta.games.lotro.gui.character.tomes;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.stats.tomes.StatTomesManager;
import delta.games.lotro.character.stats.tomes.TomesSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a panel to edit tomes.
 * @author DAM
 */
public class TomesEditionPanelController
{
  // Data
  private CharacterData _toon;
  // UI
  private List<TomeIconController> _tomeControllers;
  private JPanel _panel;
  private JPanel _iconsPanel;

  /**
   * Constructor.
   * @param character Targeted character.
   */
  public TomesEditionPanelController(CharacterData character)
  {
    _toon=character;
    _tomeControllers=new ArrayList<TomeIconController>();
    build();
    updateIconsPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void build()
  {
    _panel=GuiFactory.buildPanel(new FlowLayout());
    _iconsPanel=GuiFactory.buildPanel(new GridBagLayout());
    buildTomesControllers();
    _panel.add(_iconsPanel);
  }

  private void updateIconsPanel()
  {
    _iconsPanel.removeAll();
    int index=0;
    for(TomeIconController controller : _tomeControllers)
    {
      JLabel label=controller.getLabel();
      String statName=controller.getStat().getName();
      JLabel statLabel=GuiFactory.buildLabel(statName);
      int left=(index>0)?3:0;
      GridBagConstraints c=new GridBagConstraints(index,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,left,0,0),0,0);
      _iconsPanel.add(statLabel,c);
      c.gridy++;
      _iconsPanel.add(label,c);
      index++;
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildTomesControllers()
  {
    TomesSet tomes=_toon.getTomes();
    for(StatDescription stat : StatTomesManager.getInstance().getStats())
    {
      TomeIconController controller=buildTomeController(tomes,stat);
      _tomeControllers.add(controller);
    }
  }

  private TomeIconController buildTomeController(TomesSet tomes, StatDescription stat)
  {
    TomeIconController controller=new TomeIconController(tomes,stat);
    JLabel label=controller.getLabel();
    MouseListener listener=buildLeftClickListener();
    label.addMouseListener(listener);
    return controller;
  }

  private MouseListener buildLeftClickListener()
  {
    class LeftClickListener extends MouseAdapter
    {
      @Override
      public void mouseReleased(MouseEvent e)
      {
        int delta=0;
        if (e.getButton()==MouseEvent.BUTTON1)
        {
          delta=1;
        }
        else if (e.getButton()==MouseEvent.BUTTON3)
        {
          delta=-1;
        }
        if (delta!=0)
        {
          updateTier(e,delta);
        }
      }
    }
    return new LeftClickListener();
  }

  private void updateTier(MouseEvent e, int delta)
  {
    // Straight click
    Object invoker=e.getSource();
    int index=getIndex(invoker);
    if (index!=-1)
    {
      // Update tier
      updateTier(index,delta);
    }
  }

  private int getIndex(Object invoker)
  {
    int index=0;
    for(TomeIconController controller : _tomeControllers)
    {
      JLabel label=controller.getLabel();
      if (label==invoker)
      {
        return index;
      }
      index++;
    }
    return -1;
  }

  private void updateTier(int index,int delta)
  {
    TomesSet tomes=_toon.getTomes();
    StatDescription stat=_tomeControllers.get(index).getStat();
    int currentTierIndex=tomes.getTomeRank(stat);
    currentTierIndex+=delta;
    int maxRank=StatTomesManager.getInstance().getNbOfRanks(stat);
    if (currentTierIndex>maxRank)
    {
      currentTierIndex=0;
    }
    if (currentTierIndex<0)
    {
      currentTierIndex=maxRank;
    }
    tomes.setTomeRank(stat,currentTierIndex);
    _tomeControllers.get(index).update();
    // Broadcast toon update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
    EventsManager.invokeEvent(event);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_iconsPanel!=null)
    {
      _iconsPanel.removeAll();
      _iconsPanel=null;
    }
    _tomeControllers.clear();
    _toon=null;
  }
}
