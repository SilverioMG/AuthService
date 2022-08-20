package net.atopecode.authservice.pruebaflushtransactional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final CommentConverter commentConverter;

    public PostController(PostService postService,
                          CommentConverter commentConverter) {
        this.postService = postService;
        this.commentConverter = commentConverter;
    }

    @GetMapping("/savePost")
    public ResponseEntity<Post> savePost() {
        Post post = new Post(1L, "Post1");
        postService.savePost(post);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/saveComment")
    public ResponseEntity<CommentDto> saveComment() {
        //El 'Post' con id 1 ya existe en la B.D. (Http GET '/savePost'). Se va a insertar un nuevo 'Comment' para el 'Post' con id 1 pero
        //en la propiedad 'Comment.post' solo se indica el 'id' y no el 'nombre', para que al guardar hay que hacer
        //un 'refresh()' y recuperar los datos del 'Post' asociado al nuevo 'Comment' y que se devuelvan como respuesta hacia la web.
        Post post = new Post(1L, null);
        Comment comment = new Comment(null, "texto comment", post);

        comment = postService.saveComment(comment);
        CommentDto dto = commentConverter.convert(comment);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/saveCommentRollback")
    public ResponseEntity<CommentDto> saveCommentRollback() {
        Post post = new Post(1L, null);
        Comment comment = new Comment(null, "texto comment", post);

        comment = postService.saveComment(comment, true);
        CommentDto dto = commentConverter.convert(comment);
        return ResponseEntity.ok(dto);
    }
}
