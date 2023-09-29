package crm_project_22.entity;

public class Status {
    private int id;
    private String name;
    private String description;
    private Double percent;
    private int count;

    public Status() {
    }

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Status(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Status(int id, String name, Double percent) {
        this.id = id;
        this.name = name;
        this.percent = percent;
    }

    public Status(int id, String name, Double percent, int count) {
        this.id = id;
        this.name = name;
        this.percent = percent;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
