package delta.games.lotro.gui.items.essences;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Panel to edit essences.
 * @author DAM
 */
public class EssencesEditionPanelController
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  // Data
  private List<Item> _essences;
  // GUI
  private JPanel _essencesPanel;
  private JPanel _panel;
  private List<SingleEssenceController> _essenceControllers;
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public EssencesEditionPanelController(WindowController parent)
  {
    _parent=parent;
    _essences=new ArrayList<Item>();
    _essenceControllers=new ArrayList<SingleEssenceController>();
    _panel=build();
  }

  private JPanel build()
  {
    JPanel wholePanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _essencesPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _essencesPanel.setOpaque(true);
    wholePanel.add(_essencesPanel,BorderLayout.CENTER);
    JButton button=GuiFactory.buildButton("New slot");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        addSlot();
      }
    };
    button.addActionListener(al);
    JPanel buttonsPanel=GuiFactory.buildBackgroundPanel(null);
    BoxLayout layout=new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS);
    buttonsPanel.setLayout(layout);
    buttonsPanel.add(button);
    wholePanel.add(buttonsPanel,BorderLayout.EAST);
    return wholePanel;
  }

  /**
   * Initialize the managed panel with the given item.
   * @param item Item to set.
   */
  public void initFromItem(Item item)
  {
    _essenceControllers.clear();
    int nbSlots=item.getEssencesCount();
    for(int i=0;i<nbSlots;i++)
    {
      Item essence=item.getEssenceAt(i);
      SingleEssenceController ctrl=new SingleEssenceController();
      _essences.add(essence);
      ctrl.setEssence(essence);
      _essenceControllers.add(ctrl);
    }
    updateUi();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void updateUi()
  {
    _essencesPanel.removeAll();
    int index=0;
    for(SingleEssenceController ctrl : _essenceControllers)
    {
      buildStatUi(_essencesPanel,ctrl,index);
      index++;
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildStatUi(JPanel panel,SingleEssenceController ctrl,int index)
  {
    GridBagConstraints c=new GridBagConstraints(0,index,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Essence icon
    JButton value=ctrl.getEssenceButton();
    panel.add(value,c);
    // Essence label
    JLabel unit=ctrl.getEssenceNameLabel();
    c=new GridBagConstraints(1,index,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(unit,c);
    // Delete button
    JButton deleteButton=ctrl.getDeleteButton();
    c=new GridBagConstraints(2,index,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    c.gridx++;
    panel.add(deleteButton,c);
  }

  private void addSlot()
  {
    SingleEssenceController ctrl=new SingleEssenceController();
    _essenceControllers.add(ctrl);
    _essences.add(null);
    updateUi();
    update();
  }

  /**
   * Update UI from the managed data.
   */
  public void update()
  {
    int index=0;
    for(Item essence : _essences)
    {
      SingleEssenceController ctrl=_essenceControllers.get(index);
      ctrl.setEssence(essence);
      index++;
    }
  }

  private void handleButtonClick(JButton button, SingleEssenceController ctrl)
  {
    if (button==ctrl.getEssenceButton())
    {
      Item essence=EssenceChoice.chooseEssence(_parent);
      if (essence!=null)
      {
        int index=_essenceControllers.indexOf(ctrl);
        _essences.set(index,essence);
        update();
      }
    }
    else if (button==ctrl.getDeleteButton())
    {
      int index=_essenceControllers.indexOf(ctrl);
      _essences.set(index,null);
      update();
    }
  }

  /**
   * Get the selected essences.
   * @return a list of essences (with <code>null</code> items possible).
   */
  public List<Item> getEssences()
  {
    return _essences;
  }

  /**
   * Controller for the UI items of a single essence.
   * @author DAM
   */
  private class SingleEssenceController
  {
    private JButton _essenceIconButton;
    private JLabel _essenceName;
    private JButton _deleteButton;

    /**
     * Constructor.
     */
    public SingleEssenceController()
    {
      // Button
      _essenceIconButton=GuiFactory.buildButton("");
      _essenceIconButton.setOpaque(false);
      _essenceIconButton.setBorderPainted(false);
      _essenceIconButton.setMargin(new Insets(0,0,0,0));
      ActionListener listener=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          handleButtonClick((JButton)e.getSource(),SingleEssenceController.this);
        }
      };
      _essenceIconButton.addActionListener(listener);
      // Label
      _essenceName=GuiFactory.buildLabel("");
      // Delete button
      ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
      _deleteButton=GuiFactory.buildButton("");
      _deleteButton.setIcon(icon);
      _deleteButton.setMargin(new Insets(0,0,0,0));
      _deleteButton.setContentAreaFilled(false);
      _deleteButton.setBorderPainted(false);
      _deleteButton.addActionListener(listener);
    }

    private void setEssence(Item essence)
    {
      // Set essence icon
      ImageIcon icon=null;
      if (essence!=null)
      {
        String iconId=essence.getProperty(ItemPropertyNames.ICON_ID);
        String backgroundIconId=essence.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
        icon=IconsManager.getItemIcon(iconId,backgroundIconId);
      }
      else
      {
        icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
      _essenceIconButton.setIcon(icon);
      // Text
      String text=(essence!=null)?essence.getName():"";
      _essenceName.setText(text);
    }

    /**
     * Get the managed essence button.
     * @return the managed essence button.
     */
    public JButton getEssenceButton()
    {
      return _essenceIconButton;
    }

    /**
     * Get the label for the essence.
     * @return a label.
     */
    public JLabel getEssenceNameLabel()
    {
      return _essenceName;
    }

    /**
     * Get the delete button associated with this essence.
     * @return a button.
     */
    public JButton getDeleteButton()
    {
      return _deleteButton;
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_essencesPanel!=null)
    {
      _essencesPanel.removeAll();
      _essencesPanel=null;
    }
    _essenceControllers.clear();
  }
}
