public class Transaction {
   //Instance variable representing sender of bitcoins
    private String sender;
    //Instance variable representing receiver of bitcoins
    private String receiver;
    //Instance variable representing amount of bitcoins being sent/received
    private int amount;


    //Constructor initializes who is the sender and receiver and how much bitcoins are being sent
    public Transaction(String sender, String receiver, String amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = Integer.parseInt(amount);
    }


    // GETTERS METHODS

    public String getSender() { return sender; }

    public String getReceiver() { return receiver; }

    public int getAmount() { return amount; }

    public String toString() { return sender + ":" + receiver + "=" + amount; }
}