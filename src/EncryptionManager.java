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


    public void calculatePAndQ(BigInteger nValue) throws Exception {
        n = nValue;
        p = INIT_PRIM_NUMBER;

        while (p.compareTo(n.divide(INIT_PRIM_NUMBER)) <= 0) {

            if(n.mod(p).equals(BigInteger.ZERO)) {
                q = n.divide(p);
                return;
            }
            p = p.nextProbablePrime();
        }

        throw new Exception("No solution found");
    }

    public void calculateE() throws Exception {
        if(p == null || q == null) {
            throw new Exception("No q or p calculated. You need to execute step 1.");
        }

        BigInteger phi = getPhi();
        e = BigInteger.valueOf(3L);
        while(!phi.gcd(e).equals(BigInteger.ONE))
        {
            e = e.add(BigInteger.ONE);
        }
    }

    public void calculateD(BigInteger nValue, BigInteger eValue) throws Exception {
        if(nValue == null || eValue == null) {
            throw new Exception("No value for n or e can be found.");
        }

        n = nValue;
        e = eValue;
        calculatePAndQ(n);
        d = e.modInverse(getPhi());
    }

    public String encrypt(String message) throws Exception {
        if(message == null || message.isEmpty()) {
            throw  new Exception("There is no message entered.");
        }

        if(e == null || n == null) {
            throw new Exception("No n or e value found. You need to execute step 1 and 2.");
        }

        BigInteger m = new BigInteger(message);

        BigInteger c = m.modPow(e, n);
        return c.toString();
    }

    public String decrypt(String message) throws Exception {
        if(message == null || message.isEmpty()) {
            throw  new Exception("There is no message entered.");
        }

        if(d == null || n == null) {
            throw new Exception("No n or d value found. You need to execute step 1.");
        }

        BigInteger dm = new BigInteger(message).modPow(d, n);
        return dm.toString();
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

    public BigInteger getPhi() {
        BigInteger pMin1 = p.subtract(BigInteger.ONE);
        BigInteger qMin1 = q.subtract(BigInteger.ONE);
        return pMin1.multiply(qMin1);
    }
}
