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
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.gui.items.essences.EssenceUpdatedListener;
import delta.games.lotro.gui.items.essences.SingleEssenceEditionController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.essences.EssencesSet;

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
    init();
    _panel=buildPanel();
  }

  private void init()
  {
    CharacterEquipment equipment=_toon.getEquipment();
    for(EQUIMENT_SLOT slot : EQUIMENT_SLOT.values())
    {
      SingleItemEssencesEditionController controller=new SingleItemEssencesEditionController(_parent,slot);
      Item item=equipment.getItemForSlot(slot);
      controller.setItem(item);
      _editors.add(controller);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());

    int nbEssencesColumns=getEssencesCount();
    // Headers
    int nbColumns=nbEssencesColumns+2;
    int columnIndex=0;
    for(int i=0;i<nbColumns;i++)
    {
      int columnSpan=(i>1)?3:1;
      GridBagConstraints c=new GridBagConstraints(columnIndex,0,columnSpan,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      String text="";
      if (i==1) text="Name";
      else if (i>1) text="Slot #"+(i-1);
      JLabel label=GuiFactory.buildLabel(text);
      panel.add(label,c);
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
      panel.add(icon,c);
      columnIndex++;
      // Label
      JLabel label=controller.getItemLabel();
      c=new GridBagConstraints(columnIndex,rowIndex,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,0,10),0,0);
      panel.add(label,c);
      columnIndex++;
      // Essences
      List<SingleEssenceEditionController> essenceEditors=controller.getEssenceControllers();
      int nbEssences=essenceEditors.size();
      for(int i=0;i<nbEssencesColumns;i++)
      {
        if (i<nbEssences)
        {
          SingleEssenceEditionController essenceEditor=essenceEditors.get(i);
          essenceEditor.setListener(this);
          JButton essenceButton=essenceEditor.getEssenceButton();
          c=new GridBagConstraints(columnIndex,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
          panel.add(essenceButton,c);
          JLabel essenceLabel=essenceEditor.getEssenceNameLabel();
          c=new GridBagConstraints(columnIndex+1,rowIndex,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
          panel.add(essenceLabel,c);
          JButton deleteButton=essenceEditor.getDeleteButton();
          c=new GridBagConstraints(columnIndex+2,rowIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
          panel.add(deleteButton,c);
        }
        columnIndex+=3;
      }
      rowIndex++;
    }
    return panel;
  }

  private int getEssencesCount()
  {
    int nbEssences=0;
    for(SingleItemEssencesEditionController controller : _editors)
    {
      List<SingleEssenceEditionController> essenceEditors=controller.getEssenceControllers();
      int nbEssencesForItem=essenceEditors.size();
      nbEssences=Math.max(nbEssences,nbEssencesForItem);
    }
    return nbEssences;
  }

  public void essenceUpdated(SingleEssenceEditionController source)
  {
    for(SingleItemEssencesEditionController itemEssences : _editors)
    {
      List<SingleEssenceEditionController> essenceCtrls=itemEssences.getEssenceControllers();
      int index=essenceCtrls.indexOf(source);
      if (index!=-1)
      {
        Item item=itemEssences.getItem();
        EssencesSet essences=item.getEssences();
        if (essences==null)
        {
          essences=new EssencesSet(essenceCtrls.size());
          item.setEssences(essences);
        }
        essences.setEssence(index,source.getEssence());
        refreshToon();
        break;
      }
    }
  }

  private void refreshToon()
  {
    CharacterEvent event=new CharacterEvent(null,_toon);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_DATA_UPDATED,event);
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
      for(SingleItemEssencesEditionController editor : _editors)
      {
        editor.dispose();
      }
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
