package secondDay;

import com.google.common.collect.Maps;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by teng.liu on 2017/5/24.
 * <p>
 * 利用java的反射 写一个简易的spring xml解析 生成 对应的object
 */
public class BeanFactory {
    Map<String, Object> beansMap = Maps.newHashMap();

    /**
     * 根据配置的spring xml 初始化bean
     *
     * @param xml
     */
    public void initXml(String xml) {
        //读取xml文件内容
        try {
            SAXReader reader = new SAXReader();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = classLoader.getResourceAsStream(xml);

            Document document = reader.read(resourceAsStream);
            Element root = document.getRootElement();
            Element foo;

            //解析xml文件bean节点
            for (Iterator i = root.elementIterator("bean"); i.hasNext(); ) {
                foo = (Element) i.next();

                // 解析每个bean的属性
                Attribute id = foo.attribute("id");
                Attribute aClass = foo.attribute("class");

                // 利用java反射 根据class名称生成相应的对象
                Class<?> bean = Class.forName(aClass.getText());
                java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(bean);
                PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
                // 创建对象
                Object object = bean.newInstance();

                //Field[] declaredFields = bean.getDeclaredFields();

                //创建对象后 填充对象属性 property属性 为set注入 如果需要其他的形式需要扩展
                for (Iterator iterator = foo.elementIterator("property"); iterator.hasNext(); ) {

                    Element foo2 = (Element) iterator.next();

                    //获取配置的name
                    Attribute nameProperty = foo2.attribute("name");

                    //获取配置的value
                    Attribute valueProperty = foo2.attribute("value");

                    //利用反射调用set方法设置对象的属性值
                    for (PropertyDescriptor field : descriptors) {
                        if (field.getName().equals(nameProperty.getText())) {
                            Method writeMethod = null;
                            writeMethod = field.getWriteMethod();
                            writeMethod.invoke(object, valueProperty.getText());
                        }
                    }

                }

                //将生成的对象放入beansMap
                beansMap.put(id.getText(), object);

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("beansMap size: " + beansMap.size() + "!");

    }


    /**
     * 根据name 获取对应的bean对象
     *
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBeans(String beanId) throws Exception {

        Object bean = beansMap.get(beanId);
        if (bean == null) {
            throw new Exception("not find bean by name " + beanId);
        }

        return bean;

    }


    public static void main(String[] args) throws Exception {
        BeanFactory beanFactory = new BeanFactory();

        beanFactory.initXml("applicationContext_myself.xml");

        Book book = (Book) beanFactory.getBeans("book1");

        System.out.println(book.toString());
    }


}
