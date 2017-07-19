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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);

    int nbEssencesColumns=getEssencesCount();
    int rowIndex=0;
    for(SingleItemEssencesEditionController controller : _editors)
    {
      c.gridy=rowIndex;
      int columnIndex=0;
      // Label
      JLabel label=controller.getItemLabel();
      c.anchor=GridBagConstraints.CENTER;
      c.gridx=columnIndex;
      panel.add(label,c);
      columnIndex++;
      // Essences
      List<SingleEssenceEditionController> essenceEditors=controller.getEssenceControllers();
      int nbEssences=essenceEditors.size();
      for(int i=0;i<nbEssencesColumns;i++)
      {
        if (i<nbEssences)
        {
          c.gridwidth=1;
          SingleEssenceEditionController essenceEditor=essenceEditors.get(i);
          essenceEditor.setListener(this);
          JButton essenceButton=essenceEditor.getEssenceButton();
          c.gridx=columnIndex;
          c.weightx=0.0;
          c.fill=GridBagConstraints.NONE;
          c.anchor=GridBagConstraints.WEST;
          panel.add(essenceButton,c);
          JLabel essenceLabel=essenceEditor.getEssenceNameLabel();
          c.gridx=columnIndex+1;
          c.weightx=1.0;
          c.fill=GridBagConstraints.HORIZONTAL;
          c.anchor=GridBagConstraints.WEST;
          panel.add(essenceLabel,c);
          JButton deleteButton=essenceEditor.getDeleteButton();
          c.gridx=columnIndex+2;
          c.weightx=0.0;
          c.fill=GridBagConstraints.NONE;
          c.anchor=GridBagConstraints.WEST;
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
