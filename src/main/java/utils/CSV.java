package utils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CSV {
	private final RandomAccessFile file;
	private final String[] header;
	private String delimiter = ",";
	private long dataOffset;
	
	public CSV(String path) throws IOException {
		file = new RandomAccessFile(path, "r");
		
		if (file.length() == 0) {
			throw new IOException("Empty file");
		}
		
		header = file.readLine().split(delimiter);
		dataOffset = file.getFilePointer();
	}
	
	public String[] getHeader() {
		return header;
	}
	
	public String[] readRow() throws IOException {
		String line = file.readLine();
		return (line != null) ? line.split(delimiter) : null; 
	}
	
	public void revert() throws IOException {
		file.seek(dataOffset);
	}
	
	@Override
	public String toString() {
		byte[] buffer = { 0 };
		
		try {
			buffer = new byte[(int) file.length()];
			file.seek(0);
			file.readFully(buffer);
			
		} catch (IOException e) {
			// do nothing
		}
		
		return new String(buffer);
	}
}
