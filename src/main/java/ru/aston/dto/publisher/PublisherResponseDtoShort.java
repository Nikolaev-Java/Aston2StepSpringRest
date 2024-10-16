package ru.aston.dto.publisher;

public class PublisherResponseDtoShort {

    private long id;
    private String name;
    private String city;

    public PublisherResponseDtoShort() {
    }

    public PublisherResponseDtoShort(long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
