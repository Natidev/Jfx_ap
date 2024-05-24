package ap.jfx;

public class EventData {
    int id;
    String name;
    String organizer;
    String date;
    String location;
    String description;

    public EventData(int id, String name, String organizer, String date, String location) {
        this.id = id;
        this.name = name;
        this.organizer = organizer;
        this.date = date;
        this.location = location;
    }
    public EventData(int id, String name, String organizer, String date, String location,String description) {
        this(id,name,organizer,date,location);
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
