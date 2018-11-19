import java.io.*;
import java.sql.Timestamp;
import java.util.*;



public class Block {
    // the index of the block in the list
    private int index;

    // time at which transaction has been processed
    private Timestamp timestamp;

    // the transaction object
    private Transaction transaction;

    //  random string (for proof of work)
    private String nonce;

    // (in first block, set to string of zeroes of size of complexity "00000")
    private String previousHash;

    // hash of the block (hash of string obtained from previous variables via toString() method)
    private String hash;



    // GETTER METHODS

    // getter for Index
    public int getIndex() { return index; }

    // getter for timestamp
    public Timestamp getTimestamp() { return timestamp; }

    // getter for transaction
    public Transaction getTransaction() { return transaction; }

    // getter for nonce
    public String getNonce() { return nonce; }

    // getter for  previous hash
    public String getPreviousHash() { return previousHash; }

    // getter for curent hash
    public String getHash() { return hash; }



    // ToString method for Block class
    public String toString() {
        return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
    }


    //Constructor for the first block
    public Block(int index, Transaction transaction, String previousHash) {
        this.index = index;
        this.transaction = transaction;

        // for the first block
        if (index == 0)
            // previous hash for the first block is 00000
            this.previousHash = "00000";
        else
            this.previousHash = previousHash;
        // timestamp for initial blockchain
        timestamp = new Timestamp(System.currentTimeMillis());

        findHash();
    }

    //Block that would be created from reading a file
    public Block(int index, Transaction transaction, String nonce, String previousHash, long timestamp, String hash) {
        this.index = index;
        this.transaction = transaction;
        this.nonce = nonce;
        if (index == 0) {
            // previous hash for the first block is 00000
            this.previousHash = "00000";
        }
        else {
            //previous is equal to previous
            this.previousHash = previousHash;
        }
        this.timestamp = new Timestamp(timestamp);
        this.hash = hash;
    }



    private void findHash() {
        Random randomnumber = new Random();
        String testHash = null;
        Sha1 hasher = new Sha1();
        long trials = 0l;

        //to count how many times does it take to find suitable hash
        while (true) {

            nonce = "";


            trials++;


            for (int l = 0; l <= randomnumber.nextInt(5) + 10; l++) {
                char a = (char) (randomnumber.nextInt(93) + 33);
                nonce += a;
            }
            try {
                testHash = Sha1.hash(toString());

                // checks if the first five variables = 00000 in the hash
                if (testHash.startsWith("00000")) {
                    hash = testHash;
                    System.out.println("Number of Trials: " + trials);
                    // breaks if it finds a suitable hash
                    break;
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("Unsupported Encoding Block");
            }
        }


    }
}