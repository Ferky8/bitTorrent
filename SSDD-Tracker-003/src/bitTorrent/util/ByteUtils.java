package bitTorrent.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class ByteUtils {
	
	public static void main (String[] args) {
		long number = 45980867l;
		
		byte[] buffer = toBigEndian(number);
		
		for (byte b : buffer) {
			System.out.print("'" + b + "' ");
		}
		
		System.out.println();		
		System.out.println(bigEndianToLong(buffer));
		
		//UPDATE: 23/12/2015 - Test for the transformation between hex string and byte array
		System.out.println("Hex infoHash: 916A6189FFB20F0B20739E6F760C99174625DC2B");
		byte[] byteArray = ByteUtils.toByteArray("916A6189FFB20F0B20739E6F760C99174625DC2B");
		System.out.println("Byte array size: " + byteArray.length);
		System.out.println("New infoHash: " + ByteUtils.toHexString(byteArray));
	}
	
	//Converts int value to a Big Endian byte array
	public static byte[] toBigEndian(int intValue){
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    buffer.putInt(intValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts long value to a Big Endian byte array
	public static byte[] toBigEndian(long longValue){
	    ByteBuffer buffer = ByteBuffer.allocate(8);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    buffer.putLong(longValue);
	    buffer.flip();
	    
	    return buffer.array();
	}
	
	//Converts Big Endian byte array to int
	public static int bigEndianToInt(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    return buffer.getInt();
	}
	
	//Converts Big Endian byte array to long
	public static long bigEndianToLong(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    return buffer.getLong();
	}
	
	//Converts byte array to int
	public static int arrayToInt(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    
	    return buffer.getInt();
	}
	
	//Converts byte array to long
	public static long arrayToLong(byte[] byteArray){
	    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    
	    return buffer.getLong();
	}

    public static int byteArrayToInt(byte[] b) {
        if (b.length == 4) {
            return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
        } else if (b.length == 2) {
            return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);
        } else {
        	return 0;
        }
    }
	
	public static byte[] fileToByteArray(String filename) {
		byte[] result = null;
		
		if (filename != null) {
			try (FileInputStream fis = new FileInputStream(filename);
				 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {		        
				byte[] buf = new byte[1024];
		        
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
	            }

				result = bos.toByteArray();
			} catch (IOException e) {
				System.err.println("# fileToByteArray(): " + e.getMessage());
			}
		}
		
		return result;
	}	
	
	public static byte[] generateSHA1Hash(byte[] bytes) {
		try {
			byte[] hash = new byte[20];
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			hash = sha.digest(bytes);
			
			return hash;		
		} catch (NoSuchAlgorithmException e) {
			System.err.println("'SHA-1' is not a valid algorithm name.");
		}
		
		return null;
	}
	
	public static String toStringIpAddress(int number) {
		StringBuffer ipBuffer = new StringBuffer();
		
		ipBuffer.append(((number >> 24 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(((number >> 16 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(((number >> 8 ) & 0xFF));
		ipBuffer.append(".");
		ipBuffer.append(number & 0xFF);
		
		return  ipBuffer.toString();
	}
	
	public static String createPeerId() {
		StringBuffer bufferID = new StringBuffer();
		bufferID.append("-");
		bufferID.append("SSDD01");
		bufferID.append("-");
		Random random = new Random();
		
		for(int i=0; i<12; i++) {
			bufferID.append(random.nextInt(9));
		}
				
		return bufferID.toString();
	}
	
	/**
	 * UPDATE: 23/12/2015 - This methods transform bytes arrays and hex strings 
	 */
	public static String toHexString(byte[] array) {
	    return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
}