package firstDay;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Created by teng.liu on 2017/5/23.
 */
public class Main {

    public static void main(String[] args) {

        // /Users/teng.liu/githup/springLearning/src/main/resources/applicationContext.xml
        Resource r = new FileSystemResource("/Users/teng.liu/githup/springLearning/src/main/resources/applicationContext.xml");

        DefaultListableBeanFactory factory=new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(factory);

       reader.loadBeanDefinitions(r);

        Person person = (Person) factory.getBean("person");
        System.out.println(person.sayHello());
    }
}
