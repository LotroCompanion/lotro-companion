package delta.games.lotro.gui.character.status.effects;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.effects.EffectInstance;
import delta.games.lotro.gui.common.effects.table.EffectInstancesTableBuilder;

/**
 * Controller for a "character effects" edition panel.
 * @author DAM
 */
public class CharacterEffectsEditionPanelController extends AbstractPanelController implements ActionListener
{
  private static final String ADD_EFFECT_ID="addEffect";
  private static final String REMOVE_EFFECT_ID="removeEffect";

  // Data
  private List<EffectInstance> _effects;
  // Controllers
  private GenericTableController<EffectInstance> _effectsTable;
  private ToolbarController _toolbar;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param effects Effects to edit.
   */
  public CharacterEffectsEditionPanelController(WindowController parent, List<EffectInstance> effects)
  {
    super(parent);
    _effects=effects;
    setPanel(buildPanel());
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (ADD_EFFECT_ID.equals(command))
    {
      addEffect();
    }
    else if (REMOVE_EFFECT_ID.equals(command))
    {
      removeEffect();
    }
    else if (GenericTableController.DOUBLE_CLICK.equals(command))
    {
      EffectInstance data=(EffectInstance)e.getSource();
      editEffect(data);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _effectsTable=EffectInstancesTableBuilder.buildTable(_effects);
    _effectsTable.addActionListener(this);
    JTable table=_effectsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    ret.setBorder(GuiFactory.buildTitledBorder("Effects")); // I18n
    return ret;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(ADD_EFFECT_ID,newIconPath,ADD_EFFECT_ID,"Add a new effect...","New");
    model.addToolbarIconItem(newIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_EFFECT_ID,deleteIconPath,REMOVE_EFFECT_ID,"Remove the selected effect...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    // Register action listener
    controller.addActionListener(this);
    return controller;
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private void addEffect()
  {
    EffectInstance newInstance=new EffectInstance();
    EffectInstanceEditionWindowController editor=new EffectInstanceEditionWindowController(getWindowController(),newInstance);
    EffectInstance editedInstance=editor.editModal();
    if (editedInstance!=null)
    {
      _effects.add(editedInstance);
      _effectsTable.refresh();
    }
  }

  private void editEffect(EffectInstance effectInstance)
  {
    EffectInstance toEdit=new EffectInstance(effectInstance);
    EffectInstanceEditionWindowController editor=new EffectInstanceEditionWindowController(getWindowController(),toEdit);
    EffectInstance editedInstance=editor.editModal();
    if (editedInstance!=null)
    {
      effectInstance.copyFrom(editedInstance);
      _effectsTable.refresh(effectInstance);
    }
  }

  private void removeEffect()
  {
    EffectInstance data=_effectsTable.getSelectedItem();
    if (data==null)
    {
      return;
    }
    _effects.remove(data);
    _effectsTable.refresh();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_effectsTable!=null)
    {
      _effectsTable.dispose();
      _effectsTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
  }
}
