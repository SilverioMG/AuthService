package net.atopecode.authservice.pruebaflushtransactional;

public class PostDto {

    private Long id;

    private String name;

    public PostDto() {
        //Empty Constructor.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
