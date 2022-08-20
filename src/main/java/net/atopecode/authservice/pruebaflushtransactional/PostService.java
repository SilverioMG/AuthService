package net.atopecode.authservice.pruebaflushtransactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class PostService {

    private final IPostRepository postRepository;
    private final ICommentRepository commentRepository;
    private final EntityManager entityManager;

    public PostService(
            IPostRepository postRepository,
            ICommentRepository commentRepository,
            EntityManager entityManager) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Transactional(rollbackFor = Exception.class)
    public Comment saveComment(Comment comment) {
        return saveComment(comment, false);
    }

    @Transactional(rollbackFor = Exception.class)
    public Comment saveComment(Comment comment, boolean forceRollback) {
        comment = commentRepository.save(comment);
        entityManager.flush(); //Al hacer 'flush()' se guardan los datos que están en memoria (context de Hibernate) a la B.D. por si algún procedimiento almacenado quiere acceder a ellos, pero no hace commit (no finaliza la transacción), si se produce un rollback, se eliminan los datos guardados en B.D. con el flush.
        entityManager.refresh(comment); //Al hacer el refresh(), 'comment' y 'comment.post' se vuelven a leer de la B.D. y el campo 'comment.post.name' recupera su valor.
                                        //El refresh vuelve a recuperar de la B.D. la entidad y todas sus propiedades de navegación con valor (!= null), pero no recupera las propiedades de navegación de tipo LazyLoad que no han sido cargadas previamente (para eso hay que usar 'Hibernate.initialize()').
                                        //Aunque el refresh recupera todos los valores para las propiedades de navegación de tipo LazyLoad que ya tenían valor (!= null), no hace un 'Hibernate.initialize()' por lo que si se quiere acceder a esas propiedades
                                        //de tipo 'LazyLoad' fuera de la Transaccion, como ya no hay session con la B.D., se producirá un error. Hay que utilizar 'Hibernate.initilize()' para evitar esto, o volver a leer de la B.D. la entidad con todas las propiedades cargadas usando 'FETCH' en la query.
                                        //El 'refresh()' funciona igual sin tener que hacer un 'flush()', son 2 métodos independientes y cada uno hace una cosa distinta.

        if(forceRollback) forceRollback();

        //Sino se inicializa la propiedad LazyLoad 'comment.post', en el controller falla el mapper porque no hay session de hibernate (porque ya no hay transaction) para acceder a la propiedad.
        //Es curioso que depurando no falla, pero si ejecuto sin depurar si que falla, creo que debe ser algo del IntelliJIdea, porque en Eclipse si que fallaría depurando.
        initialize(comment);

        return comment;
    }

    private void forceRollback(){
        Post newPost = new Post(null, "PostNew");
        postRepository.save(newPost);

        throw new RuntimeException("Se fuerza un Rollback.");
    }

    private void initialize(Comment comment) {
        if(comment != null) {
            Hibernate.initialize(comment.getPost());
        }
    }
}
