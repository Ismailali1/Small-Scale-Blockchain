import java.util.*;
import java.io.*;

// BlockChain class
public class BlockChain {


    //block chain is a arraylist of block objects
    ArrayList<Block> blockchain;

    // constructor of blockchain, initializes the arraylist
    public BlockChain(ArrayList<Block> blockchain) {

        this.blockchain = blockchain;
    }


    public static BlockChain fromFile(String fileName) {

        // created a file object
        File file = new File(fileName);

        //bufferedReader to read from file specified
        BufferedReader filereader;

        // arraylist to add info into
        ArrayList<String> textFileInfo = new ArrayList<String>();


        // try catch for adding all info into textfileinfo ArrayList. checks if valid textfile
        try {

            filereader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = filereader.readLine()) != null) {

                textFileInfo.add(line);
            }
            filereader.close();

        } catch (FileNotFoundException e) {

            System.out.println("File not Found");
            System.exit(0);
        } catch (IOException e) {

            System.out.println("Error reading file");
        }


        // created a arraylist of type block to put all the info gathered from textfile
        ArrayList<Block> BlockList = new ArrayList<Block>();


        // gets index,sender,amount,hash,nonce,timestamp from textFileInfo and passes it through to the parameters of a new block object
        for (int i = 0; i <= textFileInfo.size() - 1; i += 7) {

            Transaction temp = new Transaction(textFileInfo.get(i + 2), textFileInfo.get(i + 3), textFileInfo.get(i + 4));

            if (i == 0) {

                BlockList.add(new Block(Integer.parseInt(textFileInfo.get(i)), temp, textFileInfo.get(i + 5), null, Long.parseLong(textFileInfo.get(i + 1)), textFileInfo.get(i + 6)));
            } else {

                BlockList.add(new Block(Integer.parseInt(textFileInfo.get(i)), temp, textFileInfo.get(i + 5), BlockList.get(BlockList.size() - 1).getHash(), Long.parseLong(textFileInfo.get(i + 1)), textFileInfo.get(i + 6)));
            }
        }
        // returns a blockChain object with a block list filled with block objects
        return new BlockChain(BlockList);
    }


    //Method to write information to any given text file
    public void toFile(String fileName) {

        // write into a new a file, each line is for a different variable: index,timestamp, sender, reciever, amount, nonce , hash
        try {

            PrintWriter pw = new PrintWriter(fileName);

            for (int i = 0; i < getList().size(); i++) {

                pw.println(getList().get(i).getIndex());
                pw.println(Long.toString(getList().get(i).getTimestamp().getTime()));
                pw.println(getList().get(i).getTransaction().getSender());
                pw.println(getList().get(i).getTransaction().getReceiver());
                pw.println(getList().get(i).getTransaction().getAmount());
                pw.println(getList().get(i).getNonce());
                pw.println(getList().get(i).getHash());
            }

            pw.close();
        } catch (IOException e) {

            System.out.println("Error writing file");
        }
    }

    //method to get  the array list containing the the blockchain

    public ArrayList<Block> getList() {
        return blockchain;
    }



    public boolean validateBlockchain() {

        Sha1 validateHash = new Sha1();

        // for loop to check if index is the same as a
        for (int a = 0; a < getList().size(); a++) {

            if (getList().get(a).getIndex() != a) {

                return false;
            }
        }

        // for loop to check if hash is valid for all blocks in block chain
        for (int i = 0; i < getList().size() - 1; i++) {

            try {

                if (!validateHash.hash(getList().get(i).toString()).equals(getList().get(i + 1).getPreviousHash())) {
                    return false;
                }
            } catch (UnsupportedEncodingException e) {

                System.out.println("Cannot Encode Block");
            }
        }

        // list of users that are involved in the blockchain
        ArrayList<String> users = new ArrayList<String>();

        // for loop to add all senders and recievers in the users ArrayList
        for (int i = 0; i < getList().size(); i++) {

            users.add(getList().get(i).getTransaction().getSender());
            users.add(getList().get(i).getTransaction().getReceiver());
        }

        // Used a hashset to remove duplicates of users
        Set<String> withoutDuplicates = new LinkedHashSet<String>(users);
        //emptying list of users
        users.clear();
        //adding back the new list without duplicates
        users.addAll(withoutDuplicates);

        // for loop to check if the user size is less than zero
        for (int j = 0; j < users.size(); j++) {

            if (getBalance(users.get(j)) < 0) {

                return false;
            }
        }

        return true;
    }


    //Method to get the balance of a specific user, this adds up all their spending and what they have for their account, calculating the difference
    public int getBalance(String username) {

        int Balance = 0;

        //bitcoin is not a user
        if (username.equals("bitcoin")) {

            return 0;
        }
        // add up all transactions, put balance in balance variable
        else {

            for (int i = 0; i < getList().size(); i++) {

                // adds up all the bitcoin that a user sent, increments the balance variable
                if (getList().get(i).getTransaction().getSender().equals(username)) {

                    Balance -= getList().get(i).getTransaction().getAmount();
                }

                // adds up all the bitcoin that a user recieved , increments the balance variable
                if (getList().get(i).getTransaction().getReceiver().equals(username)) {

                    Balance += getList().get(i).getTransaction().getAmount();
                }
            }
            return Balance;
        }
    }


    // Method to add new blocks into block chain

    public void add(Block block) { blockchain.add(block); }








    // main method of BlockChain

    public static void main(String[] args) {

        String receiver;
        String amount;
        String input ;
        String output;
        String sender;

        String repeatTransaction;
        boolean flag = true;

        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the File Name to read: ");
        input = scan.next();


        //makes a blockchain object from all the elements in the file given by scanner, takes away need to type .txt
        BlockChain blocks = fromFile(input + ".txt");


        //First checks if given file is valid, if it is valid continue with program
        if (!blocks.validateBlockchain()) {

            System.out.println("Invalid Block Chain");
            System.exit(0);
        }



        // this code will execute if the blocks are valid
        else {

            System.out.println("your textfile is valid!");
            System.out.println("\nMaking a new Transaction");




            //flag is set to false when the user does not want to make a transaction any more

            while (flag) {

                // scans sender info
                System.out.println("Enter the sender for Transaction: ");
                sender = scan.next();


                //scans receiver info
                System.out.println("Enter the receiver for Transaction: ");
                receiver = scan.next();

                //scans amount of bitcoins being sent/received
                System.out.println("Enter the amount for Transaction: ");
                amount = scan.next();


                //this if statement checks if the sender has enough in his balance to send that amount of bitcoins, if he/she cant tell user that they cant send this amount
                if (blocks.getBalance(sender) < Integer.parseInt(amount)) {

                    System.out.println("Sender Balance is less than amount specified. Please try again");
                    continue;





                    // if the sender has enough to make the transaction, proceed
                }else {


                    // I created a new transaction object with the input information
                    Transaction Transaction = new Transaction(sender, receiver, amount);

                    // I then added the new blocks to the blockchain
                    Block newBlock= new Block(blocks.getList().size(), Transaction, blocks.getList().get(blocks.getList().size() - 1).getHash());
                    blocks.add(newBlock);


                    // prompt user if they want to make another transaction
                    System.out.println("Do you want to make another transaction? Type yes or no");
                    repeatTransaction = scan.next();


                    //if no then add previous transactions and end program
                    if (repeatTransaction.equals("no")) {

                        flag = false;
                        break;
                    }

                    // if input is something else end program
                    else if (!repeatTransaction.equals("yes")) {
                        while (true) {

                            System.out.println("Please try again. type yes or no");
                            repeatTransaction = scan.next();

                            if (repeatTransaction.equals("no")) {

                                flag = false;
                                break;
                            } else if (repeatTransaction.equals("yes")) {

                                break;
                            }
                        }






                    }
                }
            }

            //Write into File
            System.out.println("Writing Into File");
            output = input + "_iali088";

            blocks.toFile(output + ".txt");
            System.out.println("Succesful!");
        }
    }
}
















































