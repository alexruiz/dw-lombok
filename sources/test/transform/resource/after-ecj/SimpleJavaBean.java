import com.developerworks.lombok.GenerateJavaBean;
import com.developerworks.lombok.GenerateBoundSetter;
@GenerateJavaBean class SetterOnMethod {
  @GenerateBoundSetter int i;
  public static final java.lang.String PROP_I = new java.lang.String("i");
  <clinit>() {
  }
  public @java.lang.SuppressWarnings("all") void setI(int i) {
    final int old = this.i;
    this.i = i;
    propertySupport.firePropertyChange(PROP_I, old, this.i);
  }
  SetterOnMethod() {
    super();
  }
}