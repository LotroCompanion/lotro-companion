package delta.games.lotro.gui.kinship;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.kinship.Kinship;

/**
 * Controller for a "kinship" window.
 * @author DAM
 */
public class KinshipWindowController extends DefaultWindowController
{
  private KinshipSummaryDisplayPanelController _summaryController;
  private Kinship _kinship;
  private KinshipPanelController _detailsPanel;

  /**
   * Constructor.
   * @param kinship Managed kinship.
   */
  public KinshipWindowController(Kinship kinship)
  {
    _kinship=kinship;
    _detailsPanel=new KinshipPanelController(this,_kinship);
    _summaryController=new KinshipSummaryDisplayPanelController(_kinship);
  }

  /**
   * Get the window identifier for a given kinship.
   * @param kinshipName Kinship name.
   * @return A window identifier.
   */
  public static String getIdentifier(String kinshipName)
  {
    String id="KINSHIP#"+kinshipName;
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    panel.add(summaryPanel,BorderLayout.NORTH);
    // Details panel
    panel.add(_detailsPanel.getPanel(),BorderLayout.CENTER);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_kinship.getName();
    String title="Kinship: "+name; // I18n
    frame.setTitle(title);
    frame.setMinimumSize(new Dimension(650,350));
    frame.setSize(850,600);
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String kinshipName=_kinship.getName();
    String id=getIdentifier(kinshipName);
    return id;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_detailsPanel!=null)
    {
      _detailsPanel.dispose();
      _detailsPanel=null;
    }
  }
}
