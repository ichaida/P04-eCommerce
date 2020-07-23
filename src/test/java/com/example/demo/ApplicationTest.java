package com.example.demo;

import com.example.demo.controllers.CartControllerTest;
import com.example.demo.controllers.ItemControllerTest;
import com.example.demo.controllers.OrderControllerTest;
import com.example.demo.controllers.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ismailchaida.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CartControllerTest.class,
    ItemControllerTest.class,
    OrderControllerTest.class,
    UserControllerTest.class,
})
@SpringBootTest
public class ApplicationTest {
// This class remains empty, it is used only as a holder for the above annotations
}
