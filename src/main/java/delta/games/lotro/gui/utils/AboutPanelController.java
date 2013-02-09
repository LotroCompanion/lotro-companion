package delta.games.lotro.gui.utils;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Controller for a 'about' panel.
 * @author DAM
 */
public class AboutPanelController
{
  private JPanel _panel;

  /**
   * Constructor.
   */
  public AboutPanelController()
  {
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c;

    int x=0;
    int y=0;
    
    // Icon (if any)
    String iconPath="/resources/gui/ring/ring48.png";
    Image icon=IconsManager.getImage(iconPath);
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,20,10,5),0,0);
    ImageIcon imageIcon=new ImageIcon(icon);
    JLabel lbIcon=new JLabel(imageIcon);
    panel.add(lbIcon,c);
    x++;
    
    // Project name
    JLabel lbName=new JLabel("LotRO Companion");
    lbName.setFont(lbName.getFont().deriveFont(Font.BOLD,36));
    c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,20),0,0);
    panel.add(lbName,c);
    y++;

    // Project version
    String projectVersion="Version 3.0 (14/02/2013)";
    JLabel lbVersion=new JLabel(projectVersion);
    lbVersion.setFont(lbVersion.getFont().deriveFont(Font.BOLD,24));
    c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,5),0,0);
    panel.add(lbVersion,c);
    y++;
    
    // Project contact
    String protectEmail="lotrocompanion@gmail.com";
    JLabel lbEmail=new JLabel(protectEmail);
    lbEmail.setFont(lbVersion.getFont().deriveFont(Font.BOLD,16));
    c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,5),0,0);
    panel.add(lbEmail,c);
    y++;
    
    // Project contributors
    String contributors=getContribText();
    JLabel lbContrib=new JLabel(contributors);
    lbContrib.setFont(lbVersion.getFont().deriveFont(Font.PLAIN,16));
    c=new GridBagConstraints(0,y,2,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,5,5,20),0,0);
    panel.add(lbContrib,c);
    y++;
    
    return panel;
  }

  private String getContribText()
  {
    String contributors="<html><ul><li>Glumlug: design, coding, build, advertisement" +
    		"<li>Allyriel: general support, ideas, beta testing,<p>" +
    		"  advertisement, warbands screenshots" +
    		"<li>Serilis: general support, ideas, beta testing, forum warden" +
        "<li>Warthal: general support, ideas, beta-testing,<p>" +
    		"  warbands graphics" +
        "</ul></html>";
    return contributors;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
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
  }
}
