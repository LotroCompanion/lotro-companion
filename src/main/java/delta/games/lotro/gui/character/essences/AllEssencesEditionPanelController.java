package delta.games.lotro.gui.character.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.gear.CharacterGear;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.gui.lore.items.essences.EssenceUpdatedListener;
import delta.games.lotro.gui.lore.items.essences.SimpleSingleEssenceEditionController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.essences.EssencesSet;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a panel to edit all the essences of a character.
 * @author DAM
 */
public class AllEssencesEditionPanelController implements EssenceUpdatedListener
{
  // Data
  private CharacterData _toon;
  // Controllers
  private WindowController _parent;
  private List<SingleItemEssencesEditionController> _editors;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Managed toon.
   */
  public AllEssencesEditionPanelController(WindowController parent, CharacterData toon)
  {
    _toon=toon;
    _parent=parent;
    _editors=new ArrayList<SingleItemEssencesEditionController>();
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    update();
  }

  private void initEditors()
  {
    CharacterGear equipment=_toon.getEquipment();
    for(GearSlot slot : GearSlot.getAll())
    {
      SingleItemEssencesEditionController controller=new SingleItemEssencesEditionController(_parent,_toon.getSummary(),slot);
      ItemInstance<? extends Item> item=equipment.getItemForSlot(slot);
      controller.setItem(item);
      _editors.add(controller);
    }
  }

  /**
   * Update UI.
   */
  public void update()
  {
    clearEditors();
    initEditors();
    _panel.removeAll();
    int nbEssencesColumns=getEssencesCount();
    // Headers
    int nbColumns=nbEssencesColumns+2;
    int columnIndex=0;
    for(int i=0;i<nbColumns;i++)
    {
      int columnSpan=(i>1)?3:1;
      GridBagConstraints c=new GridBagConstraints(columnIndex,0,columnSpan,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      String text="";
      if (i==1) text="Name"; // I18n
      else if (i>1) text="Slot #"+(i-1); // I18n
      JLabel label=GuiFactory.buildLabel(text);
      _panel.add(label,c);
      columnIndex+=columnSpan;
    }

    // Item lines
    int rowIndex=1;
    for(SingleItemEssencesEditionController controller : _editors)
    {
      columnIndex=0;
      // Icon
      JLabel icon=controller.getItemIcon();
      GridBagConstraints c=new GridBagConstraints(columnIndex,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,0),0,0);
      _panel.add(icon,c);
      columnIndex++;
      // Label
      JPanel label=controller.getItemLabel();
      c=new GridBagConstraints(columnIndex,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,0,10),0,0);
      _panel.add(label,c);
      columnIndex++;
      // Essences
      List<SimpleSingleEssenceEditionController> essenceEditors=controller.getEssenceControllers();
      int nbEssences=essenceEditors.size();
      for(int i=0;i<nbEssencesColumns;i++)
      {
        if (i<nbEssences)
        {
          SimpleSingleEssenceEditionController essenceEditor=essenceEditors.get(i);
          essenceEditor.setListener(this);
          JButton essenceButton=essenceEditor.getEssenceButton();
          c=new GridBagConstraints(columnIndex,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
          _panel.add(essenceButton,c);
          JPanel essenceLabel=essenceEditor.getEssenceNameLabel();
          c=new GridBagConstraints(columnIndex+1,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
          _panel.add(essenceLabel,c);
          JButton deleteButton=essenceEditor.getDeleteButton();
          c=new GridBagConstraints(columnIndex+2,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
          _panel.add(deleteButton,c);
        }
        columnIndex+=3;
      }
      rowIndex++;
    }
  }

  private int getEssencesCount()
  {
    int nbEssences=0;
    for(SingleItemEssencesEditionController controller : _editors)
    {
      List<SimpleSingleEssenceEditionController> essenceEditors=controller.getEssenceControllers();
      int nbEssencesForItem=essenceEditors.size();
      nbEssences=Math.max(nbEssences,nbEssencesForItem);
    }
    return nbEssences;
  }

  /**
   * Invoked when an essence has been updated.
   * @param source Source controller.
   */
  @Override
  public void essenceUpdated(SimpleSingleEssenceEditionController source)
  {
    for(SingleItemEssencesEditionController itemEssences : _editors)
    {
      List<SimpleSingleEssenceEditionController> essenceCtrls=itemEssences.getEssenceControllers();
      int index=essenceCtrls.indexOf(source);
      if (index!=-1)
      {
        ItemInstance<? extends Item> itemInstance=itemEssences.getItemInstance();
        EssencesSet essences=itemInstance.getEssences();
        essences.setEssence(index,source.getEssence());
        refreshToon();
        break;
      }
    }
  }

  private void refreshToon()
  {
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_DATA_UPDATED,null,_toon);
    EventsManager.invokeEvent(event);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void clearEditors()
  {
    for(SingleItemEssencesEditionController editor : _editors)
    {
      editor.dispose();
    }
    _editors.clear();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _toon=null;
    // Controllers
    _parent=null;
    if (_editors!=null)
    {
      clearEditors();
      _editors=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
