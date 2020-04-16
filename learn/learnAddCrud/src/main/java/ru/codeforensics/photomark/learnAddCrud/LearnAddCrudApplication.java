package ru.codeforensics.photomark.learnAddCrud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;



@SpringBootApplication
public class LearnAddCrudApplication implements CommandLineRunner{

  @Autowired
  private HelloService helloService;

  public static void main(String[] args) {

    //отключаем баннер spring boot, если не хотим видеть его лого в консоли
    SpringApplication app = new SpringApplication(LearnAddCrudApplication.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }

  // В этом методе описываем нашу логику
  @Override
  public void run(String... args) {
    if (args.length > 0) {
      System.out.println(helloService.getMessage(args[0]));
    } else {
      System.out.println(helloService.getMessage());
    }
    exit(0); // завершаем программу
  }
}
