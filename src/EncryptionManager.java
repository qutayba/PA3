import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptionManager {

    private static final BigInteger INIT_PRIM_NUMBER = new BigInteger("2");
    private BigInteger n;
    private BigInteger p;
    private BigInteger q;
    private BigInteger e;
    private BigInteger d;

    /**
     * Calculates the p and q values
     * @param nValue The n value to be used in the calculation
     * @throws Exception Throws exception with any calculation error
     */
    public void calculatePAndQ(BigInteger nValue) throws Exception {

        // Assigning the n value with start prime number for p
        n = nValue;
        p = INIT_PRIM_NUMBER;

        // Checking the result of dividing the p and q values
        // will result the n value
        while (p.compareTo(n.divide(INIT_PRIM_NUMBER)) <= 0) {

            // If the result equals the n value, than assign
            // the tested value to q
            if(n.mod(p).equals(BigInteger.ZERO)) {
                q = n.divide(p);
                return;
            }

            // Otherwise, find the next prime number and repeat the test
            p = p.nextProbablePrime();
        }

        throw new Exception("No solution found");
    }


    /**
     * Calculates the e value based on the p and q (from step 1)
     * @throws Exception Throws exception with any calculation error
     */
    public void calculateE() throws Exception {

        if(p == null || q == null) {
            throw new Exception("No q or p calculated. You need to execute step 1.");
        }

        // Getting the phi of the two prime-numbers (q and p)
        BigInteger phi = getPhi();

        // Starting will the first start prime number
        e = INIT_PRIM_NUMBER;

        // Checking if the greatest common divisor equals one
        // otherwise, increase the e value with one and test again
        while(!phi.gcd(e).equals(BigInteger.ONE))
        {
            e = e.add(BigInteger.ONE);
        }
    }

    /**
     * Calculates the value of d
     * @param nValue The value of n
     * @param eValue The value of e
     * @throws Exception Throws exception with any calculation error
     */
    public void calculateD(BigInteger nValue, BigInteger eValue) throws Exception {
        if(nValue == null || eValue == null) {
            throw new Exception("No value for n or e can be found.");
        }

        // Assigning the entered values
        n = nValue;
        e = eValue;

        // Calculating the p and q values based on the entered n
        calculatePAndQ(n);

        // Calculating the d value by using mod-inverse (see the manual for more info)
        d = e.modInverse(getPhi());
    }


    /**
     * Encrypts a message value (only numeric)
     * @param message The message value to be encrypted
     * @return The encrypted message
     * @throws Exception Throws exception with any encryption error
     */
    public String encrypt(String message) throws Exception {
        if(message == null || message.isEmpty()) {
            throw  new Exception("There is no message entered.");
        }

        if(e == null || n == null) {
            throw new Exception("No n or e value found. You need to execute step 1 and 2.");
        }

        // Spiting the message into a char array, because we want
        // to encrypt every letter alone
        char[] mArray = message.toCharArray();

        // Defining a string array to hold the encrypted letters (or characters)
        String[] cArray = new String[mArray.length];

        // Loop throw all letters and encrypt them
        // The encrypted chars will be stored in the cArray
        for (int i = 0; i < mArray.length; i++) {
            BigInteger m = BigInteger.valueOf((int)mArray[i]);
            if(m.compareTo(n) > 0) {
                throw new Exception("The value to be encrypted is greater than the n value. Please, change the values and try again.");
            }

            // Encrypting the message based on the calculated e and n values
            BigInteger c = m.modPow(e, n);
            cArray[i] = c.toString();
        }

        // Joining all encrypted chars into on comma separated string
        return String.join(",", cArray);
    }

    /**
     * Decrypts a message value
     * @param message The message to be decrypted
     * @return The decrypted message
     * @throws Exception Throws exception with any decryption error
     */
    public String decrypt(String message) throws Exception {
        if(message == null || message.isEmpty()) {
            throw  new Exception("There is no message entered.");
        }

        if(d == null || n == null) {
            throw new Exception("No n or d value found. You need to execute step 1.");
        }

        // Spiting the encrypted message into array based on the comma as separator
        String[] cArray = message.split(",");

        // Just a char array to hold the decrypted chars
        char[] mArray = new char[cArray.length];

        // Loop throw all founded chars and decrypt them
        for (int i = 0; i < cArray.length; i++) {
            BigInteger m = new BigInteger(cArray[i]).modPow(d, n);
            mArray[i] = (char)m.intValue();
        }

        // Return all decrypted chars as one string
        return new String(mArray);
    }


    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    /**
     * Calculates the phi value based on p and q
     * @return The phi value
     */
    public BigInteger getPhi() {
        BigInteger pMin1 = p.subtract(BigInteger.ONE);
        BigInteger qMin1 = q.subtract(BigInteger.ONE);
        return pMin1.multiply(qMin1);
    }
}
