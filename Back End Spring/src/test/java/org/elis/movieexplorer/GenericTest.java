package org.elis.movieexplorer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@SpringBootTest
@ContextConfiguration(classes = {MovieExplorerApplication.class})
@AutoConfigureMockMvc
@TestConstructor(autowireMode = AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class GenericTest {

}
