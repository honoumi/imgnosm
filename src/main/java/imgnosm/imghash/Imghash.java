package imgnosm.imghash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/** 
 * 工具类
 */
public class Imghash	{
	
	public static int bitCount(long n)
	{
	    int c =0 ;
	    for (c =0; n != 0; c++)
	    {
	        n &= (n -1) ; // 清除最低位的1
	    }
	    return c ;
	}
	
	
	public static int hammingDistance(String s1, String s2)
	{
		long l1 = Long.parseUnsignedLong(s1, 16);
		long l2 = Long.parseUnsignedLong(s2, 16);
		return bitCount(l1 ^ l2);
	}
	

}