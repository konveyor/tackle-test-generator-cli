package failing;

import java.io.*;
import java.lang.*;

public class Failing {
	
	public int myIntInput;


	public void throwCaughtException(int input) throws IOException {
		myIntInput = input;
		throw new IOException("Throwing IOException");
	}

	public void throwUncaughtException(String input) throws NullPointerException {
		
		throw new NullPointerException("Throwing NullPointerException");
	}

	
 }
