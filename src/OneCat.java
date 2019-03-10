public class OneCat {
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getTail_length() {
        return tail_length;
    }

    public void setTail_length(int tail_length) {
        this.tail_length = tail_length;
    }

    public int getWhiskers_length() {
        return whiskers_length;
    }

    public void setWhiskers_length(int whiskers_length) {
        this.whiskers_length = whiskers_length;
    }

    private String  color;
    private int tail_length;
    private int whiskers_length;
    public OneCat(String name, String color, int tail_length, int whiskers_length) {
        this.name = name;
        this.color = color;
        this.tail_length = tail_length;
        this.whiskers_length = whiskers_length;
    }
}
