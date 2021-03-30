package net.atopecode.authservice.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Esta clase al implementar la interfaz 'ApplicationContextAware' y declararla como un @Component (es un Bean para inyectar),
 * permite acceder al 'ApplicationContext' desde cualquier parte del código para recuperar 'Beans' sin utilizar el decorador
 * o atributo '@Autowired'. Cuando se inicia Spring, detecta que esta clase (Componente o Bean) implementa la interfaz correspondiente
 * y ejecuta su método 'setApplicationContext' asignándole el 'applicationContext'.
 * Para utilizarlo:
 *         ApplicationContext context = ApplicationContextProvider.context;
 *         IUsuarioRepository userRepo = context.getBean(IUsuarioRepository.class);
 */

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    public static ApplicationContext context;
    public ApplicationContext getApplicationContext(){
        return context;
    }

    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
