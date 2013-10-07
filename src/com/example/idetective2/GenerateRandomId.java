package com.example.idetective2;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateRandomId {
	public GenerateRandomId() {
		// constructor
	}
	
	public String getId() {
		return new BigInteger(130, new SecureRandom()).toString(32);
	}

}
