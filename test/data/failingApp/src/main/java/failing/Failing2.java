package failing;

import java.io.*;
import java.lang.*;

public class Failing2 {
	
	public int myIntInput;


	public void throwCaughtException(int input) throws IOException {
		myIntInput = input;
		throw new IOException("Throwing IOException");
	}

	
 }
