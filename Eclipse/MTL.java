package MetTap;
import java.io.File;
import java.util.List;
public class MTL 
{
	File file1=new File("User-defined Properties.txt"); 
	List<String> propertyList = Get.getContents(file1); 
	File file2=new File("MetTapRules.txt"); 
	List<String> MetTapList = Get.getContents(file2);	
	File file3=new File("MTL.txt");
	List<String> mtlRulesList = Get.getContents(file3);
	String t1="";
	String t2="";
	String a="";
	String phi="";
	MTL()  
	{
		int i;
		for(i=0;i<MetTapList.size();i++) 
		{
			String r1[]=MetTapList.get(i).split("THEN");
			String f1=r1[0].substring(2);
			String r2[]=f1.split("AND");
			String f2=r2[0].substring(6);
			t1=Judge(f2);
			t1= t1.substring(0,t1.length()-1);
			String f3=r2[1].substring(6);
			t2=Judge(f3);
			t2= t2.substring(0,t2.length()-1);
			String f4=r1[1].substring(6);
			f4 = f4.substring(0,f4.length()-1);
			f4=f4+" ";
			a=Judge(f4);
			a= a.substring(0,a.length()-1);
			phi="[]"+"((("+t1+")"+"/\\"+"("+t2+"))"+"->"+"("+a+"))";
			mtlRulesList.add(phi);
		}
		for(i=0;i<propertyList.size();i++) 
		{
			String p1=propertyList.get(i);
			String p2=p1.replaceAll("G_", "[]_");
			String p3=p2.replaceAll("F_", "<>_");
			phi=Add(p3);
			mtlRulesList.add(phi);
		}
		Put.putContents(mtlRulesList, file3);
	}
	public String Judge(String s)
	{
		String t="";
		String t1="";
		if(s.contains("F"))
		{
			s= s.substring(1,s.length());
			String r3[]=s.split("F:");
			t1="<>_"+r3[1];
			t=t1+r3[0];
			return t;
		}
		else if(s.contains("G"))
		{
			s= s.substring(1,s.length());
			String r3[]=s.split("G:");
			t1="[]_"+r3[1];
			t=t1+r3[0];
			return t;
		}
		else
		{
			t=s.substring(1,s.length());
			return t;
		}
	}
	public String Add(String s)
	{
		String t=s;
		String t1="";
		if(s.contains("G"))
		{
			s= s.substring(2,s.length());
			t="([]"+s;
		}
		if((s.contains("—>") & s.contains("/\\"))||((s.contains("—>") & s.contains("\\/"))))
		{
			String r3[]=s.split("—>");
			t1="("+r3[0]+")->";
			t=t1+r3[1];
		}
		return t;
		
	}
	public static void main(String args[])
	{
		new MTL();
	}
}
