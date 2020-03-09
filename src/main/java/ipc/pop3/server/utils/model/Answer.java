package ipc.pop3.server.utils.model;

public class Answer {

    private int answerCode = 999;
    private String answerMessage = "Error, answer not instantiated";

    public Answer(int code) {
        this.answerCode = code;
        this.answerMessage = "No error message";
    }

    public Answer(int code, String message) {
        this.answerCode = code;
        this.answerMessage = message;
    }

    protected void setAnswerCode(int code) { this.answerCode = code; }

    protected void setAnswerMessage(String message) { this.answerMessage = message; }

    public int getAnswerCode() { return answerCode; }

    public String getAnswerMessage() { return answerMessage; }
}
