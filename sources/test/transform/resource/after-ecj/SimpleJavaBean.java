import com.developerworks.lombok.GenerateJavaBean;
import com.developerworks.lombok.GenerateBoundSetter;
@GenerateJavaBean class SetterOnMethod {
  @GenerateBoundSetter int i;
  public static final java.lang.String PROP_I = new java.lang.String("i");
  private final java.beans.PropertyChangeSupport propertySupport = new java.beans.PropertyChangeSupport(this);
  <clinit>() {
  }
  public @java.lang.SuppressWarnings("all") void setI(int i) {
    final int old = this.i;
    this.i = i;
    propertySupport.firePropertyChange(PROP_I, old, this.i);
  }
  public @java.lang.SuppressWarnings("all") void addPropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.addPropertyChangeListener(listener);
  }
  public @java.lang.SuppressWarnings("all") void removePropertyChangeListener(final java.beans.PropertyChangeListener listener) {
    propertySupport.removePropertyChangeListener(listener);
  }
  SetterOnMethod() {
    super();
  }
}