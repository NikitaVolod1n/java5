import org.example.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {

    @Test
    void testInjectSomeImplAndSODoer() {
        SomeBean sb = new Injector().inject(new SomeBean());
        // Не должно падать с NullPointerException
        assertDoesNotThrow(sb::foo);
    }

    @Test
    void testFieldsAreNotNullAfterInject() throws Exception {
        SomeBean sb = new Injector().inject(new SomeBean());

        var field1 = SomeBean.class.getDeclaredField("field1");
        field1.setAccessible(true);
        var field2 = SomeBean.class.getDeclaredField("field2");
        field2.setAccessible(true);

        assertNotNull(field1.get(sb), "field1 должен быть проинициализирован");
        assertNotNull(field2.get(sb), "field2 должен быть проинициализирован");
    }

    @Test
    void testInjectedTypesAreCorrect() throws Exception {
        SomeBean sb = new Injector().inject(new SomeBean());

        var field1 = SomeBean.class.getDeclaredField("field1");
        field1.setAccessible(true);
        var field2 = SomeBean.class.getDeclaredField("field2");
        field2.setAccessible(true);

        assertInstanceOf(SomeInterface.class, field1.get(sb));
        assertInstanceOf(SomeOtherInterface.class, field2.get(sb));
    }

    @Test
    void testInjectWorksWithSomeImpl() throws Exception {
        SomeBean sb = new Injector().inject(new SomeBean());

        var field1 = SomeBean.class.getDeclaredField("field1");
        field1.setAccessible(true);

        assertInstanceOf(SomeImpl.class, field1.get(sb), "По умолчанию field1 должен быть SomeImpl");
    }

    @Test
    void testMissingPropertyThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            new Injector().inject(new BeanWithUnknownInterface());
        });
    }
}
