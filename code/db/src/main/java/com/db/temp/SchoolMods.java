package com.db.temp;

import java.security.ProtectionDomain;

import com.db.school.SchoolDao;



import com.sys.utils.DateTimeUtils;

import java.io.File;  
import java.net.MalformedURLException;  
import java.net.URL;  
import java.security.CodeSource;  

import org.bson.types.ObjectId;


public class SchoolMods {

	public static void main(String[] args) {
		
		

		ObjectId id =new ObjectId(DateTimeUtils.stringToDate("2016-06-24 00:00:00",DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
		ObjectId id2 =new ObjectId(DateTimeUtils.stringToDate("2016-08-23 00:00:00",DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
		
		System.out.println(id);
		System.out.println(id2);
		
		
		//576c0780f446bc0ec0ef0f4f
		//57bb2180f446bc0ec0ef0f50
		
		SchoolDao dao =new SchoolDao();
		
		dao.updateMods(0, "1,2,3,4,5,6,7");
		dao.updateMods(1, "1,2,3,4,5,6,7,15");
		dao.updateMods(3, "1,2,3,4,5,6,7,8,9,10,11,12,13,18,20");
		dao.updateMods(4, "8,");
		dao.updateMods(5, "1,2,3,4,5,6,7,8,9,10,11,12,13,");
		dao.updateMods(6, "1,2,3,4,5,6,7,9,10,11,12,13,");
		dao.updateMods(7, "1,2,3,4,5,6,7,8");
		dao.updateMods(8, "1,2,3,4,5,6,7");
		dao.updateMods(9, "1,2,3,4,5,6,7,8,9,10,11,12,13,20");
		dao.updateMods(10, "1,2,3,4,5,6,7,11");
		
		dao.updateMods(11, "1,2,4,5,7,");
		
		dao.updateMods(12, "1,2,3,4,5,6,7,12,13");
		dao.updateMods(13, "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20");//复兰
		
		dao.updateMods(14, "1,2,3,4,5,6,7,9,10,11,12,13,15,");//复兰
		
		dao.updateMods(15, "1,2,5,7,11");
		
		dao.updateMods(16, "1,2,3,4,5,7,10,11,12,13,14");
		dao.updateMods(17, "1,2,4,8");
		
		dao.updateMods(18, "1,2,4,5,6,7,9,10,11,12,13,14,18");
		
		dao.updateMods(19, "1,2,3,4,5,6,7,8,12,13,");
		
		dao.updateMods(20, "1,2,4,5,7,9,10,11,15,");
		

		dao.updateMods(22, "1,2,4,5,7,9,11,14,20,"); //有鸿定制
		dao.updateMods(24, "1,2,3,4,5,6,7,8,9,10,12,13,");
		dao.updateModsNoNVValue("1,2,3,4,5,6,7");
		
	}
	
	
	
	
	public static String where(final Class cls) {  
        if (cls == null)throw new IllegalArgumentException("null input: cls");  
        URL result = null;  
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");  
        final ProtectionDomain pd = cls.getProtectionDomain();  
        if (pd != null) {  
            final CodeSource cs = pd.getCodeSource();  
            if (cs != null) result = cs.getLocation();  
            if (result != null) {  
                if ("file".equals(result.getProtocol())) {  
                    try {  
                        if (result.toExternalForm().endsWith(".jar") ||  
                                result.toExternalForm().endsWith(".zip"))  
                            result = new URL("jar:".concat(result.toExternalForm())  
                                    .concat("!/").concat(clsAsResource));  
                        else if (new File(result.getFile()).isDirectory())  
                            result = new URL(result, clsAsResource);  
                    }  
                    catch (MalformedURLException ignore) {}  
                }  
            }  
        }  
        if (result == null) {  
            final ClassLoader clsLoader = cls.getClassLoader();  
            result = clsLoader != null ?  
                    clsLoader.getResource(clsAsResource) :  
                    ClassLoader.getSystemResource(clsAsResource);  
        }  
        return result.toString();  
    }  
}
