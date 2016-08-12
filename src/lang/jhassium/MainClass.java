package lang.jhassium;

import lang.jhassium.utils.HassiumLogger;
import org.apache.commons.lang3.time.StopWatch;

/**
 * File : MainClass.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:52
 */
public class MainClass {

    public static void main(String[] args) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HassiumArgumentConfig.executeConfig(new HassiumArgumentParser(args).parse());
        stopWatch.stop();
        HassiumLogger.info("Total time : " + stopWatch.getTime() + " ms");
    }
}
