package net.atopecode.authservice.pruebaflushtransactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentDto convert(Comment comment){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(comment, CommentDto.class);
    }
}
