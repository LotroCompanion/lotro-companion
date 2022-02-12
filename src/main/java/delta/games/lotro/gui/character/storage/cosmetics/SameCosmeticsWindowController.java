package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;

/**
 * Controller for a window that shows a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=700;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="SAME_COSMETICS_WINDOW";

  // Controllers
  private SameCosmeticsPanelController _groupsPanelCtrl;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param groups Groups to show.
   */
  public SameCosmeticsWindowController(WindowController parent, List<CosmeticItemsGroup> groups)
  {
    super(parent);
    _groupsPanelCtrl=new SameCosmeticsPanelController(this,groups);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Same cosmetics");
    frame.pack();
    frame.setSize(frame.getWidth(),INITIAL_HEIGHT);
    frame.setMinimumSize(new Dimension(frame.getWidth(),MIN_HEIGHT));
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel groupsPanel=_groupsPanelCtrl.getPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(groupsPanel);
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Controllers
    if (_groupsPanelCtrl!=null)
    {
      _groupsPanelCtrl.dispose();
      _groupsPanelCtrl=null;
    }
  }
}
