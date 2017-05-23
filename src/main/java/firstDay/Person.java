package firstDay;

/**
 * Created by teng.liu on 2017/5/23.
 */
public class Person {

    private  IHelloMessage iHelloMessage;


    public IHelloMessage getiHelloMessage() {
        return iHelloMessage;
    }

    public void setiHelloMessage(IHelloMessage iHelloMessage) {
        this.iHelloMessage = iHelloMessage;
    }

    public String sayHello(){
        return iHelloMessage.getMessage();
    }
}
