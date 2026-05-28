import org.example.AutoInjectable;

public class BeanWithUnknownInterface {

    public interface UnknownInterface {}

    @AutoInjectable
    private UnknownInterface unknownField;
}
