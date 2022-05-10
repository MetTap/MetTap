package MetTap;
import java.io.File;
import java.util.List;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import Runtime.*;
import matlabcontrol.*;
public class Runtime_Verification 
{
	public static void main(String args[]) throws Exception
	{	
		
		File file1=new File("str.txt");    
		List<String> strList = Get.getContents(file1);  
		File file2=new File("A.txt");    
		List<String> AList = Get.getContents(file2);  
		File file3=new File("b.txt");    
		List<String> bList = Get.getContents(file3);  			
		File file4=new File("RVMTL.txt");    
		List<String> mtlList = Get.getContents(file4); 
		File file5=new File("MapMTL.txt");    
		List<String> MapList = Get.getContents(file5); 
		Trace_Slice datapro=new Trace_Slice();
		Class1 rv=new Class1();
				for(int i=0;i<mtlList.size();i++)
		{
			double clg[][]=datapro.readSpecifyColumns(MapList.get(i));
			double a[][]= new double [clg[0].length][3];
			double time[][]= new double [clg[0].length][1];
			for(int j=0;j<3;j++)
			{
				for(int flag=0;flag<clg[0].length;flag++)
				{					
					a[flag][j]=clg[j][flag];		
				}
			}
			for(int flag=0;flag<clg[0].length;flag++)
			{					
				time[flag][0]=flag;		
			}
			MWNumericArray Clg = new MWNumericArray(a,MWClassID.DOUBLE);	 
			MWNumericArray Time = new MWNumericArray(time,MWClassID.DOUBLE);
			Object[] res = rv.RV(1,mtlList.get(i),strList.get(i),AList.get(i),bList.get(i),Clg,Time);
			double result =(double) res[0];
			if(result != 0.5)
			{
				System.out.println( "Rule"+i+":Error");	
			}
			
		}
	}
}
