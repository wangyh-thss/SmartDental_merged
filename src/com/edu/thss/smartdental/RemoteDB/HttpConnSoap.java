/*
 * AuthorBy :Qiao
 * reference:http://blog.csdn.net/haoxingfeng/article/details/9111105
 */

package com.edu.thss.smartdental.RemoteDB;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpConnSoap {
	public ArrayList<String> GetWebService(String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues) {
		ArrayList<String> Values = new ArrayList<String>();
		
        //服务器地址
        String ServerUrl = "http://166.111.80.118/Service1.asmx";  
  
        String soapAction = "http://tempuri.org/" + methodName;  
  
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"  
                      + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"  
                      + "<soap:Body />";  
        String tps, vps, ts;  
        String mreakString = "";  
        mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";  
        for (int i = 0; i < Parameters.size(); i++)  
        {  
            tps = Parameters.get (i).toString();  
            vps = ParValues.get (i).toString();  
            ts = "<" + tps + ">" + vps + "</" + tps + ">";  
            mreakString = mreakString + ts;  
        }  
        mreakString = mreakString + "</" + methodName + ">";  
        String soap2 = "</soap:Envelope>";  
        String requestData = soap + mreakString + soap2;  
  
        try  
        {  
            URL url = new URL (ServerUrl); 
             
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            byte[] bytes = requestData.getBytes ("utf-8"); 
            con.setDoInput (true); 
            con.setDoOutput (true); 
            con.setUseCaches (false); 
            con.setConnectTimeout (6000);
            con.setRequestMethod ("GET"); 
            con.setRequestProperty ("Content-Type", "text/xml;charset=utf-8");
            con.setRequestProperty ("SOAPAction", soapAction); 
            con.setRequestProperty ("Content-Length", "" + bytes.length);
     
            OutputStream outStream = con.getOutputStream();  
            outStream.write (bytes);  
            outStream.flush();  
            outStream.close();  
  
            InputStream inputStream = con.getInputStream();  
            Values = inputStreamtovaluelist(inputStream,methodName);
            return Values;  
  
            
        }catch (Exception e)  
        {  
            e.printStackTrace();  
            return Values;  
        }  
	}

	public ArrayList<String> inputStreamtovaluelist(InputStream in, String MonthsName){
		StringBuffer out = new StringBuffer();
		String s1 = "";
		byte[] b = new byte[4096];
		ArrayList<String> Values = new ArrayList<String>();
		Values.clear();

		try {
			for (int n; (n = in.read(b)) != -1;) {
				s1 = new String(b, 0, n);
				out.append(s1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(out);
		String[] s13 = out.toString().split("><");
		String ifString = MonthsName + "Result";
		String TS = "";
		String vs = "";

		Boolean getValueBoolean = false;
		for (int i = 0; i < s13.length; i++) {
			TS = s13[i];
			System.out.println(TS);
			int j, k, l;
			j = TS.indexOf(ifString);
			k = TS.lastIndexOf(ifString);

			if (j >= 0) {
				System.out.println(j);
				if (getValueBoolean == false) {
					getValueBoolean = true;
				} else {

				}

				if ((j >= 0) && (k > j)) {
					System.out.println("FFF" + TS.lastIndexOf("/" + ifString));
					l = ifString.length() + 1;
					vs = TS.substring(j + l, k - 2);
					Values.add(vs);
					System.out.println("�˳�" + vs);
					getValueBoolean = false;
					return Values;
				}

			}
			if (TS.lastIndexOf("/" + ifString) >= 0) {
				getValueBoolean = false;
				return Values;
			}
			if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0) && (j < 0)) {
				k = TS.length();
				vs = TS.substring(7, k - 8);
				Values.add(vs);
			}

		}

		return Values;
	}

}
