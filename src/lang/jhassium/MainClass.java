package lang.jhassium;

/**
 * File : MainClass.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:52
 */
public class MainClass {

    public static void main(String[] args) {
        HassiumArgumentConfig.executeConfig(new HassiumArgumentParser(args).parse());
    }
}
