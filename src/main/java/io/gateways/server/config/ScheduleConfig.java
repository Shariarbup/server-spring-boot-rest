package io.gateways.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ScheduleConfig {
    //@Scheduled(fixedDelay = 1000)---> eita mane hoilo 1s ei method ta call hobe, kintu 1s e full method execution na hoile eita wait korbe
    //method execution er 1s pore abar eita call hobe
    //@Scheduled(fixedRate = 1000)---> kintu eita use korle eita method fully execution er jonne wiat korbe na, 1s por por por call korbe
    //@Scheduled(fixedDelayString = "PT02H") ---> onek large time value jokhon use korbo tokhon ei attribute use korbo and erokom attribute value use krobo
    //@Scheduled(fixedDelayString = "PT02S",initialDelay = 1000)  ---> eita mane hoilo application start houar 1s por theke eita start hobe that means 3s por theke first time eita execution korbe
    //@EnableAsync ---> eita class er upor use korbo , jokhon amader parallel execution dorkar, @Async ---> eita use korbo method er upor
    //@Scheduled(cron = "* * * * * *") ---> second minute hour (day of the month) (month) (day of the week)
    //@Scheduled(cron = "*/2 * * * * *") ---> eita mane hoilo every 2s e eita execute hobe
    //@Scheduled(cron = "0 */2 * * * *")  ---> every 2 min por por eita execution hobe
    //@Scheduled(cron = "0 0 20 * * TUE") ---> on tuesday 8pm this scheduled will be executed
    // Scheduling ta parallely execute korar jonne amra task scheduling use korbo

//    @Autowired
//    private ThreadPoolTaskScheduler taskScheduler;

//    @Scheduled(cron = "${cron.expression.value}")
     public void schedular()throws InterruptedException{
         LocalDateTime current = LocalDateTime.now();
         DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
         String formattedDteTime = current.format(format);
         log.info("Schedular time: "+formattedDteTime);
         Thread.sleep(1000);
     }

//     public int noOfThreads(){
//        //logic returns 3
//         taskScheduler.setPoolSize(3);
//     }
//    public int noOfThreadsFive(){
//        //logic returns 3
//       return taskScheduler.setPoolSize(3);
//    }
}
