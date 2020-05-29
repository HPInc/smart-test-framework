package system.pojo;

public class NoteDemoClass {

    public Note note;

    public NoteDemoClass() {
    }

    public NoteDemoClass factory() {
        this.note = new Note();
        note.body = "Don't forget that STF means Smart Test Framework";
        note.from = "STF";
        note.heading = "Urgent!";
        note.to = "QA User";
        return this;
    }
}