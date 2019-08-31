package aho.uozu.auctionsniper;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages =
            new ArrayBlockingQueue<Message>(1);
    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }
    public void receivesAMessage() throws InterruptedException {
        assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
    }
}
