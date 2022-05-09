package MetTap;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
public class AddNewProperty   extends JFrame implements ActionListener,ItemListener,ListSelectionListener
{
	Vector vector=new Vector();	
	File file=new File("User-defined Properties.txt"); 
	List<String> propertyList = Get.getContents(file); 
	JPanel j = new JPanel();
	JTextField jf1=new JTextField();
	JTextField jf2=new JTextField();
	JTextField jf3=new JTextField();
	JList jls=new JList();
	
	JButton btn[]= new JButton[5];
	JLabel jlb=new JLabel("~");
	JComboBox type = new JComboBox();
	String s1[]={"F","G"};
	String s2[]={"/\\","\\/","!","—>","ADD"};
	String s="";
	String st="";
	AddNewProperty()
		{			
		
			setBounds(500,200,480,280);
			setTitle("Add User-Defined Properties");
			setResizable(false);
			j.setLayout(null);  
			
			int i;
			for(i=0;i<s1.length;i++)
			    type.addItem(s1[i]);
			for(i=0;i<s2.length;i++)
			{
				btn[i]=new JButton(s2[i]);
				btn[i].addActionListener(this);
			}
			
			j.add(type);
			type.setBounds(35, 10, 40, 25);
			
		    j.add(jf1);
		    jf1.setBounds(80, 10, 30, 25);
		    
		    j.add(jlb); //"~"
		    jlb.setBounds(115, 10, 10, 25);
		    
		    j.add(jf2);
		    jf2.setBounds(130, 10, 30, 25);	
		    
		    j.add(jf3);
		    jf3.setBounds(165, 10, 150, 25);    		    
		    
		    jls.setBounds(15, 60, 320, 150);
			jls.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
			jls.addListSelectionListener(this);
			
			j.add(btn[0]);
			btn[0].setBounds(350, 10, 80, 30);
			
			j.add(btn[1]);
			btn[1].setBounds(350, 53, 80, 30);
			
			j.add(btn[2]);
			btn[2].setBounds(350, 97, 80, 30);
			
			j.add(btn[3]);
			btn[3].setBounds(350, 142, 80, 30);
			
			j.add(btn[4]);
			btn[4].setBounds(350, 187, 80, 30);
			
			j.add(jls);			
			
		    add(j);				
			
			setVisible(true);  
		}
			public void actionPerformed(ActionEvent e)
			{
				
				if(e.getSource()==btn[0])
				{				
					String st1=(String)type.getSelectedItem();
					String st2=jf1.getText();
					String st3=jf2.getText();
					String st4=jf3.getText();
					if(st2.equals(""))
					{
						s=s+st1+st2+st3+st4+"/\\";
						st=st+"("+st1+st2+st3+st4+")"+"/\\";
					}
					else
					{
						s=s+st1+"_"+"["+st2+","+st3+"] "+st4+"/\\";
						st=st+"("+st1+"_"+"["+st2+","+st3+"] "+st4+")"+"/\\";
					}
				}
				if(e.getSource()==btn[1])
				{
					String st1=(String)type.getSelectedItem();
					String st2=jf1.getText();
					String st3=jf2.getText();
					String st4=jf3.getText();
					if(st2.equals(""))
					{
						s=s+st1+st2+st3+st4+"\\/";
						st=st+"("+st1+st2+st3+st4+")"+"\\/";
					}
					else
					{
						s=s+st1+"_"+"["+st2+","+st3+"] "+st4+"\\/";
						st=st+"("+st1+"_"+"["+st2+","+st3+"] "+st4+")"+"\\/";
					}
					
				}
				if(e.getSource()==btn[2])
				{
					String st1=(String)type.getSelectedItem();
					String st2=jf1.getText();
					String st3=jf2.getText();
					String st4=jf3.getText();
					if(st2.equals(""))
					{
						s=s+st1+st2+st3+st4+"!";
						st=st+"("+st1+st2+st3+st4+")"+"!";
					}
					else
					{
						s=s+st1+"_"+"["+st2+","+st3+"] "+st4+"!";
						st=st+"("+st1+"_"+"["+st2+","+st3+"] "+st4+")"+"!";
					}
				}
				if(e.getSource()==btn[3])
				{
					String st1=(String)type.getSelectedItem();
					String st2=jf1.getText();
					String st3=jf2.getText();
					String st4=jf3.getText();
					if(st2.equals(""))
					{
						s=s+st1+st2+st3+st4+"—>";
						st=st+"("+st1+st2+st3+st4+"))"+"—>";
					}
					else
					{
						s=s+st1+"_"+"["+st2+","+st3+"] "+st4+"—>";
						st=st+"("+st1+"_"+"["+st2+","+st3+"] "+st4+")"+"—>";
					}
				}
				if(e.getSource()==btn[4])
				{
					String st1=(String)type.getSelectedItem();
					String st2=jf1.getText();
					String st3=jf2.getText();
					String st4=jf3.getText();
					if(st2.equals(""))
					{
						s=s+st1+st2+st3+st4;
						st=st+"("+st1+st2+st3+st4+")";
					}
					else
					{
						s=s+st1+"_"+"["+st2+","+st3+"] "+st4;
						st=st+"("+st1+"_"+"["+st2+","+st3+"] "+st4+")";
					}
					vector.add(s);
					jls.setListData(vector);
					propertyList.add(st);
					Put.putContents(propertyList, file);
					s="";
					st="";
				}
				
			}
			public void itemStateChanged(ItemEvent e) 
			{
				
			}
			public void valueChanged(ListSelectionEvent e) 
			{				
				
			}
	public static void main(String args[])
	{
		new AddNewProperty ();
	}
}

