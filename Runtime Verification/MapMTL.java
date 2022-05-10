package MetTap;
import java.io.File;
import java.util.List;
public class MapMTL  
	{
		File file1=new File("MetTapRules.txt"); 
		List<String> MetTapList = Get.getContents(file1);
	
		File file2=new File("RVMTL.txt");
		List<String> mtlList = Get.getContents(file2);
		
		File file3=new File("boolfalse.txt"); 
		List<String> falseList = Get.getContents(file3);
		
		File file4=new File("number.txt"); 
		List<String> numberList = Get.getContents(file4);	
		
		File file5=new File("str.txt"); 
		List<String> strList = Get.getContents(file5);	
		
		File file6=new File("A.txt"); 
		List<String> AList = Get.getContents(file6);	
		
		File file7=new File("b.txt"); 
		List<String> bList = Get.getContents(file7);			

		String t1="";
		String t2="";
		String a="";
		
		String t0="";
		String ts0="";
		String act0="";
		
		String t3="";
		String ts3="";
		String act3="";
		
	
		String phi="";
		String str="";
		String A="";
		String b="";
		
		MapMTL()  
		{
			int i;
			for(i=0;i<MetTapList.size();i++) 
			{
				String str1="a";
				String A1="-1";
				String b1="-0.5";
				
				String str2="b";
				String A2="-1";
				String b2="-0.5";
				
				String str3="c";
				String A3="-1";
				String b3="-0.5";
				
				String r1[]=MetTapList.get(i).split("THEN");
				String f1=r1[0].substring(2);
				String r2[]=f1.split("AND");
				String f2=r2[0].substring(6);
				t1=TransJudge(f2);
				t1= t1.substring(0,t1.length()-1);
				String ts1=t1;
				if(t1.contains("] ")) 
				{
					String t[]=t1.split("] ");
					ts1=t[1];
					t0=t[0];
				}									
				for(int j=0;j<falseList.size(); j++)
				{
					if(ts1.contains(falseList.get(j)))
					{
						str1="a";
						A1="1";
						b1="0.5";	
					}					
				}
				for(int j=0;j<numberList.size(); j++)
				{
					if(ts1.contains(numberList.get(j)))
					{
						str1="a";
						A1="1";
						String s[] = ts1.split(numberList.get(j));
						String s2[] = s[1].split("degree");
						b1=s2[0].substring(1,s2[0].length()-1);
					}					
				}			
					
				String f3=r2[1].substring(6);
				t2=TransJudge(f3);
				t2= t2.substring(0,t2.length()-1);
				String ts2=t2;
				if(t2.contains("] ")) 
				{
					String t[]=t2.split("] ");
					ts2=t[1];
					ts0=t[0];
				}	

				for(int j=0;j<falseList.size(); j++)
				{
					if(ts2.contains(falseList.get(j)))
					{
						str2="b";
						A2="1";
						b2="0.5";	
					}					
				}
				for(int j=0;j<numberList.size(); j++)
				{
					if(ts2.contains(numberList.get(j)))
					{
						str2="b";
						A2="1";
						String s[] = ts2.split(numberList.get(j));
						String s2[] = s[1].split("degree");
						b2=s2[0].substring(1,s2[0].length()-1);
					}					
				}	
				String f4=r1[1].substring(6);
				f4 = f4.substring(0,f4.length()-1);
				f4=f4+" ";
				a=TransJudge(f4);
				a= a.substring(0,a.length()-1);
				String act1=a;
				if(a.contains("]")) 
				{
					String t[]=a.split("] ");
					act1=t[1];	
					act0=t[0];
				}	
				for(int j=0;j<falseList.size(); j++)
				{
					if(act1.contains(falseList.get(j)))
					{
						str3="c";
						A3="1";
						b3="0.5";
						break;
					}					
				}
				for(int j=0;j<numberList.size(); j++)
				{
					if(act1.contains(numberList.get(j)))
					{
						str3="c";
						A3="1";
						String s[] = act1.split(numberList.get(j));
						String s2[] = s[1].split("degree");
						b3=s2[0].substring(1,s2[0].length()-1);
						break;
					}					
				}
				if(t0.equals(""))
					t3=str1;
				else
					t3=t0+"]"+str1;
				if(ts0.equals(""))
					ts3=str2;
				else
					ts3=ts0+"]"+str2;
				if(act0.equals(""))
					act3=str3;
				else
					act3=act0+"]"+str3;
				
				phi="[]"+"(("+t3+"/\\"+ts3+")"+"->"+act3+")";
				mtlList.add(phi);
				str=str1+","+str2+","+str3;
				strList.add(str);
				A=A1+","+A2+","+A3;
				AList.add(A);
				b=b1+","+b2+","+b3;
				bList.add(b);
				
				
				
			}
			
			Put.putContents(mtlList, file2);
			Put.putContents(strList, file5);
			Put.putContents(AList, file6);
			Put.putContents(bList, file7);
		}
		public String TransJudge(String s)
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
		public static void main(String args[])
		{
			new MapMTL();
		}
	}

