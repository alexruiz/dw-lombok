import com.developerworks.lombok.GenerateJavaBean;
import com.developerworks.lombok.GenerateBoundSetter;

@GenerateJavaBean
class SetterOnMethod {
	@GenerateBoundSetter int i;
}
