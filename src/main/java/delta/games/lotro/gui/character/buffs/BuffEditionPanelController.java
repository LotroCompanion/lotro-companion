package delta.games.lotro.gui.character.buffs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.ListOrderedMap;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffInstance;
import delta.games.lotro.character.stats.buffs.BuffRegistry;
import delta.games.lotro.character.stats.buffs.BuffsManager;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.character.status.traits.TraitsStatus;
import delta.games.lotro.gui.character.traitTree.TraitTreeEditionDialog;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a panel to edit buffs.
 * @author DAM
 */
public class BuffEditionPanelController implements ActionListener
{
  private static final String REMOVE_COMMAND="remove";

  // Data
  private CharacterData _toon;
  // Controllers
  private WindowController _parentWindow;
  private ListOrderedMap<BuffIconController> _buffControllers;
  // UI
  private JPanel _panel;
  private JPanel _iconsPanel;
  private JPopupMenu _contextMenu;
  private JLabel _transparentIcon;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param character Targeted character.
   */
  public BuffEditionPanelController(WindowController parent, CharacterData character)
  {
    _parentWindow=parent;
    _toon=character;
    _buffControllers=new ListOrderedMap<BuffIconController>();
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
    JButton traitTreeButton=GuiFactory.buildButton("Trait tree..."); // I18n
    _panel.add(traitTreeButton);
    ActionListener alTraitTree=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editTraitTree();
      }
    };
    traitTreeButton.addActionListener(alTraitTree);
    _iconsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    buildBuffControllers(_panel);
    _panel.add(_iconsPanel);
    JButton button=GuiFactory.buildButton("Add..."); // I18n
    _panel.add(button);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        add();
      }
    };
    button.addActionListener(al);
    _contextMenu=buildContextualMenu();
    _transparentIcon=GuiFactory.buildTransparentIconlabel(32);
  }

  private void updateIconsPanel()
  {
    _iconsPanel.removeAll();
    for(BuffIconController controller : _buffControllers.values())
    {
      controller.update();
      JLabel label=controller.getLabel();
      _iconsPanel.add(label);
    }
    if (_buffControllers.size()==0)
    {
      _iconsPanel.add(_transparentIcon);
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildBuffControllers(JPanel panel)
  {
    BuffsManager buffs=_toon.getBuffs();
    int nbBuffs=buffs.getBuffsCount();
    for(int i=0;i<nbBuffs;i++)
    {
      BuffInstance buffInstance=buffs.getBuffAt(i);
      String buffId=buffInstance.getBuff().getId();
      BuffIconController controller=buildBuffController(buffInstance);
      _buffControllers.put(buffId,controller);
    }
  }

  private BuffIconController buildBuffController(BuffInstance buff)
  {
    BuffIconController controller=new BuffIconController(buff,_toon);
    JLabel label=controller.getLabel();
    label.setName(buff.getBuff().getId());
    MouseListener popupListener=buildRightClickListener();
    label.addMouseListener(popupListener);
    MouseListener listener=buildLeftClickListener();
    label.addMouseListener(listener);
    return controller;
  }

  private JPopupMenu buildContextualMenu()
  {
    JPopupMenu popup=new JPopupMenu();
    JMenuItem remove=new JMenuItem("Remove"); // I18n
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    return popup;
  }

  private MouseListener buildRightClickListener()
  {
    class PopClickListener extends MouseAdapter
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }

      private void doPop(MouseEvent e)
      {
        _contextMenu.show(e.getComponent(),e.getX(),e.getY());
      }
    }
    return new PopClickListener();
  }

  private MouseListener buildLeftClickListener()
  {
    class LeftClickListener extends MouseAdapter
    {
      @Override
      public void mouseReleased(MouseEvent e)
      {
        if (e.getButton()==MouseEvent.BUTTON1)
        {
          // Straight click
          Object invoker=e.getSource();
          String buffId=((Component)invoker).getName();
          updateTier(buffId);
        }
      }
    }
    return new LeftClickListener();
  }

  /**
   * Callback for managed commands:
   * <ul>
   * <li>Remove.
   * </ul>
   * @param event Source event.
   */
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (REMOVE_COMMAND.equals(cmd))
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      String buffId=invoker.getName();
      remove(buffId);
    }
  }

  private void editTraitTree()
  {
    TraitTreeStatus status=_toon.getTraits().getTraitTreeStatus();
    TraitTreeStatus toEdit=new TraitTreeStatus(status);
    TraitTreeEditionDialog dialog=new TraitTreeEditionDialog(_parentWindow,_toon,toEdit);
    TraitTreeStatus result=dialog.editModal();
    if (result!=null)
    {
      TraitsStatus traitsStatus=_toon.getTraits();
      TraitTreeStatus newStatus=new TraitTreeStatus(result);
      traitsStatus.setTraitTreeStatus(newStatus);
      // Broadcast toon update event...
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
      EventsManager.invokeEvent(event);
    }
  }

  private void add()
  {
    BuffsManager buffs=_toon.getBuffs();
    List<Buff> possibleBuffs=BuffRegistry.getInstance().buildBuffSelection(_toon.getSummary(),buffs);
    Buff buff=BuffChoiceWindowController.selectBuff(_parentWindow,possibleBuffs,null);
    if (buff!=null)
    {
      BuffInstance buffInstance=buff.buildInstance();
      buffs.addBuff(buffInstance);
      String buffId=buff.getId();
      BuffIconController controller=buildBuffController(buffInstance);
      _buffControllers.put(buffId,controller);
      // Broadcast toon update event...
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
      EventsManager.invokeEvent(event);
    }
  }

  /**
   * Update display.
   */
  public void update()
  {
    // Update UI
    updateIconsPanel();
  }

  private void updateTier(String buffId)
  {
    BuffsManager buffs=_toon.getBuffs();
    BuffInstance buff=buffs.getBuffById(buffId);
    List<Integer> tiers=buff.getBuff().getImpl().getTiers();
    if ((tiers!=null) && (tiers.size()>0))
    {
      Integer currentTier=buff.getTier();
      int currentTierIndex=tiers.indexOf(currentTier);
      if (currentTierIndex!=-1)
      {
        currentTierIndex++;
        if (currentTierIndex==tiers.size())
        {
          currentTierIndex=0;
        }
        buff.setTier(tiers.get(currentTierIndex));
        _buffControllers.get(buffId).update();
        // Broadcast toon update event...
        CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
        EventsManager.invokeEvent(event);
      }
    }
  }

  private void remove(String buffId)
  {
    BuffsManager buffs=_toon.getBuffs();
    buffs.removeBuff(buffId);
    _buffControllers.remove(buffId);
    // Update UI
    updateIconsPanel();
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
    if (_buffControllers!=null)
    {
      for(BuffIconController buffIconController : _buffControllers.values())
      {
        buffIconController.dispose();
      }
      _buffControllers.clear();
      _buffControllers=null;
    }
    _parentWindow=null;
    _toon=null;
  }
}
