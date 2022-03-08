package failing;

import java.io.*;
import java.lang.*;

public class Failing2 {
	
	public String myIntInput;


	public void throwUncaughtException(String input) throws NullPointerException {
				myIntInput = input;
		throw new NullPointerException("Throwing NullPointerException");
	}

	
 }
