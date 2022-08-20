package net.atopecode.authservice.pruebaflushtransactional;

import javax.persistence.*;

public class CommentDto {

    private Long id;

    private String text;

    private PostDto post;

    public CommentDto(){
        //Empty Constructor.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PostDto getPost() {
        return post;
    }

    public void setPost(PostDto post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", post=" + post +
                '}';
    }
}
