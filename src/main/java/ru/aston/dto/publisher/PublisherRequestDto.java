package ru.aston.dto.publisher;

public class PublisherRequestDto {

    private String name;
    private String city;

    public PublisherRequestDto() {
    }

    public PublisherRequestDto(String name, String city) {
        this.name = name;
        this.city = city;
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
