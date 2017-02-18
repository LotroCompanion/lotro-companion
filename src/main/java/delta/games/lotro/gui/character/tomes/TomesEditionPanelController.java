package delta.games.lotro.gui.character.tomes;

import java.awt.FlowLayout;
import java.awt.Font;
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

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.tomes.TomesSet;
import delta.games.lotro.gui.utils.GuiFactory;

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
    _iconsPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    buildTomesControllers(_panel);
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

  private void buildTomesControllers(JPanel panel)
  {
    TomesSet tomes=_toon.getTomes();
    for(STAT stat : TomesSet.AVAILABLE_TOMES)
    {
      TomeIconController controller=buildTomeController(tomes,stat);
      _tomeControllers.add(controller);
    }
  }

  private TomeIconController buildTomeController(TomesSet tomes, STAT stat)
  {
    Font font=_iconsPanel.getFont();
    TomeIconController controller=new TomeIconController(tomes,stat,font);
    JLabel label=controller.getLabel();
    MouseListener listener=buildLeftClickListener();
    label.addMouseListener(listener);
    return controller;
  }

  private MouseListener buildLeftClickListener()
  {
    class LeftClickListener extends MouseAdapter
    {
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
    STAT stat=TomesSet.AVAILABLE_TOMES[index];
    int currentTierIndex=tomes.getTomeRank(stat);
    currentTierIndex+=delta;
    if (currentTierIndex>TomesSet.MAX_RANK)
    {
      currentTierIndex=0;
    }
    if (currentTierIndex<0)
    {
      currentTierIndex=TomesSet.MAX_RANK;
    }
    tomes.setTomeRank(stat,currentTierIndex);
    _tomeControllers.get(index).update();
    // Broadcast toon update event...
    CharacterEvent event=new CharacterEvent(null,_toon);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
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
