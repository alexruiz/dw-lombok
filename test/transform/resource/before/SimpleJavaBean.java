import lombok.AccessLevel;

import lombok.GenerateJavaBean;
import lombok.GenerateBoundSetter;

@GenerateJavaBean
class SimpleJavaBean {
	@GenerateBoundSetter int i;
	@GenerateBoundSetter(AccessLevel.PUBLIC) String s;
	@GenerateBoundSetter(AccessLevel.PROTECTED) float f;
	@GenerateBoundSetter(AccessLevel.PACKAGE) Object o;
	@GenerateBoundSetter(AccessLevel.PRIVATE) double d;
}
